package com.example.demo.controller.JsonObject;

import java.io.Serializable;

/**
 * @FileName: OCLHData
 * @Author: Haifeng Tong
 * @Date: 2021/4/274:07 下午
 * @Description:
 * @History: 2021/4/27
 */
public class OCLHData implements Serializable {
    private String open;
    private String close;
    private String low;
    private String high;
    public OCLHData(){}
    public OCLHData(float open,float close,float low,float high){
        this.open = getScaleString(open);
        this.close = getScaleString(close);
        this.low = getScaleString(low);
        this.high = getScaleString(high);
    }
    public OCLHData(String open,String close,String low,String high){
        this.open = open;
        this.close = close;
        this.low = low;
        this.high = high;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getClose() {
        return close;
    }

    public void setClose(String close) {
        this.close = close;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }
    public void setOpen(float open){
        this.open = getScaleString(open);
    }
    public void setClose(float close){
        this.close = getScaleString(close);
    }
    public void setLow(float low){
        this.low = getScaleString(low);
    }
    public void setHigh(float high){
        this.high = getScaleString(high);
    }
    public String toOCLHDataString(){
        return "[" + open + ","+ close + ","+low + ","+ close + "]";
    }
    public String toString(){
        return this.toOCLHDataString();
    }

    private static String getScaleString(float number){
        return String.format("%.2f",number);
    }
}
