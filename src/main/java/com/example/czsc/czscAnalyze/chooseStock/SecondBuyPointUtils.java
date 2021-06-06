package com.example.czsc.czscAnalyze.chooseStock;

import com.example.czsc.common.Bar;
import com.example.czsc.common.Kline;
import com.example.czsc.common.Klines;
import com.example.czsc.czscAnalyze.czscDefine.FxUtils;
import com.example.czsc.czscAnalyze.czscDefine.TaUtils;
import com.example.czsc.czscAnalyze.objects.FX;
import com.example.czsc.dataSystem.simpleData.DataFromBockStockCSV;
import com.sun.istack.internal.Nullable;


import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.czsc.czscAnalyze.czscDefine.CzscUtils.getLast;
import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util.println;


/**
 * @FileName: SecondBuyPointUtils
 * @Author: Haifeng Tong
 * @Date: 2021/3/288:36 下午
 * @Description:
 * @History:
 */
public class SecondBuyPointUtils {
    private static List<Bar> subListKlines(Klines klines, @Nullable LocalDateTime startTime, @Nullable LocalDateTime endTime){
        List<Bar> ks = new ArrayList<Bar>();
        for (Bar k : klines){
            if((startTime == null && k.getTime().isBefore(endTime)) ||
                    (endTime == null && k.getTime().isAfter(startTime))
            ){
                ks.add(k);
            }
        }
        return ks;
    }
    //部分段的macd
    private static boolean checkMACD(List<Kline> klines, LocalDateTime startTime, LocalDateTime endTime){
        TaUtils.MACD macd = TaUtils.MACDFromTA(TaUtils.closeList(klines),12,26,9);
        List<Float> macds = macd.getMacd();
        List<Float> diffs = macd.getDiff();
        List<Float> deas = macd.getDea();
        int[] index = new int[2];
        for (Bar bar : klines){
            if(bar.getTime().isEqual(endTime)){
                int i  = klines.indexOf(bar);
                index[1] = i;
            }
            if(bar.getTime().isEqual(startTime)){
                int i  = klines.indexOf(bar);
                index[0] = i;
            }
        }
        float maxDiff = Float.MIN_VALUE,maxDea = Float.MIN_VALUE;
        float minMacd = Float.MAX_VALUE;
        for (int i = index[0];i<macds.size();i++){
            if(i <= index[1]){
                if(diffs.get(i) >maxDiff){
                    maxDiff = diffs.get(i);
                }
                if(deas.get(i) > maxDea){
                    maxDea = deas.get(i);
                }
            }else{
                if(macds.get(i) < minMacd){
                    minMacd = macds.get(i);
                }
            }
        }
        //需要前面的diff和dea > 0,而后面的macd
        //前面的macd > 0.2 ，后面的macd < 0，还需要检查当前macd 小于0
        return maxDiff > 0.3 && maxDea > 0.3 && minMacd < -0.3 && getLast(macds) < 0;
    }
    public static void main(String[] args) throws IOException {
        String filePath = "/Users/haifeng/Documents/stock_database/baostock";
        File root = new File(filePath);
        File[] files = root.listFiles();
        List<String> chooses = new ArrayList<>();
        for (File file : files){
            String path = file.getAbsolutePath();
            if(!path.contains("0") && (!path.contains("6")) && (!path.contains("3"))){//文件夹中包含一些特殊的文件
                continue;
            }
            Klines klines = DataFromBockStockCSV.read(path, LocalDateTime.of(2020,1,1,0,0,0));
            List<FX> fies = FxUtils.divideFx(klines,true);
            if (fies == null || fies.size() < 2){
                continue;
            }
            LocalDateTime startTime = fies.get(fies.size() - 2).getTime();
            LocalDateTime endTime = getLast(fies).getTime();
            if (checkMACD(klines,startTime, endTime)){
                chooses.add(klines.getSymbol());
            }
        }
        chooses.forEach(e->println("符合要求" + e));
    }
}
