package com.example.czsc.dataSystem.simpleData;



import com.example.czsc.common.Kline;
import com.example.czsc.common.Klines;
import com.example.czsc.czscAnalyze.objects.Frequency;
import com.sun.istack.internal.NotNull;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;


/**
 * @FileName: DataFromBockStockCSV
 * @Author: Haifeng Tong
 * @Date: 2021/3/2810:34 下午
 * @Description:
 * @History:
 */
public class DataFromBockStockCSV {
    private static Klines readMinuteKlines(String fileName) {
        Klines klines = new Klines(new ArrayList<>());
        //datetime symbol name，close，high，low，open，preClose
        //日期	股票代码	名称	收盘价	最高价	最低价	开盘价	前收盘	涨跌额	涨跌幅	换手率	成交量	成交金额	总市值	流通市值 成交笔数
        //date	code	open	high	low	close	preclose	volume	amount,tradestatus
        File file = new File(fileName);
        try {
            DataInputStream in = new DataInputStream(new FileInputStream(file));
            BufferedReader reader = new BufferedReader(new InputStreamReader(in,"GBK"));
            String title = reader.readLine(); //抛弃标题行
            //很多新股可能并没有任何数据
            while(reader.ready()){
                String line = reader.readLine();
                String[] split = line.split(",");
                //处理错误行
                boolean brokenData = false;
                for(String s : split){
                    if(s.length() == 0){
                        brokenData = true;
                        break;
                    }
                }
                if(brokenData){
                    continue;
                }

                Kline kline = new Kline();
                if(line.contains("None")){
                    continue;
                }
                String time = split[1].substring(0,14);
                LocalDateTime date = LocalDateTime.parse(time, DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
                kline.setTime(date);
                kline.setSymbol(split[2].substring(3));
                kline.setOpen(Float.parseFloat(split[3]));
                kline.setHigh(Float.parseFloat(split[4]));
                kline.setLow(Float.parseFloat(split[5]));
                kline.setClose(Float.parseFloat(split[6]));
                kline.setVolume(Float.parseFloat(split[7]) / 100);
                kline.setExchangeMoney(Float.parseFloat(split[8]));
                klines.add(kline);
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        //因为即使只有标题行，下面的步骤也会进行，所以会出问题
        Collections.sort(klines);
        return klines;
    }
    public static Klines readDay(String fileName) throws IOException {
        Klines klines = new Klines(new ArrayList<>());
        //datetime symbol name，close，high，low，open，preClose
        //日期	股票代码	名称	收盘价	最高价	最低价	开盘价	前收盘	涨跌额	涨跌幅	换手率	成交量	成交金额	总市值	流通市值 成交笔数
        //date	code	open	high	low	close	preclose	volume	amount,tradestatus
        File file = new File(fileName);
        try {
            DataInputStream in = new DataInputStream(new FileInputStream(file));
            BufferedReader reader = new BufferedReader(new InputStreamReader(in,"GBK"));
            String title = reader.readLine(); //抛弃标题行
            //很多新股可能并没有任何数据
            while(reader.ready()){
                String line = reader.readLine();
                String[] split = line.split(",");
                //处理错误行
                boolean brokenData = false;
                for(String s : split){
                    if(s.length() == 0){
                        brokenData = true;
                        break;
                    }
                }
                if(brokenData){
                    continue;
                }

                Kline kline = new Kline();
                if(line.contains("None")){
                    continue;
                }
                LocalDate date = LocalDate.parse(split[0], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                kline.setTime(LocalDateTime.of(date, LocalTime.of(15, 0, 0)));
                kline.setSymbol(split[1].substring(3));
                kline.setOpen(Float.parseFloat(split[2]));
                kline.setHigh(Float.parseFloat(split[3]));
                kline.setLow(Float.parseFloat(split[4]));
                kline.setClose(Float.parseFloat(split[5]));
                kline.setPreClose(Float.parseFloat(split[6]));
                kline.setVolume(Float.parseFloat(split[7]) / 100);
                kline.setExchangeMoney(Float.parseFloat(split[8]));
                klines.add(kline);
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        //因为即使只有标题行，下面的步骤也会进行，所以会出问题
        Collections.sort(klines);
        return klines;
    }
    public static Klines read(String fileName,LocalDateTime fromTime,LocalDateTime toTime) throws IOException {
        Klines klines = readDay(fileName);
        return getBars(fromTime, toTime, klines);
    }

    @NotNull
    static Klines getBars(LocalDateTime fromTime, LocalDateTime toTime, Klines klines) {
        Klines newKlines = new Klines(new ArrayList<>());
        for (Kline kline : klines){
            if (kline.getTime().isAfter(fromTime) && kline.getTime().isBefore(toTime)){
                newKlines.add(kline);
            }
        }
        return newKlines;
    }

    public static Klines read(String fileName,LocalDateTime fromTime) throws IOException {
        LocalDateTime toTime = LocalDateTime.now();
        return read(fileName,fromTime,toTime);
    }
    public static Klines read(String fileName,int size) throws IOException {
        Klines klines = readDay(fileName);
        if (size <= klines.size()) {
            klines= new Klines(klines.subList(klines.size() - size, klines.size()));
        }
        return klines;
    }
    public static Klines getKlines(String symbol, Frequency frequency) throws IOException {
        StringBuilder fileName = new StringBuilder("/Users/haifeng/Documents/stock_database/baostock/");
        if (frequency == Frequency.MONTH){
            fileName.append("month/");
        }
        else if (frequency == Frequency.WEEK){
            fileName.append("week/");
        }else if (frequency.getMinutes() == 1440){
            fileName.append("day/");
        }else if (frequency.getMinutes() == 30){
            fileName.append("min30/");
        }else if (frequency.getMinutes() == 5){
            fileName.append("min5/");
        }else{
            throw new IllegalArgumentException("暂时不支持该周期");
        }
        fileName.append(symbol).append(".csv");
        if (frequency == Frequency.WEEK || frequency == Frequency.MONTH){
            return readyWeek(fileName.toString());
        }
        if (frequency.getMinutes() == 1440) {
            return readDay(fileName.toString());
        }else{
            return readMinuteKlines(fileName.toString());
        }
    }

    private static Klines readyWeek(String fileName) {
        Klines klines = new Klines(new ArrayList<>());
        //datetime symbol name，close，high，low，open，preClose
        //日期	股票代码	名称	收盘价	最高价	最低价	开盘价	前收盘	涨跌额	涨跌幅	换手率	成交量	成交金额	总市值	流通市值 成交笔数
        //date	code	open	high	low	close	volume	amount turn pctChg
        File file = new File(fileName);
        try {
            DataInputStream in = new DataInputStream(new FileInputStream(file));
            BufferedReader reader = new BufferedReader(new InputStreamReader(in,"GBK"));
            String title = reader.readLine(); //抛弃标题行
            //很多新股可能并没有任何数据
            while(reader.ready()){
                String line = reader.readLine();
                String[] split = line.split(",");
                //处理错误行
                boolean brokenData = false;
                for(String s : split){
                    if(s.length() == 0){
                        brokenData = true;
                        break;
                    }
                }
                if(brokenData){
                    continue;
                }

                Kline kline = new Kline();
                if(line.contains("None")){
                    continue;
                }
                LocalDate date = LocalDate.parse(split[0], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                kline.setTime(LocalDateTime.of(date, LocalTime.of(15, 0, 0)));
                kline.setSymbol(split[1].substring(3));
                kline.setOpen(Float.parseFloat(split[2]));
                kline.setHigh(Float.parseFloat(split[3]));
                kline.setLow(Float.parseFloat(split[4]));
                kline.setClose(Float.parseFloat(split[5]));
                kline.setVolume(Float.parseFloat(split[6]) / 100);
                kline.setExchangeMoney(Float.parseFloat(split[7]));
                klines.add(kline);
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        //因为即使只有标题行，下面的步骤也会进行，所以会出问题
        Collections.sort(klines);
        return klines;
    }

    public static void main(String[] args) throws IOException {
        Klines klines = getKlines("600919",Frequency.MIN5);
        for (Kline kline : klines) {
            System.out.println(kline);
        }
    }
}
