package com.example.demo.controller.JsonObject;

import com.example.czsc.common.Klines;
import com.example.czsc.czscAnalyze.czscDefine.CenterUtils;
import com.example.czsc.czscAnalyze.objects.Center;
import com.example.czsc.czscAnalyze.objects.XD;

import static com.example.czsc.czscAnalyze.czscDefine.CzscUtils.getLast;
import static com.example.czsc.utils.Utils.printSplit;
import static com.example.czsc.utils.Utils.println;


/**
 * @FileName: XDData
 * @Author: Haifeng Tong
 * @Date: 2021/5/15:03 下午
 * @Description:
 * @History: 2021/5/1
 */
public class XDData{
    private FXData beginFX;
    private FXData endFX;
    private float low;
    private float high;
    private float lowest;
    private float highest;

    public XDData() {
    }
    public XDData(FXData beginFX, FXData endFX, float low, float high, float lowest, float highest) {
        this.beginFX = beginFX;
        this.endFX = endFX;
        this.low = low;
        this.high = high;
        this.lowest = lowest;
        this.highest = highest;
    }
    public XDData(XD xd, String datePattern) {
        beginFX = new FXData(xd.getBis().get(0).getFx_begin(),datePattern);
        endFX = new FXData(getLast(xd.getBis()).getFx_end(),datePattern);
        Center center = new Center(xd.getBis(),true);
        Center.Region region= center.getRecentRegion();
        if(region != null){
            low = region.getLow();
            high = region.getHigh();
            lowest = region.getLowest();
            highest = region.getHighest();
        }
    }

    public FXData getBeginFX() {
        return beginFX;
    }

    public void setBeginFX(FXData beginFX) {
        this.beginFX = beginFX;
    }

    public FXData getEndFX() {
        return endFX;
    }

    public void setEndFX(FXData endFX) {
        this.endFX = endFX;
    }

    public float getLow() {
        return low;
    }

    public void setLow(float low) {
        this.low = low;
    }

    public float getHigh() {
        return high;
    }

    public void setHigh(float high) {
        this.high = high;
    }

    public float getLowest() {
        return lowest;
    }

    public void setLowest(float lowest) {
        this.lowest = lowest;
    }

    public float getHighest() {
        return highest;
    }

    public void setHighest(float highest) {
        this.highest = highest;
    }

    @Override
    public String toString() {
        return "XDData{" +
                "beginFX=" + beginFX +
                ", endFX=" + endFX +
                ", low=" + low +
                ", high=" + high +
                ", lowest=" + lowest +
                ", highest=" + highest +
                '}';
    }
}
