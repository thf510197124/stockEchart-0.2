package com.example.demo.controller;

import com.example.demo.pojo.Exchange;
import com.example.demo.pojo.ExchangeType;
import com.example.demo.service.ExchangeService;
import com.example.demo.utils.DateUtils;
import com.example.simulateExchange.Account;
import com.example.simulateExchange.Positions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @FileName: ReviewController
 * @Author: Haifeng Tong
 * @Date: 2021/5/269:36 下午
 * @Description:
 * @History: 2021/5/26
 */
@Controller
public class ReviewController {
    ExchangeService exchangeService;
    private static final float FEE = -0.0005f;
    private static final float FEE_TAX = -0.001f;
    Account account = null;
    @Autowired
    public void setExchangeService(ExchangeService exchangeService) {
        this.exchangeService = exchangeService;
    }
    public void buy(String symbol, Date time, float price, long amount, String reason){
        Exchange exchange = new Exchange();
        exchange.setSymbol(symbol);
        exchange.setExchangeTime(time);
        exchange.setPrice(price);
        exchange.setAmount(amount);
        exchange.setReason(reason);
        exchange.setFee(getFee(symbol,amount,price));
        exchange.setType(ExchangeType.BUY);
        //买入时，属于支出，所以money为负
        float money = (-1) * exchange.getAmount() * exchange.getPrice() + exchange.getFee();
        exchange.setExchangeMoney(money);
        if(account == null){
            init();
        }
        exchange.setRepertory(account.getRepository(symbol) + amount);
        exchangeService.save(exchange);
        account.addExchange(exchange);
    }

    public ModelAndView holdPosition(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/review");
        if(account == null){
            init();
        }
        List<Positions> positionsList = account.holds();
        for (Positions pos : positionsList){
            mav.addObject(pos.getSymbol(), new Positions.PositionsBean(pos));
        }
        return mav;
    }
    public void sell(String symbol, Date time,float price,long amount,String reason){
        Exchange exchange = new Exchange();
        exchange.setSymbol(symbol);
        exchange.setExchangeTime(time);
        exchange.setPrice(price);
        exchange.setAmount(amount * (-1));
        exchange.setReason(reason);
        exchange.setFee(getFee(symbol,amount,price));
        exchange.setFeeTax(getFeeTax(amount,price));
        exchange.setType(ExchangeType.SELL);
        float money = (-1) * exchange.getAmount() * exchange.getPrice()
                + exchange.getFee() + exchange.getFeeTax();
        exchange.setExchangeMoney(money);
        if(account == null){
            init();
        }
        exchange.setRepertory(account.getRepository(symbol) + amount);
        exchangeService.save(exchange);
        account.addExchange(exchange);
    }
    private float getFee(String symbol,long amount,float price) {
        float fee = amount * price * FEE;
        if(symbol.startsWith("6")){
            if(fee < 6){
                return 6;
            }else{
                return fee;
            }
        }else{
            if(fee < 5){
                return 5;
            }else{
                return fee;
            }
        }
    }
    private float getFeeTax(long amount,float price){
        return amount * price * FEE_TAX;
    }
    public void init(){
        List<Exchange> exchangeList = exchangeService.findAll();
        account = Account.getAccount();
        for (Exchange exchange : exchangeList){
            account.addExchange(exchange);
        }
    }
}
