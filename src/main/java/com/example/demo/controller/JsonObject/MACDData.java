package com.example.demo.controller.JsonObject;

import com.example.czsc.czscAnalyze.objects.Frequency;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @FileName: MACDData
 * @Author: Haifeng Tong
 * @Date: 2021/4/275:24 下午
 * @Description:
 * @History: 2021/4/27
 */
public class MACDData implements Serializable {
    public static final int GREEN = 1;
    public static final int RED = -1;
    private int index;
    private String macd;
    private int isGreen;

    public MACDData() {
    }
    public MACDData(int index,float macd,int isGreen){
        this.index = index;
        this.macd = getScaleString(macd);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getMacd() {
        return macd;
    }

    public void setMacd(String macd) {
        this.macd = macd;
    }
    public void setMacd(float macd){
        this.macd = getScaleString(macd);
    }
    public int getIsGreen() {
        return isGreen;
    }

    public void setIsGreen(int isGreen) {
        this.isGreen = isGreen;
    }

    @Override
    public String toString() {
        return "[" + index + "," + macd + "," + isGreen + "]";
    }
    private static String getScaleString(float num){
        return getScaleString(num,null);
    }
    public static String getScaleString(float number,Frequency frequency){
        int times = 2;
        if (frequency != null) {
            switch (frequency) {
                case HOUR:
                case MIN30:
                case MIN15:
                    times = 3;
                    break;
                case MIN5:
                case MIN:
                    times = 4;
                    break;
            }
        }
        if(Float.isNaN(number)){
            return "NaN";
        }
        if(number < 0.0001){
            times = 5;
        }else if (number < 0.001){
            times = 4;
        }else if (number < 0.01){
            times = 3;
        }
        return String.format("%." + times + "f",number);
    }
    public static  List<String> getScaleList(List<Float> list,Frequency frequency){
        List<String> strList = new ArrayList<>();
        for(float f : list){
            strList.add(getScaleString(f,frequency));
        }
        return strList;
    }
}
