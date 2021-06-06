package com.example.czsc.czscAnalyze.objects;



import com.example.czsc.common.Interval;
import com.example.czsc.common.Kline;
import com.example.czsc.common.Klines;
import com.sun.istack.internal.NotNull;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

/**
 * @FileName: BI
 * @Author: Haifeng Tong
 * @Date: 2021/2/2611:21 下午
 * @Description: 分笔
 * @History:
 */
public class BI implements Comparable<BI>, Interval {
    private FX fx_begin;
    private FX fx_end;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private Direction direction;
    private float high;
    private float low;
    private float highest = Float.NaN;
    private float lowest = Float.NaN;
    public BI(){}
    public BI(FX fx_begin, FX fx_end) {
        this.fx_begin = fx_begin;
        this.fx_end = fx_end;
        this.beginTime = fx_begin.getTime();
        this.endTime = fx_end.getTime();
        this.direction = fx_begin.getMark() == Mark.D ? Direction.UP : Direction.DOWN;
        this.high = Math.max(fx_begin.getFx(),fx_end.getFx());
        this.low= Math.min(fx_begin.getFx(),fx_end.getFx());
    }
    public BI(BI bi){
        this.fx_begin = bi.getFx_begin();
        this.fx_end = bi.getFx_end();
        this.beginTime = bi.getBeginTime();
        this.endTime = bi.getEndTime();
        this.direction = bi.getDirection();
        this.high = bi.getHigh();
        this.low = bi.getLow();
    }


    public FX getFx_begin() {
        return fx_begin;
    }

    public void setFx_begin(FX fx_begin) {
        this.fx_begin = fx_begin;
    }

    public FX getFx_end() {
        return fx_end;
    }

    public void setFx_end(FX fx_end) {
        this.fx_end = fx_end;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
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

    public LocalDateTime getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(LocalDateTime beginTime) {
        this.beginTime = beginTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "BI{" +
                ", fx_begin=" + fx_begin.getMark() +
                ", fx_end=" + fx_end.getMark() +
                ", beginTime=" + beginTime +
                ", endTime=" + endTime +
                ", direction=" + direction +
                ", high=" + high +
                ", low=" + low +
                '}';
    }

    @Override
    public int compareTo(@NotNull BI o) {
        if (this.getBeginTime().isBefore(o.getBeginTime())){
            return -1;
        }else if(this.getBeginTime().isAfter(o.getBeginTime())){
            return 1;
        }
        return 0;
    }
    public float getHighest(Klines klines){
        if (Float.isNaN(highest)) {
            initHighAndLow(klines);
        }
        return highest;
    }

    private void initHighAndLow(Klines klines) {
        float lowest = Float.MAX_VALUE;
        float highest = Float.MIN_VALUE;
        for(Kline kline : klines){
            if(kline.getTime().isBefore(this.getEndTime().plusSeconds(1)) &&
                    kline.getTime().isAfter(this.getBeginTime().minusSeconds(1))){
                if(kline.getLow() < lowest) lowest = kline.getLow();
                if(kline.getHigh() > highest) highest = kline.getHigh();
            }
        }
        this.highest = highest;
        this.lowest = lowest;
    }

    public float getLowest(Klines klines){
        if (Float.isNaN(lowest)) {
            initHighAndLow(klines);
        }
        return lowest;
    }

}
