package com.example.demo.controller.JsonObject;

import com.example.czsc.czscAnalyze.objects.FX;
import com.example.czsc.czscAnalyze.objects.Mark;
import com.example.czsc.czscAnalyze.objects.XD;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @FileName: FXData
 * @Author: Haifeng Tong
 * @Date: 2021/4/306:35 下午
 * @Description:
 * @History: 2021/4/30
 */
public class FXData {
    private String time;
    private Mark mark;
    private float fx;

    public FXData() {
    }
    public FXData(FX fx,String datePattern){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(datePattern);
        this.time = dtf.format(fx.getTime());
        this.mark = fx.getMark();
        this.fx = fx.getFx();
    }
    public FXData(String time, Mark mark, float fx) {
        this.time = time;
        this.mark = mark;
        this.fx = fx;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Mark getMark() {
        return mark;
    }

    public void setMark(Mark mark) {
        this.mark = mark;
    }

    public float getFx() {
        return fx;
    }

    public void setFx(float fx) {
        this.fx = fx;
    }

    @Override
    public String toString() {
        return "FXData{" +
                "time='" + time + '\'' +
                ", mark=" + mark +
                ", fx=" + fx +
                '}';
    }
}
