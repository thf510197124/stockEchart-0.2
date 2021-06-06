package com.example.simulateExchange;

import java.time.LocalDateTime;

/**
 * @FileName: Position
 * @Author: Haifeng Tong
 * @Date: 2021/5/213:15 下午
 * @Description:
 * @History: 2021/5/21
 */
public class Position {
    private PositionType position;
    private int count;//数量
    private float price;
    private LocalDateTime buildTime;
    private float profit;
    private float money = 100000f;
    public Position(PositionType position, int count, float price, LocalDateTime time) {
        this.position = position;
        this.count = count;
        this.price = price;
        this.buildTime = time;
    }
    public Position(PositionType position,float price,LocalDateTime time){
        this.position = position;
        this.count = 100* (int)Math.ceil(money / (100*price));
        this.money = 100000;
        this.price = price;
        this.buildTime = time;
    }
    public void addPosition(Position position){
        this.position = position.getPosition();
        this.count = 100* (int)Math.ceil(getMoney() / (100 * position.getPrice()));
        System.out.println("当前的money有" + getMoney());
        this.price = position.getPrice();
        this.buildTime = position.getBuildTime();
    }


    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public PositionType getPosition() {
        return position;
    }

    public void setPosition(PositionType position) {
        this.position = position;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public LocalDateTime getBuildTime() {
        return buildTime;
    }

    public void setBuildTime(LocalDateTime buildTime) {
        this.buildTime = buildTime;
    }

    public float getProfit() {
        return profit;
    }

    public void setProfit(float profit) {
        this.profit = profit;
    }

    public float clear(){
        if (getPosition() == PositionType.BUY_POSITION){
            setPosition(PositionType.POSITION_CLEAR);
            setCount(0);
        }else if (getPosition() == PositionType.SELL_POSITION){
            setPosition(PositionType.POSITION_CLEAR);
            setCount(0);
        }
        return getProfit();
    }
    public float clear(float price){
        if (getPosition() == PositionType.BUY_POSITION){
            setPosition(PositionType.POSITION_CLEAR);
            setProfit((price - getPrice()) * getCount());
            setMoney(getMoney() +  (price - getPrice()) * getCount());
            setCount(0);
        }else if (getPosition() == PositionType.SELL_POSITION){
            setPosition(PositionType.POSITION_CLEAR);
            setProfit((getPrice() - price) * getCount());
            setMoney(getMoney()  +  (getPrice() - price) * getCount());
            setCount(0);
        }
        return getProfit();
    }
    public float getProfit(float price){
        if (getPosition() == PositionType.BUY_POSITION){
            setProfit((price - getPrice()) * getCount());
            return getProfit();
        }else if (getPosition() == PositionType.SELL_POSITION){
            setProfit((getPrice() - price)* getCount());
            return getProfit();
        }else{
            return 0;
        }
    }
    public float percent(float price){
        if (getPosition() == PositionType.BUY_POSITION){
            return (price - getPrice()) / (getPrice());
        }else if (getPosition() == PositionType.SELL_POSITION){
            return (getPrice() - price + getProfit()) * getCount() / getPrice();
        }else{
            return 0;
        }
    }

}
