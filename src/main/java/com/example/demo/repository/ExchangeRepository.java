package com.example.demo.repository;

import com.example.demo.pojo.Exchange;
import com.example.demo.pojo.ExchangeType;
import com.example.demo.pojo.MinuteKline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @FileName: ExchangeRepository
 * @Author: Haifeng Tong
 * @Date: 2021/5/266:11 下午
 * @Description:
 * @History: 2021/5/26
 */

@Repository
public interface ExchangeRepository extends JpaRepository<Exchange,Integer>, JpaSpecificationExecutor<Exchange> {
    public List<Exchange> findBySymbol(String symbol);
    public List<Exchange> findByReasonLike(String reason);
    public List<Exchange> findByType(ExchangeType type);
    public List<Exchange> findByExchangeTimeIsBetween(Date begin, Date end);
    public List<Exchange> findAll();
    public Exchange findBySymbolAndExchangeTime(String symbol,Date time);
}
