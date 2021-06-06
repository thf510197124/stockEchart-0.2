package com.example.czsc.czscAnalyze.objects;


import com.example.czsc.common.Interval;
import com.example.czsc.common.Kline;
import com.example.czsc.common.Klines;
import com.example.czsc.czscAnalyze.czscDefine.FxUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @FileName: FX
 * @Author: Haifeng Tong
 * @Date: 2021/2/2611:19 下午
 * @Description: 分型
 * @History:
 */
public class FX implements Interval {
    private LocalDateTime time;//是分型3根
    private Mark mark;
    private float high;
    private float low;
    private List<Kline> bars;
    private float fx;

    public FX(){}
    public FX(Kline bar1, Kline bar2, Kline bar3){
        this.time = bar2.getTime();
        this.mark = FxUtils.confirmMark(bar1,bar2,bar3);
        assert this.mark != null;
        if (mark == Mark.G){
            this.high = bar2.getHigh();
            this.low = Math.min(bar1.getLow(),bar3.getLow());
            this.fx = this.high;
        }else{
            this.low = bar2.getLow();
            this.high = Math.max(bar1.getHigh(),bar3.getHigh());
            this.fx = this.low;
        }
        List<Kline> bars  = new ArrayList<>();
        bars.add(bar1);bars.add(bar2);bars.add(bar3);
        this.bars = bars;
    }
    public FX(Kline bar2,Kline bar3){
        this.time = bar2.getTime();
        this.mark = bar2.getHigh() > bar3.getHigh() ? Mark.G : Mark.D;
        if (mark == Mark.G){
            this.high = bar2.getHigh();
            this.low = bar3.getLow();
            this.fx = this.high;
        }else{
            this.low = bar2.getLow();
            this.high =bar3.getHigh();
            this.fx = this.low;
        }
        List<Kline> bars  = new ArrayList<>();
        bars.add(bar2);bars.add(bar3);
        this.bars = bars;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public Mark getMark() {
        return mark;
    }

    public void setMark(Mark mark) {
        /*System.out.println("----------------------------------------------------------");
        System.out.println("mark 更改了，原来的分型为" + this.getMark());
        System.out.println("mark 更改了，更改为" + mark);*/
        this.mark = mark;
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

    public List<Kline> getBars() {
        return bars;
    }

    public void setBars(List<Kline> newBars) {
        this.bars = newBars;
    }

    public float getFx() {
        return fx;
    }

    public void setFx(float fx) {
        this.fx = fx;
    }

    @Override
    public String toString() {
        return "FX{" +
                "time=" + time +
                ", mark=" + mark +
                ", fx=" + fx +
                ", bars = " + bars +
                '}';
    }
}
