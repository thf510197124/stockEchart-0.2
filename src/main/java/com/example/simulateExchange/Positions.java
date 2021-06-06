package com.example.simulateExchange;

import com.example.demo.pojo.Exchange;
import com.example.demo.pojo.ExchangeType;
import com.example.demo.utils.DateUtils;
import com.sun.istack.Nullable;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.czsc.czscAnalyze.czscDefine.CzscUtils.getLast;

/**
 * @FileName: Positions
 * @Author: Haifeng Tong
 * @Date: 2021/5/266:29 下午
 * @Description:
 * @History: 2021/5/26
 */
public class Positions {
    private PositionType type;
    private final List<Exchange> exchanges;
    private final String symbol;

    public Positions(Exchange exchange){
        this.exchanges = new ArrayList<>();
        this.exchanges.add(exchange);
        this.type = PositionType.BUY_POSITION;
        this.symbol = exchange.getSymbol();
    }
    public void addExchange(Exchange exchange){
        if (!canAdd(exchange)){
            throw new RuntimeException("该仓位已经结束");
        }else{
            exchanges.add(exchange);
            if(getAmount() == 0){
                type = PositionType.POSITION_CLEAR;
            }
        }
    }
    private boolean canAdd(Exchange exchange){
        return exchange.getSymbol().equals(symbol) && !isClosed();
    }
    public LocalDateTime beginDate(){
        return DateUtils.asLocalDateTime(exchanges.get(0).getExchangeTime());
    }
    @Nullable
    public LocalDateTime endDate(){
        if(isClosed()){
            return DateUtils.asLocalDateTime(getLast(exchanges).getExchangeTime());
        }else{
            return null;
        }
    }
    public long holdsDays(){
        LocalDateTime begin = beginDate();
        LocalDateTime end;
        if(isClosed()){
            end = endDate();
        }else{
            end = LocalDateTime.now();
        }
        return Duration.between(begin, end).toDays();
    }
    public boolean isClosed(){
        return exchanges.size() > 1 && getAmount() == 0 && type == PositionType.POSITION_CLEAR;
    }
    public long getAmount(){
        long amount = 0;
        for (Exchange exchange : exchanges){
            amount += exchange.getAmount();
        }
        return amount;
    }
    public float getHoldMoney(){
        if (type == PositionType.BUY_POSITION){
            float holdMoney = 0f;
            for (Exchange exchange : exchanges){
                holdMoney +=  exchange.getExchangeMoney();
            }
            return holdMoney * -1;
        }else{
            return 0;
        }
    }
    public float profit(){
        if (type == PositionType.POSITION_CLEAR){
            float profit = 0f;
            for (Exchange exchange : exchanges){
                profit += exchange.getExchangeMoney();
            }
            return profit;
        }else{
            return Float.NaN;
        }
    }
    public float profit(float currentPrice){
        long amount = getAmount();
        float profit = 0f;
        for (Exchange exchange : exchanges){
            profit += exchange.getExchangeMoney();
        }
        return profit + amount * currentPrice;
    }


    public PositionType getType() {
        return type;
    }

    public void setType(PositionType type) {
        this.type = type;
    }

    public List<Exchange> getExchanges() {
        return exchanges;
    }


    public String getSymbol() {
        return symbol;
    }
    public static class PositionsBean{
        private String symbol;
        private float amount;
        private float avgCost;
        private LocalDateTime begin;
        private int holdDays;
        private float profit;
        public PositionsBean() {
        }
        public PositionsBean(Positions positions){
            this.symbol = positions.getSymbol();
            this.amount = positions.getAmount();
            this.avgCost = positions.getHoldMoney() / positions.getAmount();
            this.begin = positions.beginDate();
            this.holdDays = (int)positions.holdsDays();
            if (positions.getType() == PositionType.POSITION_CLEAR){
                this.profit = positions.profit();
            }else{
                this.profit = 0;
            }
        }
        public PositionsBean(String symbol, float amount, float avgCost,
                             LocalDateTime begin, int holdDays,float profit) {
            this.symbol = symbol;
            this.amount = amount;
            this.avgCost = avgCost;
            this.begin = begin;
            this.holdDays = holdDays;
            this.profit = profit;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public float getAmount() {
            return amount;
        }

        public void setAmount(float amount) {
            this.amount = amount;
        }

        public float getAvgCost() {
            return avgCost;
        }

        public void setAvgCost(float avgCost) {
            this.avgCost = avgCost;
        }

        public LocalDateTime getBegin() {
            return begin;
        }

        public void setBegin(LocalDateTime begin) {
            this.begin = begin;
        }

        public int getHoldDays() {
            return holdDays;
        }

        public void setHoldDays(int holdDays) {
            this.holdDays = holdDays;
        }

        public float getProfit() {
            return profit;
        }

        public void setProfit(float profit) {
            this.profit = profit;
        }
    }
}
