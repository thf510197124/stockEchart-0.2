package com.example.demo.service;

import com.example.demo.pojo.Exchange;
import com.example.demo.pojo.ExchangeType;
import com.example.demo.repository.ExchangeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @FileName: ExchangeService
 * @Author: Haifeng Tong
 * @Date: 2021/5/266:11 下午
 * @Description:
 * @History: 2021/5/26
 */
@Service
public class ExchangeService {
    @Autowired
    ExchangeRepository exchangeRepository;

    public void save(Exchange exchange){
        exchangeRepository.save(exchange);
    }
    public void update(int id,Exchange exchange){
        exchange.setId(id);
        exchangeRepository.save(exchange);
    }
    public Exchange get(int id){
        return exchangeRepository.getOne(id);
    }
    public List<Exchange> findBySymbol(String symbol){
        return exchangeRepository.findBySymbol(symbol);
    }
    public List<Exchange> findByReasonLike(String reason){
        return exchangeRepository.findByReasonLike(reason);
    }
    public List<Exchange> findByType(ExchangeType type){
        return exchangeRepository.findByType(type);
    }
    public List<Exchange> findByExchangeTimeIsBetween(Date begin, Date end){
        return exchangeRepository.findByExchangeTimeIsBetween(begin,end);
    }
    public List<Exchange> findAll(){
        return exchangeRepository.findAll();
    }
    public Exchange findBySymbolAndExchangeTime(String symbol,Date time){
        return exchangeRepository.findBySymbolAndExchangeTime(symbol,time);
    }
    public void saveAll(List<Exchange> exchanges){
        exchangeRepository.saveAll(exchanges);
    }
}
