package com.example.simulateExchange;

import com.example.demo.pojo.Exchange;
import com.example.demo.utils.DateUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @FileName: Account
 * @Author: Haifeng Tong
 * @Date: 2021/5/266:29 下午
 * @Description:
 * @History: 2021/5/26
 */
public class Account {
    private final List<Positions> positionsList;
    private static Account account;
    private Account(){
        this.positionsList = new ArrayList<>();
    }
    public static Account getAccount(){
        if (account == null){
            return new Account();
        }else{
            return account;
        }
    }
    public void addExchange(Exchange exchange){
        List<Positions> unEnd = holds();
        for (Positions pos : unEnd){
            if (pos.getSymbol().equals(exchange.getSymbol())){
                pos.addExchange(exchange);
                return;
            }
        }
        Positions positions = new Positions(exchange);
        positionsList.add(positions);
    }
    public long getRepository(String symbol){
        for (Positions pos : positionsList){
            if (!pos.isClosed() && pos.getSymbol().equals(symbol)){
                return pos.getAmount();
            }
        }
        return 0;
    }
    public float profit(){
        float profit = 0;
        for (Positions pos : positionsList){
            if (pos.isClosed()){
                profit+= pos.profit();
            }
        }
        return profit;
    }
    public float profit(Date begin, Date end){
        float profit = 0;
        LocalDateTime begin1 = DateUtils.asLocalDateTime(begin);
        LocalDateTime end1 = DateUtils.asLocalDateTime(end);
        for (Positions pos : positionsList){
            if (pos.isClosed() &&
                    pos.beginDate().isAfter(begin1.plusSeconds(1))
                    && pos.endDate().isBefore(end1.minusSeconds(1))){
                profit+= pos.profit();
            }
        }
        return profit;
    }
    public List<Positions> getPositionsList() {
        return positionsList;
    }

    public List<Positions> holds(){
        List<Positions> ps = new ArrayList<>();
        for(Positions position : positionsList){
            if(!position.isClosed()){
                ps.add(position);
            }
        }
        return ps;
    }

}
