package com.example.demo.repository;

import com.example.demo.pojo.MinuteKline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @FileName: MinuteKlinesRepository
 * @Author: Haifeng Tong
 * @Date: 2021/5/157:02 下午
 * @Description:
 * @History: 2021/5/15
 */
@Repository
public interface MinuteKlineRepository extends JpaRepository<MinuteKline,Integer>, JpaSpecificationExecutor<MinuteKline> {

    public MinuteKline findBySymbolAndMinute(String symbol,Date minute);

    @Query("from MinuteKline as m where m.symbol=:symbol and m.minute >= :timeStart and m.minute <= :timeEnd")
    public List<MinuteKline> findBySymbolAndTime(String symbol, Date timeStart, Date timeEnd);

    public List<MinuteKline> findBySymbolOrderByMinuteAsc(String symbol);

    public void deleteBySymbol(String symbol);
    @Modifying
    @Query("update MinuteKline set open = :open,close = :close,high = :high,low = :low " +
            ",volume = :volume,exchangeMoney = :exchangeMoney where symbol = :symbol and minute = :minute")
    @Transactional
    int update(String symbol,Date minute,float open,float close,float high,float low,float volume,float exchangeMoney);

    @Query("select distinct symbol from MinuteKline")
    List<String> findAllSymbols();
/*
    @Query("from MinuteKline as m where m.symbol = :symbol " +
            "and m.minute = (select max(m.minute) from MinuteKline)")
    List<MinuteKline> findLatestBySymbol(String symbol);*/
}
