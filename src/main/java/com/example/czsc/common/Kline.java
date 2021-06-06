package com.example.czsc.common;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Objects;

/**
 * @FileName: Kline
 * @Author: Haifeng Tong
 * @Date: 2021/3/15:10 下午
 * @Description:
 * @History:
 */
public class Kline extends Bar implements Comparator<Kline> {
    private String symbol;
    private String name;
    private LocalDateTime time;
    private float open;
    private float close;
    private float high;
    private float low;
    private float preClose;//昨
    //private float priceChange;//涨跌额，通过方法来计算
    //private float priceChangePercent;//涨跌幅
    private float exchangePercent;//换手率
    private float volume;//成交量
    private float exchangeMoney;//成交金额

    public Kline() {
    }
    public Kline(Kline kline){
        this.symbol = kline.getSymbol();
        this.name = kline.getName();
        this.time = kline.getTime();
        this.open = kline.getOpen();
        this.close = kline.getClose();
        this.high = kline.getHigh();
        this.low  = kline.getLow();
        this.preClose = kline.getPreClose();
        this.exchangePercent = kline.getExchangePercent();
        this.volume = kline.getVolume();
        this.exchangeMoney = kline.getExchangeMoney();
    }

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

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    @Override
    public int compare(Kline o1, Kline o2) {
        if (o1.getTime().isBefore(o2.getTime())){
            return -1;
        }else if(o1.getTime().isAfter(o2.getTime())){
            return 1;
        }
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Kline bar = (Kline) o;
        return Float.compare(bar.open, open) == 0 &&
                Float.compare(bar.close, close) == 0 &&
                Float.compare(bar.high, high) == 0 &&
                Float.compare(bar.low, low) == 0 &&
                Float.compare(bar.volume, volume) == 0 &&
                Objects.equals(symbol, bar.symbol) &&
                Objects.equals(time, bar.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol, time, open, close, high, low, volume);
    }

    public float getPreClose() {
        return preClose;
    }

    public void setPreClose(float preClose) {
        this.preClose = preClose;
    }

    public float getPriceChange() {
        return getClose() - getPreClose();
    }

    public float getPriceChangePercent() {
        assert getPreClose() != 0;
        return (getPriceChange() / getPreClose()) * 100;
    }

    public float getExchangePercent() {
        return exchangePercent;
    }

    public void setExchangePercent(float exchangePercent) {
        this.exchangePercent = exchangePercent;
    }

    public float getExchangeMoney() {
        return exchangeMoney;
    }

    public void setExchangeMoney(float exchangeMoney) {
        this.exchangeMoney = exchangeMoney;
    }

    @Override
    public String toString() {
        return "Kline{" +
                "name = " + name +
                ", symbol='" + symbol + "'" +
                ", time=" + time +
                ", open=" + open +
                ", close=" + close +
                ", high=" + high +
                ", low=" + low +
                ", preClose=" + preClose +
                ", exchangePercent=" + exchangePercent +
                ", volume=" + volume +
                ", exchangeMoney=" + exchangeMoney +
                '}';
    }
}
