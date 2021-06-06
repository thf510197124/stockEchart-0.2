package com.example.demo.service;

import com.example.czsc.common.Kline;
import com.example.czsc.common.Klines;
import com.example.demo.pojo.MinuteKline;
import com.example.demo.repository.MinuteKlineRepository;
import com.example.demo.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static com.example.czsc.czscAnalyze.czscDefine.CzscUtils.getLast;

/**
 * @FileName: MinuteKlinesService
 * @Author: Haifeng Tong
 * @Date: 2021/5/1510:05 下午
 * @Description:
 * @History: 2021/5/15
 */

@Service
public class MinuteKlinesService {

    MinuteKlineRepository mRepository;
    @Autowired
    public void setMinuteKlines(MinuteKlineRepository minuteKlinesRepository) {
        this.mRepository = minuteKlinesRepository;
    }
    //超过1000条是，前500条，基本上每天检查一次就可以了
    public void checkNumber(String symbol){
        List<MinuteKline> ms = mRepository.findBySymbolOrderByMinuteAsc(symbol);
        if (ms.size() > 5000){
            int needDelete = ms.size() - 4500;
            for (int i = 0;i<needDelete;i++){
                mRepository.delete(ms.get(i));
            }
        }
    }
    //10天没有查看，就删除
    public void checkUnUsed(){
        LocalDateTime now = LocalDateTime.now();
        List<String> symbols = mRepository.findAllSymbols();
        for (String s : symbols){
            MinuteKline m = getLastKline(s);
            if (m != null && m.getTime().isBefore(now.minusDays(10))){
                mRepository.deleteBySymbol(s);
            }
        }
    }
    public Kline update(Kline k){
        int index =  mRepository.update(k.getSymbol(),DateUtils.asDate(k.getTime()),
                k.getOpen(),k.getClose(),k.getHigh(),k.getLow(),k.getVolume(),k.getExchangeMoney());
        return mRepository.getOne(index);
    }
    public Kline updateOrAddKline(Kline kline){
        kline.setTime(kline.getTime().withSecond(0));
        MinuteKline m = getLastKline(kline.getSymbol());
        if (m != null && m.getTime().equals(kline.getTime())){
            return update(kline);
        }else{
            return mRepository.save((MinuteKline)kline);
        }
    }
    public MinuteKline getLastKline(String symbol){
        if (mRepository.findBySymbolOrderByMinuteAsc(symbol).size() > 0){
            //System.out.println("查询时间最大的为数量为：" + mRepository.findLatestBySymbol(symbol).size());
            return getLast(mRepository.findBySymbolOrderByMinuteAsc(symbol));
        }else{
            return null;
        }
    }
    public void saveKlines(Klines klines){
        MinuteKline m = getLastKline(klines.get(0).getSymbol());
        if (m != null) {
            Collections.sort(klines);
            String symbol = klines.get(0).getSymbol();
            klines = Klines.subKLinesAfterAndEqualTime(klines,m.getTime());
            if(klines != null) {
                if (m.getTime().equals(klines.get(0).getTime())) {
                    Kline k = klines.get(0);
                    update(k);
                    klines.remove(0);
                }
                for (Kline kline : klines) {
                    mRepository.save(MinuteKline.kline(kline));
                }
            }
            checkNumber(symbol);
        }else{
            for (Kline kline : klines) {
                mRepository.save(MinuteKline.kline(kline));
                System.out.println(MinuteKline.kline(kline).getMinute());
            }
        }
    }
    public List<MinuteKline> findBySymbol(String symbol){
        return mRepository.findBySymbolOrderByMinuteAsc(symbol);
    }
    public List<MinuteKline> findBySymbolAndTime(String symbol,LocalDateTime timeStart,LocalDateTime timeEnd){
        return mRepository.findBySymbolAndTime(symbol,DateUtils.asDate(timeStart),DateUtils.asDate(timeEnd));
    }

}
