package com.example.czsc.czscAnalyze.chooseStock;

import com.example.czsc.common.Interval;
import com.example.czsc.common.Kline;
import com.example.czsc.common.Klines;
import com.example.czsc.czscAnalyze.czscDefine.Analyze;
import com.example.czsc.czscAnalyze.czscDefine.TaUtils;
import com.example.czsc.czscAnalyze.czscDefine.toolsForXD.IntervalRelationship;
import com.example.czsc.czscAnalyze.objects.*;

import com.example.czsc.dataSystem.UnifiedDataFromBaoData;
import com.example.czsc.dataSystem.database.DataSourceConnection;
import javafx.scene.layout.Region;
import jdk.nashorn.internal.parser.JSONParser;


import java.io.IOException;
import java.sql.SQLOutput;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.example.czsc.czscAnalyze.chooseStock.AverageDuo.averageDuo;
import static com.example.czsc.czscAnalyze.czscDefine.CzscUtils.getLast;
import static com.example.czsc.czscAnalyze.czscDefine.toolsForXD.IntervalRelationship.*;
import static com.example.czsc.czscAnalyze.objects.Frequency.*;


/**
 * @FileName: ThirdBuyPoint
 * @Author: Haifeng Tong
 * @Date: 2021/5/115:37 下午
 * @Description:
 * @History: 2021/5/11
 */
public class ThirdBuyPoint {
    Frequency frequency;
    LocalDateTime dateBegin;
    Klines klines;
    Analyze analyze;
    boolean isThirdBuyPoint = true;
    String symbol;
    LocalDateTime timeF1;
    LocalDateTime timeF2;
    Center.Region region;
    private ThirdBuyPoint(String symbol, Frequency frequency) throws IOException {
        this.symbol = symbol;
        this.frequency = frequency;
        klines = UnifiedDataFromBaoData.getKlines(symbol,null,LocalDateTime.now(),frequency);
        if(klines.size() == 0){
            isThirdBuyPoint = false;
        }else {
            analyze = new Analyze(klines, frequency);
        }
    }

    private ThirdBuyPoint(String symbol, Frequency frequency, LocalDateTime dateBegin) throws IOException {
        this.symbol = symbol;
        this.frequency = frequency;
        this.dateBegin = dateBegin;
        klines = UnifiedDataFromBaoData.getKlines(symbol,dateBegin,LocalDateTime.now(),frequency);
        if(klines.size() == 0){
            isThirdBuyPoint = false;
        }else {
            analyze = new Analyze(klines, frequency);
        }
    }

