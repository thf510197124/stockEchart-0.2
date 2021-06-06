package com.example.demo.controller.JsonObject;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.czsc.common.Kline;
import com.example.czsc.common.Klines;
import com.example.czsc.czscAnalyze.czscDefine.Analyze;
import com.example.czsc.czscAnalyze.czscDefine.TaUtils;
import com.example.czsc.czscAnalyze.objects.BI;
import com.example.czsc.czscAnalyze.objects.FX;
import com.example.czsc.czscAnalyze.objects.Frequency;
import com.example.czsc.czscAnalyze.objects.XD;
import org.springframework.web.servlet.ModelAndView;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


/**
 * @FileName: ControllerUtils
 * @Author: Haifeng Tong
 * @Date: 2021/5/176:03 下午
 * @Description:
 * @History: 2021/5/17
 */
public class ControllerUtils {
    public static List<List<String>> getCandleData(Klines klines,String datePattern){
        List<List<String>> candlesData = new ArrayList<>();
        for (Kline kline : klines) {
            List<String> oclh = new ArrayList<>();
            DateTimeFormatter dt = DateTimeFormatter.ofPattern(datePattern);
            oclh.add(dt.format(kline.getTime()));
            oclh.add(getScaleString(kline.getOpen()));
            oclh.add(getScaleString(kline.getClose()));
            oclh.add(getScaleString(kline.getLow()));
            oclh.add(getScaleString(kline.getHigh()));
            oclh.add(getScaleString(kline.getVolume() / 100));
            candlesData.add(oclh);
        }
        return candlesData;
    }
    private static String formatNumber(int index){
        String number;
        if (index < 10){
            number = "10000"+index;
        }else if (index < 100){
            number = "1000" + index;
        }else if (index < 1000){
            number = "100" + index;
        }else if (index < 10000){
            number = "10" + index;
        }else if (index < 100000){
            number = "1" + index;
        }else{
            number = "" + index;
        }
        return number;
    }
    public static List<Float> closeList(Klines klines){
        List<Float> closeList = new ArrayList<>();
        for (Kline kline : klines) {
            closeList.add(kline.getClose());
        }
        return closeList;
    }
    public static float[] toArrayFloat(List<Float> l){
        float[] f = new float[l.size()];
        for (int i=0;i<l.size(); i++){
            f[i] = l.get(i);
        }
        return f;
    }
    public static double[] toArrayDouble(List<Double> l){
        double[] f = new double[l.size()];
        for (int i=0;i<l.size(); i++){
            f[i] = l.get(i);
        }
        return f;
    }
    public static int[] toArrayInteger(List<Integer> l){
        int[] f = new int[l.size()];
        for (int i=0;i<l.size(); i++){
            f[i] = l.get(i);
        }
        return f;
    }
    public static String[] toArrayString(List<String> l){
        String[] f = new String[l.size()];
        for (int i=0;i<l.size(); i++){
            f[i] = l.get(i);
        }
        return f;
    }
    public static long[] toArrayLong(List<Long> l){
        long[] f = new long[l.size()];
        for (int i=0;i<l.size(); i++){
            f[i] = l.get(i);
        }
        return f;
    }
    public static List<XDData> transformerXDData(List<XD> xdList,String datePattern) {
        List<XDData> xds = new ArrayList<>();
        if(xdList != null && xdList.size() > 0){
            for (XD xd : xdList){
                XDData xdData = new XDData(xd, datePattern);
                xds.add(xdData);
            }
        }
        if(xds.size() > 0){
            return xds;
        }
        return null;
    }
    public static <T> JSONArray toJSONArray(List<T> list){
        return JSONArray.parseArray(JSON.toJSONString(list));
    }
    public static void frequencyAnalyze(Klines klines, Frequency freq, String datePattern,String prefix,ModelAndView mav) throws Exception {
        Analyze analysis = new Analyze(klines,freq);
        List<List<String>> candlesData = ControllerUtils.getCandleData(klines,datePattern);

        mav.addObject("candlesData"+ prefix,toJSONArray(candlesData));
        TaUtils.MACD macd = TaUtils.MACD(toArrayFloat(closeList(klines)),12,26,9);
        mav.addObject("macds"+ prefix,toJSONArray(getMacds(macd,freq)));
        mav.addObject("diffs"+ prefix,toJSONArray(getDiff(macd,freq)));
        mav.addObject("deas" + prefix,toJSONArray(getDeas(macd,freq)));
        mav.addObject("freqStr"+ prefix,freq.getName().length() > 1 ? freq.getName() : freq.getName() + "K线");
        List<FX> fies = analysis.getFX();

        List<BI> bis = analysis.getBIS();
        List<XDData> xds = transformerXDData(analysis.getXD(),datePattern);
        List<FXData> fxData = transformerFxData(fies,datePattern);//在数据中添加
        mav.addObject("fx" + prefix,toJSONArray(fxData));
        if(bis != null && bis.size()> 0){
            BisData bisData = new BisData(bis,datePattern);//只取最后一个中枢
            mav.addObject("bisCenter" + prefix, JSONObject.toJSON(bisData));
        }
        if (xds != null && xds.size() > 0) {
            mav.addObject("xds" + prefix,toJSONArray(xds));
        }
    }
    public static List<FXData> transformerFxData(List<FX> fies,String datePattern) throws Exception {
        List<FXData> fxData = new ArrayList<>();
        if (fies == null){
            return null;
        }
        for(FX fx : fies){
            FXData f = new FXData(fx,datePattern);
            fxData.add(f);
        }
        return fxData;
    }
    public static String getScaleString(float number){
        return String.format("%.2f",number);
    }
    public static String determineDatePattern(Frequency frequency){
        if (frequency.getMinutes() > 10080) {
            //月线，年线，取10年
            return "yyyy-MM";
        } else if (frequency.getMinutes() >= 1440) {
            //周线，月线，年线，取10年
            return  "yy-MM-dd";
        }else if (frequency.getMinutes() <= 30){
            return  "MM-dd HH:mm";
        }
        return  "yy-MM-dd";
    }
    public static List<String> getDiff(TaUtils.MACD macd,Frequency frequency){
        return MACDData.getScaleList(macd.getDiff(),frequency);
    }
    public static List<String> getDeas(TaUtils.MACD macd,Frequency frequency){
        return MACDData.getScaleList(macd.getDea(),frequency);
    }
    public static List<String> getMacds(TaUtils.MACD macd,Frequency frequency){
        return MACDData.getScaleList(macd.getMacd(),frequency);
    }

}
