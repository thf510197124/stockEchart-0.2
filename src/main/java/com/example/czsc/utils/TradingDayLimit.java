package com.example.czsc.utils;


import com.example.czsc.common.Kline;
import com.example.czsc.common.Klines;
import com.example.czsc.dataSystem.simpleData.DataFromWangYiCSV;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.czsc.czscAnalyze.czscDefine.CzscUtils.getLast;
import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util.println;


/**
 * 不考虑日线以下的涨跌限制
 * 涨停计算可能与同花顺之类的不同，在涨停中，如果以涨停价收盘，但是卖1有单子，同花顺不计入涨停。跌停也类似，这个没有办法通过计算收盘价来计算
 * @FileName: TradingLimit
 * @Author: Haifeng Tong
 * @Date: 2021/4/28:27 上午
 * @Description:
 * @History:
 */
public class TradingDayLimit {
    private static LocalDateTime divideLine = LocalDateTime.of(2020,8,23,15,0,1);
    //根据之前的K线，交易规则，判断当天的股价是否涨停，最低以天为单位
    public static boolean todayUpTradingLimit(Klines klines){
        Kline kline = getLast(klines);
        Klines news = Klines.copyKlines(klines);
        news.remove(kline);//之前的和今天的
        return upTradingLimit(news,kline);
    }
    public static boolean todayDownTradingLimit(Klines klines){
        Kline kline = getLast(klines);
        Klines news = Klines.copyKlines(klines);
        news.remove(kline);//之前的和今天的
        return downTradingLimit(news,kline);
    }
    public static boolean upTradingLimit(Klines klines,LocalDateTime date){
        Kline kline = klines.getBarByDate(date);//之前最近的一天的
        if (kline == null){//一直停牌到现在的股票
            return false;
        }
        Klines subKines = Klines.subKLinesBeforeTime(klines,date);
        subKines.remove(kline);
        return upTradingLimit(subKines,kline);
    }
    public static boolean downTradingLimit(Klines klines,LocalDateTime date){
        Kline kline = klines.getBarByDate(date);
        if(kline == null){//一直停牌到现在的股票
            return false;
        }
        Klines subKines = Klines.subKLinesBeforeTime(klines,date);
        subKines.remove(kline);//删除当天
        return downTradingLimit(subKines,kline);
    }
    
    //klines中不包含kline，
    public static boolean upTradingLimit(Klines klines, Kline kline){
        if (kline.getSymbol().startsWith("68") &&(kline.getName().contains("N") || kline.getName().contains("C"))){
            return false;
        }
        if (kline.getSymbol().startsWith("3") &&
                (kline.getName().contains("N") || kline.getName().contains("C"))
                && kline.getTime().isAfter(divideLine))
        {
            return false;
        }
        //处理st股
        if (klines.getName().contains("ST")){
            BigDecimal closeBig = BigDecimal.valueOf(kline.getClose()).setScale(2,BigDecimal.ROUND_HALF_UP);
            BigDecimal downLimit = stUpTradingLimit(klines,kline);
            return closeBig.compareTo(downLimit) == 0;
        }
        BigDecimal closeBig = BigDecimal.valueOf(kline.getClose()).setScale(2,BigDecimal.ROUND_HALF_UP);
        println("close = " + closeBig.toPlainString());
        BigDecimal upLimit;
        if (kline.getSymbol().startsWith("688")){
            upLimit = keChangUpTradingLimit(klines,kline);
            println("upLimit = " + upLimit.toPlainString());
            return closeBig.compareTo(upLimit) == 0;
        }else if (kline.getSymbol().startsWith("3")){
            upLimit = changYeUpTradingLimit(klines,kline);
            println("upLimit = " + upLimit.toPlainString());
            return closeBig.compareTo(upLimit) == 0;
        }else{
            upLimit = zhuBanUpTradingLimit(klines,kline);
            println("upLimit = " + upLimit.toPlainString());
            return closeBig.compareTo(upLimit) == 0;
        }

    }
    public static boolean downTradingLimit(Klines klines,Kline kline){
        //如果是创业板或科创板，没有涨跌幅的限制
        if (kline.getSymbol().startsWith("688") &&(kline.getName().contains("N") || kline.getName().contains("C"))){
            return false;
        } else if (kline.getSymbol().startsWith("3") &&
                (kline.getName().contains("N") || kline.getName().contains("C"))
                && kline.getTime().isAfter(divideLine))
        {
            return false;
        }
        //处理st股
        if (klines.getName().contains("ST")){
            BigDecimal closeBig = BigDecimal.valueOf(kline.getClose()).setScale(2,BigDecimal.ROUND_HALF_UP);
            BigDecimal downLimit = stDownTradingLimit(klines,kline);
            return closeBig.compareTo(downLimit) == 0;
        }
        BigDecimal closeBig = BigDecimal.valueOf(kline.getClose()).setScale(2,BigDecimal.ROUND_HALF_UP);
        println("close = " + closeBig.toPlainString());
        BigDecimal downLimit;
        if (kline.getSymbol().startsWith("688")){
            downLimit = keChangDownTradingLimit(klines,kline);
            println("downLimit = " + downLimit.toPlainString());
            return closeBig.compareTo(downLimit) == 0;
        }else if (kline.getSymbol().startsWith("3")){
            downLimit = changeYeDownTradingLimit(klines,kline);
            println("downLimit = " + downLimit.toPlainString());
            return closeBig.compareTo(downLimit) == 0;
        }else{
            downLimit = zhuBanDownTradingLimit(klines,kline);
            println("downLimit = " + downLimit.toPlainString());
            return closeBig.compareTo(downLimit) == 0;
        }
    }