    public static boolean isThirdBuyPoint(String symbol,Frequency frequency) throws Exception {
        assert frequency != null;
        if (frequency == YEAR){
            return false;
        }
        LocalDateTime time;
        //往上一个等级的处理，日->周
        Frequency highlyFreq = Frequency.highlyFrequency(frequency);
        Klines highlyK = UnifiedDataFromBaoData.getKlines(symbol, null, null, highlyFreq);
        Analyze highPeriod = new Analyze(highlyK);
        List<BI> bis1 = highPeriod.getBIS();
        if (bis1 == null || bis1.size() == 0){
            System.out.println("股票" + symbol + "没有形成上一个周期的一笔，被抛弃");
            return false;
        }else if (getLast(bis1).getDirection() != Direction.UP) {
            System.out.println("股票" + symbol + "高一级别，最后一笔不是上涨，被抛弃");
            return false;
        }else {
            time = getLast(bis1).getBeginTime();
            System.out.println("股票" + symbol + "开始分析的时间为：" + time);
            if (highlyFreq != null && highlyFreq != YEAR) {
                Frequency highestFreq = Frequency.highlyFrequency(highlyFreq);
                Klines highestK = UnifiedDataFromBaoData.getKlines(symbol, null, null, highestFreq);
                Analyze highestPeriod = new Analyze(highestK);
                List<BI> bis2 = highestPeriod.getBIS();
                if (bis2 != null && getLast(bis2).getDirection() != Direction.DOWN) {
                    System.out.println("股票" + symbol + "爷爷级的最后一笔不是下跌，被抛弃");
                    System.out.println(getLast(bis2));
                    return false;
                }
            }
        }
        Klines klines = UnifiedDataFromBaoData.getKlines(symbol,null,null,frequency);
        klines = Klines.subKLinesAfterTime(klines,time.minusMinutes(frequency.getMinutes() * 8));
        Analyze analyzer = new Analyze(klines,frequency);
        final List<BI> bis = analyzer.getBIS();
        if (bis==null || bis.size() < 4){
            System.out.println("股票" + symbol + "大周期的下跌后没有形成上涨趋势，被抛弃");
            return false;
        }else{
            //保证bis.get(2)的macd 从大于0
            final List<BI> biss = handleSmallBI(bis);//把一些小的笔，合并成大笔
            Center center = new Center(biss,false);
            Center.Region region = center.getRecentRegion();
            if (region == null){
                System.out.println("股票" + symbol + "一个中枢都没有形成");
                return false;
            }
            Klines subklines = Klines.subKLinesAfterTime(klines,getLast(bis).getEndTime());
            BI last = getLast(bis);

            if (getLast(bis).getDirection() == Direction.UP){
                //首先该上涨笔不能是被包含笔
                BI pre = bis.get(bis.size() - 3);
                if (IntervalRelationship.getRelationship(pre,last) == INCLUDEAFTER){
                    return false;
                }

                for (Kline k : subklines){
                    if (k.getHigh() > last.getHigh()){
                        return false;
                    }
                    if (k.getLow() < region.getHigh()){
                        return false;
                    }
                }
            }else{ //最后一笔是下跌
                BI pre = bis.get(bis.size() -2);
                for (Kline k : subklines){
                    if (k.getHigh() > last.getHigh()){
                        return false;
                    }
                    if (k.getLow() < last.getLow() || k.getLow() < region.getHigh()){
                        return false;
                    }
                }
                TaUtils.MACD macds = TaUtils.MACD(klines,12,26,9);
                List<Float> macd = macds.getMacd();
                Optional<Kline> kline1 = klines//获取的是进入中枢的笔，的最后一根K线
                        .stream()
                        .filter(e->e.getTime().isEqual(region.getBis().get(0).getEndTime()))
                        .findFirst();

                if (kline1.isPresent()){
                    if (macd.get(klines.indexOf(kline1.get())) < 0 ||
                            macds.getDiff().get(klines.indexOf(kline1.get())) < 0 ||
                            macds.getDea().get(klines.indexOf(kline1.get())) < 0){
                        System.out.println("股票" + symbol + "中枢前macd 没有上到0轴以上被抛弃");
                        return false;
                    }

                }else{
                    System.out.println("股票" + symbol + "没有找到应该上0轴的K线");
                }
                Optional<Kline> kline2 = klines//获取的是下跌笔的最后一根K线
                        .stream()
                        .filter(e->e.getTime().isEqual(getLast(biss).getEndTime()))
                        .findFirst();
                if (kline2.isPresent()){
                    if (macds.getDiff().get(klines.indexOf(kline2.get())) < 0 ||
                            macds.getDea().get(klines.indexOf(kline2.get())) < 0){
                        System.out.println("股票" + symbol + "最后一笔macd 跌到0轴以下");
                        return false;
                    }
                }else{
                    System.out.println("股票" + symbol + "后一根K线");
                }
            }
            if (biss.get(0).getHigh() >= getLast(biss).getLow()){//反之出现下跌后的暴力反转
                System.out.println("股票" + symbol + "最后一笔的低点回到中枢内");
                return false;
            }
            if (getLast(biss).getLow() > region.getHigh() &&
                    biss.get(0).getHigh() < getLast(biss).getLow() &&
                    (biss.get(0).getLow() < region.getLow())){
                return true;
            }else{
                System.out.println("股票" + symbol + "最后一笔的低点又破坏中枢");
                return false;
            }
        }
    }

