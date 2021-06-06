package com.example.czsc.dataSystem.urlRes;



import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.czsc.common.Kline;
import com.example.czsc.common.Klines;
import com.example.czsc.czscAnalyze.objects.Frequency;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

import static com.example.czsc.utils.Utils.println;


/**
 * @FileName: SmallFreqKline
 * @Author: Haifeng Tong
 * @Date: 2021/4/102:00 下午
 * @Description:
 * @History:
 */
public class SmallFreqKline {
    public static Klines minuteKline(String symbol, Frequency frequency, int count){
        String code;
        if (symbol.startsWith("6")){
            code = "sh" + symbol;
        }else{
            code = "sz" + symbol;
        }
        if (frequency.getMinutes() < 5 || frequency.getMinutes() > 60){
            throw new InvalidParameterException("只支持5、15，30，60分钟数据");
        }
        String html = "http://money.finance.sina.com.cn/quotes_service/api/json_v2.php/CN_MarketData.getKLineData?symbol=" +
                code + "&scale=" + frequency.getMinutes() + "&ma=no&datalen=" + count;
        String results = getUrlResult(html);
        Klines klines = new Klines();
        klines.setFrequency(frequency);
        JSONArray jsonArray = JSONArray.parseArray(results.toString());
        for (int i=0;i<jsonArray.size(); i++){
            Kline kline = new Kline();
            kline.setSymbol(symbol);
            JSONObject json = jsonArray.getJSONObject(i);
            kline.setTime(LocalDateTime.parse(json.getString("day"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            kline.setOpen(json.getFloat("open"));
            kline.setHigh(json.getFloat("high"));
            kline.setLow(json.getFloat("low"));
            kline.setClose(json.getFloat("close"));
            kline.setVolume(json.getLongValue("volume"));
            klines.add(kline);
        }

        if (klines.size() > count){
            klines= new Klines(klines.subList(klines.size() - count,klines.size() - 1));
        }
        Collections.sort(klines);
        return klines;
    }
    public static Klines minuteKline(String symbol, Frequency frequency){
        return minuteKline(symbol,frequency,1023);
    }
    public static Klines oneMinuteKline(String symbol){
        String code;
        if (symbol.startsWith("6")){
            code = "sh" + symbol;
        }else{
            code = "sz" + symbol;
        }
        String html = "https://web.ifzq.gtimg.cn/appstock/app/kline/mkline?param=" + code + ",m1";
        String results = getUrlResult(html);
        Klines klines = new Klines();
        klines.setFrequency(Frequency.MIN);
        JSONObject allObject = JSONArray.parseObject(results.toString());//{}
        JSONObject data = allObject.getJSONObject("data");//"data"
        JSONObject stock = data.getJSONObject(code);//"sh600519"
        JSONObject qt = stock.getJSONObject("qt");//"qt"
        JSONArray attr = qt.getJSONArray(code);//"sh600519":["1","贵州茅台","600519"]
        String name = attr.getString(1);
        JSONArray m1 = stock.getJSONArray("m1");
        for (int i=0;i<m1.size(); i++){
            Kline kline = new Kline();
            kline.setSymbol(symbol);
            kline.setName(name);
            JSONArray json = m1.getJSONArray(i);
            kline.setTime(LocalDateTime.parse(json.getString(0), DateTimeFormatter.ofPattern("yyyyMMddHHmm")));
            kline.setOpen(json.getFloat(1));
            kline.setClose(json.getFloat(2));
            kline.setHigh(json.getFloat(3));
            kline.setLow(json.getFloat(4));
            kline.setVolume(json.getFloat(5));
            klines.add(kline);
        }
        Collections.sort(klines);
        return klines;
    }
    public static String getUrlResult(String httpUrl){
        StringBuilder results = new StringBuilder();
        try{
            URL url = new URL(httpUrl);
            URLConnection conn = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            String line;
            while ((line = in.readLine()) != null){
                results.append(line);
            }

        }catch(IOException e){
            e.printStackTrace();
        }
        return results.toString();
    }
    public static void main(String[] args) {
        //count为2，只能取到1个，可能是不能去最后一个，最后一个需要手动合成
        Klines klines = minuteKline("000002",Frequency.HOUR,3);
        println(klines.size());
        println(klines);
        println(klines.getFrequency());
    }
}
