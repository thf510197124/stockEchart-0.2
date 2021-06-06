package com.example.czsc.dataSystem.simpleData;


import com.example.czsc.common.Kline;
import com.example.czsc.common.Klines;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;

import static com.example.czsc.utils.Utils.println;


/**
 * @FileName: DataFromXls
 * @Author: Haifeng Tong
 * @Date: 2021/3/38:40 上午
 * @Description:
 * @History:
 */
public class DataFromWangYiCSV {
    public static Klines read(String fileName) throws IOException {
        return read(fileName,null,null);
    }
    public static Klines read(String fileName,LocalDateTime fromTime,LocalDateTime toTime) throws IOException {
        Klines klines = new Klines(new ArrayList<>());
        //datetime symbol name，close，high，low，open，preClose
        //日期	股票代码	名称	收盘价	最高价	最低价	开盘价	前收盘	涨跌额	涨跌幅	换手率	成交量	成交金额	总市值	流通市值 成交笔数
        //date	code	open	high	low	close	preclose	volume	amount
        File file = new File(fileName);
        if(fromTime == null){
            fromTime = LocalDateTime.MIN;
        }
        if(toTime == null){
            toTime = LocalDateTime.MAX;
        }
        try {
            DataInputStream in = new DataInputStream(new FileInputStream(file));
            BufferedReader reader = new BufferedReader(new InputStreamReader(in,"GBK"));
            String title = reader.readLine(); //抛弃标题行
            if (!reader.ready()){//如果只有标题行，返回空。新股可能会出现这种情况
                return null;
            }
            while(reader.ready()){
                String line = reader.readLine();
                String[] split = line.split(",");
                //println(line);
                Kline kline = new Kline();
                if(Float.parseFloat(split[6]) == 0f ||  Float.parseFloat(split[7]) == 0f || split[8].equals("None")){
                    //println(line);
                    continue;
                }

                LocalDate date = LocalDate.parse(split[0], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                LocalDateTime time = LocalDateTime.of(date,LocalTime.of(15,0,0));
                if (time.isBefore(fromTime) || time.isAfter(toTime)){
                    continue;
                }else {
                    kline.setTime(LocalDateTime.of(date, LocalTime.of(15, 0, 0)));
                }

                kline.setSymbol(split[1].substring(1));
                kline.setName(split[2]);
                kline.setClose(Float.parseFloat(split[3]));
                kline.setHigh(Float.parseFloat(split[4]));
                kline.setLow(Float.parseFloat(split[5]));
                kline.setOpen(Float.parseFloat(split[6]));
                kline.setPreClose(Float.parseFloat(split[7]));
                //kline.setPriceChange(Float.parseFloat(split[8]));
                /*if(split[9].length() != 0) {
                    kline.setPriceChangePercent(Float.parseFloat(split[9]));
                }*/
                if(split[10].length() != 0) {
                    kline.setExchangePercent(Float.parseFloat(split[10]));
                }
                kline.setVolume(Float.parseFloat(split[11]));
                kline.setExchangeMoney(Float.parseFloat(split[12]));
                //System.out.println("in DadaFromWangYiCSV，kline = " + kline);
                klines.add(kline);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        if(klines.size() == 0){
            return null;
        }
        Collections.sort(klines);
        return klines;
    }
    public static Klines read(String fileName,LocalDateTime fromTime) throws IOException {
        return read(fileName,fromTime,null);
    }
    public static Klines read(String fileName,int size) throws IOException {
        Klines klines = read(fileName);
        if (size <= klines.size()) {
            klines= new Klines(klines.subList(klines.size() - size, klines.size()));
        }
        return klines;
    }

    public static void main(String[] args) throws IOException {
        String fileName = "/Users/haifeng/Documents/stock_database/trading163/603288.csv";
        //Klines klines = read(fileName,100);
        Klines klines = read(fileName,LocalDateTime.of(2020,1,1,0,0,0),
                LocalDateTime.of(2020,9,4,0,0,0));
        assert klines != null;
    }
}
