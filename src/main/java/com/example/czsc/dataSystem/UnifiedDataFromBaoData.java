package com.example.czsc.dataSystem;

import com.example.czsc.common.Klines;
import com.example.czsc.czscAnalyze.objects.Frequency;
import com.example.czsc.dataSystem.simpleData.DataFromBockStockCSV;
import com.example.czsc.dataSystem.urlRes.SmallFreqKline;
import com.example.czsc.dataSystem.urlRes.WangYiDayData;

import java.io.IOException;
import java.time.LocalDateTime;

import static com.example.czsc.czscAnalyze.czscDefine.CzscUtils.getLast;

/**
 * @FileName: UnifiedDataFromBaoData
 * @Author: Haifeng Tong
 * @Date: 2021/5/129:08 下午
 * @Description:
 * @History: 2021/5/12
 */
public class UnifiedDataFromBaoData {
    public static Klines getKlines(String symbol, LocalDateTime dateBegin, LocalDateTime dateEnd, Frequency frequency) throws IOException {
        Klines klines = null;
        if (frequency == Frequency.YEAR){
            klines = WangYiDayData.getYearKlines(symbol,dateBegin,dateEnd);
        }else if (frequency == Frequency.QUARTER){
            klines = WangYiDayData.getQuarterKlines(symbol,dateBegin,dateEnd);
        }
        else if (frequency == Frequency.MONTH){
            klines = DataFromBockStockCSV.getKlines(symbol,Frequency.MONTH);
        } else if(frequency == Frequency.WEEK){
            klines = DataFromBockStockCSV.getKlines(symbol,Frequency.WEEK);
        }else if (frequency== Frequency.DAY){
            klines = DataFromBockStockCSV.getKlines(symbol,Frequency.DAY);
            if(dateEnd != null) {
                klines = Klines.subKLinesBeforeTime(klines, dateEnd);
            }
            if (dateBegin != null) {
                klines = Klines.subKLinesAfterTime(klines, dateBegin);
            }
        }else if (frequency == Frequency.MIN30){//需要判断最后一根K线是否出现，有时候娶不到最后一根K线
            klines = DataFromBockStockCSV.getKlines(symbol,Frequency.MIN30);
        }else if (frequency == Frequency.MIN5){
            klines = DataFromBockStockCSV.getKlines(symbol,Frequency.MIN5);
        }
        if (klines == null || klines.size() == 0){
            throw new IllegalArgumentException("可能查询了不支持的周期");
        }
        return klines;
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
