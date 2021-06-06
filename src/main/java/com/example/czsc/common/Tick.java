package com.example.czsc.common;

import java.time.LocalDateTime;

/**
 * @FileName: Tick
 * @Author: Haifeng Tong
 * @Date: 2021/2/2610:53 下午
 * @Description: 某个周期的状态
 * @History:
 */
public class Tick extends Kline implements Interval{
    private String symbol;//代码
    private String name;//名称
    private LocalDateTime time;//代表时间
    private float open_interest;//？持仓量

    private float limit_up; //涨停价
    private float limit_down;//跌停价

    private float open; //开盘
    private float high;//收盘
    private float low;//最低
    private float close;//最高
    private float pre_close;//昨收

    private float volume;//成交量
    private float pre_volume;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public float getOpen_interest() {
        return open_interest;
    }

    public void setOpen_interest(float open_interest) {
        this.open_interest = open_interest;
    }

    public float getLimit_up() {
        return limit_up;
    }

    public void setLimit_up(float limit_up) {
        this.limit_up = limit_up;
    }

    public float getLimit_down() {
        return limit_down;
    }

    public void setLimit_down(float limit_down) {
        this.limit_down = limit_down;
    }

    public float getOpen() {
        return open;
    }

    public void setOpen(float open) {
        this.open = open;
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

    public float getClose() {
        return close;
    }

    public void setClose(float close) {
        this.close = close;
    }

    public float getPre_close() {
        return pre_close;
    }

    public void setPre_close(float pre_close) {
        this.pre_close = pre_close;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public float getPre_volume() {
        return pre_volume;
    }

    public void setPre_volume(float pre_volume) {
        this.pre_volume = pre_volume;
    }

}
