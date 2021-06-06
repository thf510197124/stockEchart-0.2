package com.example.czsc.common;



import com.example.czsc.czscAnalyze.objects.Frequency;
import com.sun.istack.internal.NotNull;

import java.time.LocalDateTime;
import java.util.*;

import static com.example.czsc.czscAnalyze.czscDefine.CzscUtils.getLast;


/**
 * @FileName: KLines
 * @Author: Haifeng Tong
 * @Date: 2021/2/2611:29 下午
 * @Description:
 * @History:
 */
public class Klines extends ArrayList<Kline>{
    private Frequency frequency;
    public Klines(){
        super();
    }
    public Klines(List<Kline> bars){
        super(bars);
    }

    public Klines(Frequency frequency) {
        super();
        this.frequency = frequency;
    }

    public static Klines subKLinesAfterAndEqualTime(Klines klines, LocalDateTime time) {
        Klines ks = subKLinesAfterTime(klines,time);
        if (ks.size() ==0){
            return null;
        }
        Klines before = subKLinesBeforeTime(klines, time);
        if(before.size() == 0){
            return ks;
        }
        else if (getLast(before) != ks.get(0)) {
            ks.add(0, getLast(subKLinesBeforeTime(klines, time)));
        }
        return ks;
    }

    public String getSymbol() {
        return get(0).getSymbol();
    }

    public String getName() {
        return get(0).getName() == null || get(0).getName().equals("") ? getLast(this).getName() : get(0).getName();
    }

    public LocalDateTime getStart() {
        return get(0).getTime();
    }

    public LocalDateTime getEnd() {
        return getLast(this).getTime();
    }

    public Frequency getFrequency() {
        if (frequency !=null){
            return frequency;
        }
        if(size() == 0 || size() == 1){
            return Frequency.DAY;
        }else if(size() == 2 ){
            return Frequency.frequency(get(0).getTime(),get(1).getTime());
        }else{
            Frequency f1 = Frequency.frequency(get(0).getTime(),get(1).getTime());
            Frequency f2 = Frequency.frequency(get(1).getTime(),get(2).getTime());
            return f1.getMinutes() > f2.getMinutes() ? f2 : f1;
        }
    }

    public void setFrequency(Frequency frequency) {
        this.frequency = frequency;
    }

    @Override
    public @NotNull
    List<Kline> subList(int startIndex, int endIndex) {
        if (endIndex < size()){
            return super.subList(startIndex,endIndex);
        }
        else{
            return super.subList(startIndex,size());
        }
    }

    //取当前和之前的所有K线
    public static Klines subKLinesBeforeTime(Klines klines,LocalDateTime today){
        List<Kline> subKLines = new ArrayList<>();
        for (Kline kline : klines){
            if (kline.getTime().isBefore(today.plusSeconds(1))){
                subKLines.add(kline);
            }
        }
        return new Klines(subKLines);
    }
    public static Klines subKLinesAfterTime(Klines klines,LocalDateTime today){
        List<Kline> subKLines = new ArrayList<>();
        for (Kline kline : klines){
            if (kline.getTime().isAfter(today.minusSeconds(1))){
                subKLines.add(kline);
            }
        }
        return new Klines(subKLines);
    }
    //如果刚好有那个时刻的，就取那个时刻的。没有就取之前最近一个的
    public Kline getBarByDate(LocalDateTime date){
        for (int i = 0;i<size();i++){
            Kline bar = get(i);
            if (bar.getTime().equals(date)){
                return bar;
            }else{
                if (i < size() - 1 && bar.getTime().isBefore(date) && get(i+1).getTime().isAfter(date)){
                    return bar;
                }
            }
        }
        return null;
    }
    public static Klines copyKlines(Klines klines){
        return new Klines(klines);
    }

    public void sort() {
        this.sort((o1, o2) -> {
            if (o1.getTime().isBefore(o2.getTime())) {
                return -1;
            } else if (o1.getTime().isAfter(o2.getTime())) {
                return 1;
            }
            return 0;
        });
    }
}
