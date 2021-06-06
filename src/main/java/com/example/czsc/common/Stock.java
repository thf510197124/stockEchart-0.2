package com.example.czsc.common;

/**
 * @FileName: Stock
 * @Author: Haifeng Tong
 * @Date: 2021/3/38:51 上午
 * @Description:
 * @History:
 */
public class Stock {
    private String symbol;
    private String name;
    private float totalCapital;
    private float flowCapital;

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

    public float getTotalCapital() {
        return totalCapital;
    }

    public void setTotalCapital(float totalCapital) {
        this.totalCapital = totalCapital;
    }

    public float getFlowCapital() {
        return flowCapital;
    }

    public void setFlowCapital(float flowCapital) {
        this.flowCapital = flowCapital;
    }
}
