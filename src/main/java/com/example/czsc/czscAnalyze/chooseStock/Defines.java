package com.example.czsc.czscAnalyze.chooseStock;

import com.example.czsc.common.Kline;
import com.example.czsc.common.Klines;
import com.example.czsc.czscAnalyze.czscDefine.TaUtils;
import com.example.czsc.czscAnalyze.objects.Frequency;
import com.example.czsc.czscAnalyze.objects.XD;
import com.example.czsc.dataSystem.urlRes.SmallFreqKline;
import com.example.czsc.utils.Utils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.czsc.czscAnalyze.czscDefine.CzscUtils.getLast;
import static com.example.czsc.dataSystem.urlRes.WangYiDayData.getDayKlines;

/**
 * @FileName: Defines
 * @Author: Haifeng Tong
 * @Date: 2021/5/76:45 下午
 * @Description:
 * @History: 2021/5/7
 */
public class Defines {
    /**
     * 形成5日均成交量
     * 成交量大于5日成交量，且比前5天的最高都高
     * 成交量大于昨日的1.5倍
     * @param klines k list
     * @param time 需要判断的时间
     * @return 是否放量
     */
    public static boolean volumeEnlarge(Klines klines, LocalDateTime time){
        float[] volumes = new float[klines.size()];
        Kline kline = klines.get(0);
        int index = 0;
        for(int i=0; i < klines.size();i++){
            volumes[i] = klines.get(i).getVolume();
            if(klines.get(i).getTime().isBefore(time.minusSeconds(59)) && klines.get(i).getTime().isAfter(time.plusSeconds(59))){
                kline = klines.get(i);
                index = i;
            }
        }
        if(index < 4){
            throw new IllegalArgumentException("时间点前的K线太少了，缺乏判断依据");
        }
        //依次放量，至少应该大于10日成交量
        List<Float> ma10 = TaUtils.SMA(volumes,10);
        //不是5日中最大的成交量
        for(int i=index - 5; i<index;i++){
            if (volumes[i] > kline.getVolume()){
                return false;
            }
        }
        assert ma10 != null;
        //小于5日平均成交量，返回false
        if (kline.getVolume() < ma10.get(index)){
            return false;
        }
        return klines.get(index).getVolume()> ma10.get(index)
                && klines.get(index-1).getVolume() > ma10.get(index-1) &&
                ma10.get(index) > ma10.get(index -1);
    }
    public  static boolean volumeEnNarrow(Klines klines,LocalDateTime time){
        float[] volumes = new float[klines.size()];
        Kline kline = klines.get(0);
        int index = 0;
        for(int i=0; i < klines.size();i++){
            volumes[i] = klines.get(i).getVolume();
            if(klines.get(i).getTime().isBefore(time.minusSeconds(59)) && klines.get(i).getTime().isAfter(time.plusSeconds(59))){
                kline = klines.get(i);
                index = i;
            }
        }
        if(index < 4){
            throw new IllegalArgumentException("时间点前的K线太少了，缺乏判断依据");
        }
        //依次放量，至少应该大于10日成交量
        List<Float> ma5 = TaUtils.SMA(volumes,5);
        //不是5日中最大的成交量
        for(int i=index - 5; i<index;i++){
            if (volumes[i] < kline.getVolume()){
                return false;
            }
        }
        assert ma5 != null;
        //小于5日平均成交量，返回false
        if (kline.getVolume() < ma5.get(index)){
            return false;
        }
        return klines.get(index).getVolume() < ma5.get(index)
                && klines.get(index-1).getVolume() < ma5.get(index-1) &&
                ma5.get(index) < ma5.get(index -1);
    }
    //阶段缩量
    public static boolean volumeEnNarrowPeriod(Klines klines,LocalDateTime timeStart,LocalDateTime timeEnd){
        float[] volumes = new float[klines.size()];
        Kline kline = klines.get(0);
        List<Kline> kList = new ArrayList<>();
        for(int i=0; i < klines.size();i++){
            volumes[i] = klines.get(i).getVolume();
            if(klines.get(i).getTime().isBefore(timeEnd.plusSeconds(59)) && klines.get(i).getTime().isAfter(timeStart.minusSeconds(59))){
                kList.add(klines.get(i));
            }
        }
        //依次放量，至少应该大于10日成交量
        List<Float> ma10 = TaUtils.SMA(volumes,10);
        List<Float> ma5 = TaUtils.SMA(volumes,5);
        int startIndex = -1;
        int endIndex = -1;
        assert ma5 != null;
        assert ma10 != null;
        int start = klines.indexOf(kList.get(0));
        int end = klines.indexOf(getLast(kList));
        for(int i = start;i<end;i++){
            if (ma5.get(i - 1) >= ma10.get(i-1) && ma5.get(i) <= ma10.get(i)){
                startIndex = i;
            } else if (ma5.get(i-1) <= ma10.get(i-1) && ma5.get(i) >= ma10.get(i-1)){
                endIndex = i;
            }
            if (startIndex != -1 && endIndex != -1){
                break;
            }
        }
        if (endIndex != -1 && endIndex < startIndex ){
            return false;
        }
        //一直处于成交量的下跌中
        if (startIndex >= start && startIndex != -1){
            return true;
        }else return ma5.get(start) < ma10.get(start) && ma5.get(end) < ma10.get(end);
    }
    /*public static void main(String[] args) throws Exception {
        Klines klines = getDayKlines("002707",);
        List<XD> xds = getXDFromKlineByOrder(klines,LocalDateTime.of(2018, 1, 1, 0, 0, 0));
        if(xds.size() > 0) {
            //关于南京银行的判断，出了一些问题，看来是需要进行一些修正
            //应该在getXDFromKlineByOrder中轮流的进行判断。
            // 如果已经添加了一个上涨的，那么下一个就判断下跌的，
            // 如果下一个发现了更高的点，且下跌不构成线段，那么就认为之前的上涨判断错误，对上一个上涨进行修正
            xds.forEach(Utils::println);
        }
    }*/
}