    /**
     * 这样处理的好处是，确定中枢高点更准确
     * 不知道怎么处理，就先不处理了
     * @param bis
     * @return
     */
    private static List<BI> handleSmallBI(List<BI> bis) {
        return bis;
    }
    public static boolean thirdBuyPoint(String symbol,Frequency frequency) throws Exception {
        if (frequency == Frequency.YEAR){
            return false;
        }
        Frequency highlyFreq = Frequency.highlyFrequency(frequency);
        System.out.println("高一级别的周期为" + highlyFreq);
        Klines klines = UnifiedDataFromBaoData.getKlines(symbol,null,null,highlyFreq);
        Analyze analyze = new Analyze(klines,frequency);
        List<BI> bis = analyze.getBIS();
        if (bis == null || bis.size() == 0 || getLast(bis).getDirection() == Direction.DOWN){
            return false;
        }else{
            System.out.println("当前股票："  + symbol + "符合周线特征，最后一笔为上涨笔");
        }
        LocalDateTime time = getLast(bis).getBeginTime().minusMinutes(frequency.getMinutes() * 8);
        System.out.println("日线分析的起点为：" + time);
        Klines newKlines = UnifiedDataFromBaoData.getKlines(symbol,null,null,frequency);
        //放在这里是因为不同的处理方式，macd是不一样的
        List<TaUtils.SingleMACD> macds = TaUtils.singleMACDList(newKlines,12,26,9);

        newKlines = Klines.subKLinesAfterTime(newKlines,time);
        Analyze analysis = new Analyze(newKlines,frequency);
        List<BI> thisBis = analysis.getBIS();
        if (thisBis == null || thisBis.size() == 0){
            return false;
        }
        System.out.println("当前日周期形成的笔数为：" + thisBis.size());
        if (thisBis.get(0).getDirection() == Direction.DOWN){
            thisBis.remove(0);
        }
        if (thisBis.size() < 2){
            return false;
        }
        int buyPointTwo = -1;
        float high = Float.NaN;
        for (int i=0;i<thisBis.size();i = i+2){
            BI bi = thisBis.get(i);
            System.out.println("当前股票："  + symbol + "检查上涨笔，方向是上涨吗？" + (bi.getDirection() == Direction.UP));
            System.out.println("当前股票："  + symbol + ";buyPointTwo位置为：" + buyPointTwo);
            if (buyPointTwo == -1) {//寻找macd高于0的第一个点
                Optional<Kline> kline = newKlines//获取的是进入中枢的笔，的最后一根K线
                        .stream()
                        .filter(e -> e.getTime().isEqual(bi.getEndTime()))
                        .findFirst();

                if (kline.isPresent()) {
                    TaUtils.SingleMACD macd = null;
                    for (TaUtils.SingleMACD s : macds){
                        if (s.getKline() == kline.get()){
                            macd = s;
                            break;
                        }
                    }
                    assert macd != null;
                    if (macd.getMacd()> 0 && macd.getDea() > 0 && macd.getDiff() > 0) {
                        buyPointTwo = i;
                        high = bi.getHigh();
                    }
                }
            }else{//后面还有高于这个点的笔，且不是最后一笔，那就不是第三类买点
                System.out.println("当前股票："  + symbol + ";buyPointTwo位置不为空");
                System.out.println("当前笔的高点为：" + bi.getHigh() + ";低点为：" + bi.getLow());
                System.out.println("当前high = " + high);
                System.out.println("当前i = " + i + "当前笔数为" + (thisBis.size() - 1));
                if (bi.getHigh() > high && i != thisBis.size() - 1){
                    return false;
                }
            }
        }
        if (buyPointTwo == -1){//没有突破MACD0轴
            return false;
        }else{//突破了0轴，判断目前的价格高于之前的高点，
            BI bi = getLast(thisBis);
            Klines lastKlines = Klines.subKLinesAfterTime(newKlines,bi.getEndTime());
            Kline kline = lastKlines.get(0);
            if (bi.getDirection() == Direction.DOWN){
                if (bi.getHigh() <= high){//表明当前笔是第二类买点的形成笔
                    for (int i=1;i< lastKlines.size();i++) {
                        if (lastKlines.get(i) .getHigh() > kline.getHigh()){
                            kline = lastKlines.get(i);
                        }
                    }
                    if (kline == getLast(lastKlines)){//后面一直上涨，没有回调
                        return false;
                    }else{
                        int k = lastKlines.indexOf(kline);//后面出现回调低于中枢
                        for (int j=k + 1;j<lastKlines.size();j++){
                            if (lastKlines.get(j).getLow() < high){
                                return false;
                            }
                        }
                    }
                }
            }else{//最后一笔是上涨，出现小幅度回调-------也就是次级别回调
                int k = lastKlines.indexOf(kline);//后面出现回调低于中枢
                for (int j=k + 1;j<lastKlines.size();j++){
                    if (lastKlines.get(j).getLow() < high){
                        return false;
                    }
                }
                return true;
            }
            /*for (Kline k : lastKlines.getBars()) {
                if (k.getLow() < high) {
                    return false;
                }
                if (bi.getDirection() == Direction.UP){
                    if (k.getHigh() > bi.getHigh()){
                        return false;
                    }
                }
            }*/



            /*
            //最后一个次级别的30分钟必须是向下的，或者形成一个30分钟中枢
            LocalDateTime endTime = getLast(thisBis).getEndTime().minusMinutes(frequency.getMinutes());
            Frequency subFreq = Frequency.secondaryFrequency(frequency);
            Klines subKlines = UnifiedDataFromBaoData.getKlines(symbol,null,null,subFreq);
            Analyze subAnalyze = new Analyze(subKlines,subFreq);
            List<BI> subBis = subAnalyze.getBIS();
            if (subBis == null || subBis.size() == 0){
                return false;
            }else{
                if (subBis.size() == 1 && subBis.get(0).getDirection() == Direction.DOWN){
                    return true;
                }else{
                    if(subBis.size() > 2){
                        Center center = new Center(subBis,false);
                        return center.getRecentRegion() != null;
                    }
                }
            }*/
            return true;
        }
        //return false;
    }