    private static BigDecimal stDownTradingLimit(Klines klines, Kline kline) {
        BigDecimal bigDecimal = BigDecimal.valueOf(getLast(klines).getClose() * 0.95f);
        return bigDecimal.setScale(2, RoundingMode.HALF_UP);
    }

    //st股涨跌停
    private static BigDecimal stUpTradingLimit(Klines klines,Kline kline){
        BigDecimal bigDecimal = BigDecimal.valueOf(getLast(klines).getClose() * 1.05f);
        return bigDecimal.setScale(2, RoundingMode.HALF_UP);
    }
    //科创板
    private static BigDecimal keChangUpTradingLimit(Klines klines, Kline kline) {
        BigDecimal bigDecimal = BigDecimal.valueOf(getLast(klines).getClose() * 1.2f);
        return bigDecimal.setScale(2, RoundingMode.HALF_UP);
    }
    private static BigDecimal keChangDownTradingLimit(Klines klines,Kline kline){
        BigDecimal bigDecimal = BigDecimal.valueOf(getLast(klines).getClose() * 0.8f);
        return bigDecimal.setScale(2, RoundingMode.HALF_UP);
    }
    //创业板
    //2020年8月24日开始注册制的涨跌停交易规则
    private static BigDecimal changYeUpTradingLimit(Klines klines,Kline kline) {
        if (kline.getTime().isBefore(divideLine)){//8月24日之前，采用主板的交易规则
            return zhuBanUpTradingLimit(klines,kline);
        }else{
            return keChangUpTradingLimit(klines,kline);//如果8月24日之后，采用科创板的交易规则
        }
    }
    private static BigDecimal changeYeDownTradingLimit(Klines klines,Kline kline){
        LocalDateTime divideLine = LocalDateTime.of(2020,8,23,15,0,1);
        if (kline.getTime().isBefore(divideLine)){//8月24日之前，采用主板的交易规则
            return zhuBanDownTradingLimit(klines,kline);
        }else{
            return keChangDownTradingLimit(klines,kline);//如果8月24日之后，采用科创板的交易规则
        }
    }
    //主板交易上涨交易规则
    private static BigDecimal zhuBanUpTradingLimit(Klines klines,Kline kline){
        BigDecimal bigDecimal;
        if (kline.getName().contains("N")){//如果昨日是新股
            bigDecimal = BigDecimal.valueOf(getLast(klines).getClose() * 1.44f);
        }else{
            bigDecimal = BigDecimal.valueOf(getLast(klines).getClose() * 1.1f);
        }
        return bigDecimal.setScale(2, RoundingMode.HALF_UP);
    }

    private static BigDecimal zhuBanDownTradingLimit(Klines klines,Kline kline){
        BigDecimal bigDecimal;
        if (kline.getName().contains("N")){//如果昨日是新股
            bigDecimal = BigDecimal.valueOf(getLast(klines).getClose() * .64f);
        }else{
            bigDecimal = BigDecimal.valueOf(getLast(klines).getClose() * 0.9f);
        }
        return bigDecimal.setScale(2, RoundingMode.HALF_UP);
    }

    public static void main(String[] args) throws IOException {
        String path = "/Users/haifeng/Documents/stock_database/trading163";
        File dirFile = new File(path);
        List<String> upSymbols = new ArrayList<>();
        List<String> downSymbols = new ArrayList<>();
        File[] files = dirFile.listFiles();
        for (File file : files){
            if (file.getName().contains(".csv")){
                Klines klines = DataFromWangYiCSV.read(file.getAbsolutePath(), LocalDateTime.of(2021, 1, 1, 0, 0, 0));
                if (klines == null){
                    continue;
                }
                println("处理股票：    " + klines.getName());
                //测试今天涨停
                /*if (todayUpTradingLimit(klines)){
                    upSymbols.add(klines.getName());
                }else if(todayDownTradingLimit(klines)){
                    downSymbols.add(klines.getName());
                }*/
                //昨日涨停
                LocalDateTime date = LocalDateTime.of(2021, 4, 1,15,10,0);
                if (upTradingLimit(klines,date)){
                    upSymbols.add(klines.getName());
                }else if(downTradingLimit(klines,date)){
                    downSymbols.add(klines.getName());
                }
            }
        }
        println("涨停数为：" + upSymbols.size());
        println("#####################################################################################################################");
        println("涨停股票");
        upSymbols.forEach(Utils::println);
        println("#####################################################################################################################");
        println("跌停数为：" + downSymbols.size());
        println("跌停股票");
        downSymbols.forEach(Utils::println);
    }
}
