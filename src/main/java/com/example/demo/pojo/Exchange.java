package com.example.demo.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;

/**
 * @FileName: Exchange
 * @Author: Haifeng Tong
 * @Date: 2021/5/263:49 下午
 * @Description:
 * @History: 2021/5/26
 */

@Entity
@Table(name = "exchange")
@Data
public class Exchange {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    @Temporal(TemporalType.TIMESTAMP)
    private Date exchangeTime;
    private String symbol;
    @Enumerated(EnumType.STRING)
    private ExchangeType type;
    private long amount;
    private float fee;
    private float feeTax;
    private float price;
    private long repertory;//库存
    private String reason;
    private float exchangeMoney;//发生金额，包含手续费等
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getExchangeTime() {
        return exchangeTime;
    }

    public void setExchangeTime(Date exchangeTime) {
        this.exchangeTime = exchangeTime;
    }

    public float getExchangeMoney() {
        return exchangeMoney;
    }

    public void setExchangeMoney(float exchangeMoney) {
        this.exchangeMoney = exchangeMoney;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public ExchangeType getType() {
        return type;
    }

    public void setType(ExchangeType type) {
        this.type = type;
    }


    public float getFee() {
        return fee;
    }

    public void setFee(float fee) {
        this.fee = fee;
    }

    public float getFeeTax() {
        return feeTax;
    }

    public void setFeeTax(float feeTax) {
        this.feeTax = feeTax;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public long getRepertory() {
        return repertory;
    }

    public void setRepertory(long repertory) {
        this.repertory = repertory;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "Exchange{" +
                "id=" + id +
                ", exchangeTime=" + exchangeTime +
                ", symbol='" + symbol + '\'' +
                ", type=" + type +
                ", amount=" + amount +
                ", fee=" + fee +
                ", feeTax=" + feeTax +
                ", price=" + price +
                ", repertory=" + repertory +
                ", reason='" + reason + '\'' +
                ", exchangeMoney=" + exchangeMoney +
                '}';
    }
}
