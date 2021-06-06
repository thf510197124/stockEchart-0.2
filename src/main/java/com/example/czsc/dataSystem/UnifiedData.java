package com.example.czsc.dataSystem;

import com.example.czsc.common.Kline;
import com.example.czsc.common.Klines;
import com.example.czsc.czscAnalyze.objects.Frequency;
import com.example.czsc.dataSystem.urlRes.SmallFreqKline;
import com.example.czsc.dataSystem.urlRes.WangYiDayData;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.example.czsc.czscAnalyze.czscDefine.CzscUtils.getLast;

/**
 * @FileName: UnifiedData
 * @Author: Haifeng Tong
 * @Date: 2021/5/116:43 下午
 * @Description:
 * @History: 2021/5/11
 */
public class UnifiedData {
    public static Klines getKlines(String symbol, LocalDateTime dateBegin, LocalDateTime dateEnd, Frequency frequency){
        Klines klines;
        if (frequency == Frequency.YEAR){
            klines = WangYiDayData.getYearKlines(symbol,dateBegin,dateEnd);
        }else if (frequency == Frequency.QUARTER){
            klines = WangYiDayData.getQuarterKlines(symbol,dateBegin,dateEnd);
        }
        else if (frequency == Frequency.MONTH){
            klines = WangYiDayData.getMonthKlines(symbol,dateBegin,dateEnd);
        } else if(frequency == Frequency.WEEK){
            klines = WangYiDayData.getWeekKlines(symbol,dateBegin,dateEnd);
        }else if (frequency== Frequency.DAY){
            klines = WangYiDayData.getDayKlines(symbol,dateBegin,dateEnd);
            //return klines;
            if (dateEnd == null){
                return klines;
            }
            if (klines != null && dateEnd.isAfter(LocalDateTime.now().minusHours(2))) {
                WangYiDayData.combineLostDays(klines);
            }
        }else if (frequency == Frequency.MIN){//需要判断最后一根K线是否出现，有时候娶不到最后一根K线
            klines = SmallFreqKline.oneMinuteKline(symbol);
        }else{
            klines = SmallFreqKline.minuteKline(symbol,frequency);
        }
        if (klines == null || klines.size() == 0){
            try {
                return UnifiedDataFromBaoData.getKlines(symbol, dateBegin, dateEnd, frequency);
            }catch(IOException exception){
                exception.printStackTrace();
            }
            throw new IllegalArgumentException("一条K线都没找到");
        }
        return klines;
    }
    public static Klines getMonthKlines(Klines dayKlines){
        return WangYiDayData.getMonthKlines(dayKlines);
    }
    public static Klines getWeekKlines(Klines dayKlines){
        return WangYiDayData.getWeekKlines(dayKlines);
    }
    public static List<Float> factor(Klines klines){
        LinkedList<Float> factors = new LinkedList<>();
        factors.addFirst(1.0f);
        float factor = getLast(factors);
        for (int i = klines.size() - 1;i > 0;i--){//循环中设置的是preK的factor，序号为i-1
            Kline lastKline = klines.get(i);
            Kline preK = klines.get(i-1);
            if(preK.getClose() != lastKline.getPreClose()){
                factor = lastKline.getPreClose() / preK.getClose() * factor;
            }
            factors.addFirst(factor);
        }
        return factors;
    }
    public static Klines fuQuan(Klines klines,List<Float> factors){
        Klines fuquan = new Klines();
        for (int i=0;i<klines.size();i++){
            Kline k = new Kline(klines.get(i));
            float factor = factors.get(i);
            k.setOpen(round(k.getOpen() * factor));
            k.setClose(round(k.getClose() * factor));
            k.setHigh(round(k.getHigh() * factor));
            k.setLow(round(k.getLow() * factor));
            k.setPreClose(round(k.getPreClose() * factor));
            fuquan.add(k);
        }
        return fuquan;
    }
    private static float round(float price){
        return ((float)Math.round(price * 100) / 100);
    }
    public static Klines getQuarterKlines(Klines monthKlines){
        return WangYiDayData.getQuarterKlines(monthKlines);
    }
    public static Klines getYearKlines(Klines monthKlines){
        return WangYiDayData.getYearKlines(monthKlines);
    }
    private static LocalDateTime getDateEnd(Klines klines, LocalDateTime dateEnd){
        if (dateEnd != null){
            return dateEnd;
        }
        else{
            if (klines.size() > 0){
                return getLast(klines).getTime();
            }else{
                return LocalDateTime.now();
            }
        }
    }
}
