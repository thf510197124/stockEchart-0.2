package com.example.czsc.common;



import com.example.czsc.czscAnalyze.objects.Frequency;
import com.sun.istack.internal.NotNull;

import java.time.LocalDateTime;

/**
 * @FileName: Bar
 * @Author: Haifeng Tong
 * @Date: 2021/2/2611:11 下午
 * @Description:
 * @History:
 */
public class Bar implements Comparable<Bar>,Interval{
    private String symbol;
    public LocalDateTime time;
    private Frequency frequency;
    public float high;
    public float low;
    public float open;
    public float close;


    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }


    public float getHigh() {
        return high;
    }

    public void setHigh(float high) {
        this.high = high;
    }

    public float getLow() {
        return low;
    }

    public void setLow(float low) {
        this.low = low;
    }

    public float getOpen() {
        return open;
    }

    public void setOpen(float open) {
        this.open = open;
    }

    public float getClose() {
        return close;
    }

    public void setClose(float close) {
        this.close = close;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public Frequency getFrequency() {
        return frequency;
    }

    public void setFrequency(Frequency frequency) {
        this.frequency = frequency;
    }

    @Override
    public String toString() {
        return "Bar{" +
                "symbol='" + symbol + '\'' +
                ", time=" + time +
                ", high=" + high +
                ", low=" + low +
                ", open=" + open +
                ", close=" + close +
                '}';
    }


    @Override
    public int compareTo(@NotNull Bar o) {
        if (this.getTime().isBefore(o.getTime())){
            return -1;
        }else if(this.getTime().isAfter(o.getTime())){
            return 1;
        }
        return 0;
    }
}