    public static void main(String[] args) throws Exception {
        List<String[]> result = DataSourceConnection.getResultSet("select symbol from symbol_name");
        List<String> duo = new ArrayList<>();
        /*List<String[]> result = new ArrayList<>();
        result.add(new String[]{"600368"});*/
        List<String> has = new ArrayList<>();
        /*List<String[]> result = new ArrayList<>();
        result.add(new String[]{"600368"});*/
        for(String[] s : result){
            try {
                Klines klines = UnifiedDataFromBaoData.getKlines(s[0], null, null, DAY);
                if (averageDuo(klines)) {
                    duo.add(s[0]);
                    /*if(ChooseUtils.huoYue(klines)) {
                        duo.add(s[0]);//还有一些问题
                    }*/
                }
            }catch (Exception ignored){
            }
        }
        System.out.println(has);
        for(String s : duo){
            try {
                if (thirdBuyPoint(s, DAY)) {
                    has.add(s);
                    System.out.println(s + "是第三类买点");
                } else {
                    System.out.println("------------" + s + "没有形成第三类买点");
                }
            }catch(Exception  e){
                e.printStackTrace();
                System.out.println("X X X X X X X X X X X X X X X X X X X X " + s + "出现错误");
            }
        }
        System.out.println(has);
        //System.out.println(isThirdBuyPoint("002603",Frequency.DAY));
    }
}
