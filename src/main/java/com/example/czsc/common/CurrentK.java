package com.example.czsc.common;

/**
 * @FileName: CurrentK
 * @Author: Haifeng Tong
 * @Date: 2021/4/96:38 下午
 * @Description:
 * @History:
 */
public class CurrentK extends Kline{
    private float buyPrice1;//11
    private float buyPrice2;//13
    private float buyPrice3;//15
    private float buyPrice4;//17
    private float buyPrice5;//19
    private float sellPrice1;//21
    private float sellPrice2;//23
    private float sellPrice3;//25
    private float sellPrice4;//27
    private float sellPrice5;//29
    private int buyAmount1;//10
    private int buyAmount2;//12
    private int buyAmount3;//14
    private int buyAmount4;//16
    private int buyAmount5;//28
    private int sellAmount1;//20
    private int sellAmount2;//22
    private int sellAmount3;//24
    private int sellAmount4;//26
    private int sellAmount5;//28
    private float upLimit;
    private float downLimit;

    public float getBuyPrice1() {
        return buyPrice1;
    }

    public void setBuyPrice1(float buyPrice1) {
        this.buyPrice1 = buyPrice1;
    }

    public float getBuyPrice2() {
        return buyPrice2;
    }

    public void setBuyPrice2(float buyPrice2) {
        this.buyPrice2 = buyPrice2;
    }

    public float getBuyPrice3() {
        return buyPrice3;
    }

    public void setBuyPrice3(float buyPrice3) {
        this.buyPrice3 = buyPrice3;
    }

    public float getBuyPrice4() {
        return buyPrice4;
    }

    public void setBuyPrice4(float buyPrice4) {
        this.buyPrice4 = buyPrice4;
    }

    public float getBuyPrice5() {
        return buyPrice5;
    }

    public void setBuyPrice5(float buyPrice5) {
        this.buyPrice5 = buyPrice5;
    }

    public float getSellPrice1() {
        return sellPrice1;
    }

    public void setSellPrice1(float sellPrice1) {
        this.sellPrice1 = sellPrice1;
    }

    public float getSellPrice2() {
        return sellPrice2;
    }

    public void setSellPrice2(float sellPrice2) {
        this.sellPrice2 = sellPrice2;
    }

    public float getSellPrice3() {
        return sellPrice3;
    }

    public void setSellPrice3(float sellPrice3) {
        this.sellPrice3 = sellPrice3;
    }

    public float getSellPrice4() {
        return sellPrice4;
    }

    public void setSellPrice4(float sellPrice4) {
        this.sellPrice4 = sellPrice4;
    }

    public float getSellPrice5() {
        return sellPrice5;
    }

    public void setSellPrice5(float sellPrice5) {
        this.sellPrice5 = sellPrice5;
    }

    public int getBuyAmount1() {
        return buyAmount1;
    }

    public void setBuyAmount1(int buyAmount1) {
        this.buyAmount1 = buyAmount1;
    }

    public int getBuyAmount2() {
        return buyAmount2;
    }

    public void setBuyAmount2(int buyAmount2) {
        this.buyAmount2 = buyAmount2;
    }

    public int getBuyAmount3() {
        return buyAmount3;
    }

    public void setBuyAmount3(int buyAmount3) {
        this.buyAmount3 = buyAmount3;
    }

    public int getBuyAmount4() {
        return buyAmount4;
    }

    public void setBuyAmount4(int buyAmount4) {
        this.buyAmount4 = buyAmount4;
    }

    public int getBuyAmount5() {
        return buyAmount5;
    }

    public void setBuyAmount5(int buyAmount5) {
        this.buyAmount5 = buyAmount5;
    }

    public int getSellAmount1() {
        return sellAmount1;
    }

    public void setSellAmount1(int sellAmount1) {
        this.sellAmount1 = sellAmount1;
    }

    public int getSellAmount2() {
        return sellAmount2;
    }

    public void setSellAmount2(int sellAmount2) {
        this.sellAmount2 = sellAmount2;
    }

    public int getSellAmount3() {
        return sellAmount3;
    }

    public void setSellAmount3(int sellAmount3) {
        this.sellAmount3 = sellAmount3;
    }

    public int getSellAmount4() {
        return sellAmount4;
    }

    public void setSellAmount4(int sellAmount4) {
        this.sellAmount4 = sellAmount4;
    }

    public int getSellAmount5() {
        return sellAmount5;
    }

    public void setSellAmount5(int sellAmount5) {
        this.sellAmount5 = sellAmount5;
    }

    public float getUpLimit() {
        return upLimit;
    }

    public void setUpLimit(float upLimit) {
        this.upLimit = upLimit;
    }

    public float getDownLimit() {
        return downLimit;
    }

    public void setDownLimit(float downLimit) {
        this.downLimit = downLimit;
    }

    @Override
    public String toString() {
        return  super.toString() +
                "\n" +
                "CurrentK{" +"\n" +
                "buyPrice1=" + buyPrice1 +"\n" +
                ", buyPrice2=" + buyPrice2 +"\n" +
                ", buyPrice3=" + buyPrice3 +"\n" +
                ", buyPrice4=" + buyPrice4 +"\n" +
                ", buyPrice5=" + buyPrice5 +"\n" +
                ", sellPrice1=" + sellPrice1 +"\n" +
                ", sellPrice2=" + sellPrice2 +"\n" +
                ", sellPrice3=" + sellPrice3 +"\n" +
                ", sellPrice4=" + sellPrice4 +"\n" +
                ", sellPrice5=" + sellPrice5 +"\n" +
                ", buyAmount1=" + buyAmount1 +"\n" +
                ", buyAmount2=" + buyAmount2 +"\n" +
                ", buyAmount3=" + buyAmount3 +"\n" +
                ", buyAmount4=" + buyAmount4 +"\n" +
                ", buyAmount5=" + buyAmount5 +"\n" +
                ", sellAmount1=" + sellAmount1 +"\n" +
                ", sellAmount2=" + sellAmount2 +"\n" +
                ", sellAmount3=" + sellAmount3 +"\n" +
                ", sellAmount4=" + sellAmount4 +"\n" +
                ", sellAmount5=" + sellAmount5 +"\n" +
                ", upLimit=" + upLimit +"\n" +
                ", downLimit=" + downLimit +"\n" +
                "} ";
    }
}
