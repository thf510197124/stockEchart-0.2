package com.example.czsc.dataSystem.urlRes;


import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.example.czsc.common.CurrentK;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.example.czsc.utils.Utils.println;
import static com.example.czsc.utils.Utils.unicodeDecode;


/**
 * 获取当前数数据
 * @FileName: CurrentData
 * @Author: Haifeng Tong
 * @Date: 2021/4/96:26 下午
 * @Description:
 * @History:
 */
public class CurrentData {
    public static CurrentK getDataFrom126(String symbol){
        if(symbol.startsWith("6")){
            symbol = "0" +symbol;
        }else{
            symbol = "1" + symbol;
        }
        String html = "http://api.money.126.net/data/feed/";
        StringBuilder results= new StringBuilder();
        try{
            URL url = new URL(html + symbol);
            URLConnection conn = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            String line;
            while ((line = in.readLine()) != null){
                results.append(line);
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        println(results);
        String js = results.toString();
        js = js.substring(js.indexOf("{"), js.lastIndexOf("}") + 1);
        CurrentK k = new CurrentK();
        try{
            JSONObject json = JSONObject.parseObject(js);
            JSONObject ks = JSONObject.parseObject(json.getString(symbol));
            k.setSymbol(ks.getString("symbol"));
            k.setName(ks.getString("name"));
            k.setHigh(ks.getFloat("high"));
            k.setLow(ks.getFloat("low"));
            k.setVolume(ks.getIntValue("volume")/1000000.0f);
            k.setExchangeMoney(ks.getFloatValue("turnover")/100000000.0f);
            k.setName(unicodeDecode(ks.getString("name")));
            k.setClose(ks.getFloat("price"));
            k.setOpen(ks.getFloat("open"));
            k.setBuyAmount1(getShou(ks.getString("bidvol1")));//10 1593（买一数）
            k.setBuyPrice1(ks.getFloat("bid1"));//11 20.390（买一加）
            k.setBuyAmount2(getShou(ks.getString("bidvol2")));//12 38900（买二数）
            k.setBuyPrice2(ks.getFloat("bid2"));//13 20.280（买二价）
            k.setBuyAmount3(getShou(ks.getString("bidvol3")));//14 9800（买三数
            k.setBuyPrice3(ks.getFloat("bid3"));//15 20.270（买三价）
            k.setBuyAmount4(getShou(ks.getString("bidvol4")));//16 45200（买四数）
            k.setBuyPrice4(ks.getFloat("bid4"));//17 20.260（买四价）
            k.setBuyAmount5(getShou(ks.getString("bidvol5")));//18 1000（买5数）,
            k.setBuyPrice5(ks.getFloat("bid5"));//19 20.230（买5价）
            k.setSellAmount1(getShou(ks.getString("askvol1")));//20 11400（卖一数）
            k.setSellPrice1(ks.getFloat("ask1"));//21 20.400（卖1价）
            k.setSellAmount2(getShou(ks.getString("askvol2")));//22 3600（卖2数）
            k.setSellPrice2(ks.getFloat("ask2"));//23 20.410（卖2价）
            k.setSellAmount3(getShou(ks.getString("askvol3")));//24 5900（卖3数）
            k.setSellPrice3(ks.getFloat("ask3"));//25 20.430（卖3价）
            k.setSellAmount4(getShou(ks.getString("askvol4")));//26 62700（卖四数）
            k.setSellPrice4(ks.getFloat("ask4"));//27 20.440（卖四价）
            k.setSellAmount5(getShou(ks.getString("askvol5")));//28 18500（卖5数）
            k.setSellPrice5(ks.getFloat("ask5"));//29 20.450（卖5价）
            k.setPreClose(ks.getFloat("yestclose"));
            k.setTime(LocalDateTime.parse(ks.getString("time"),DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));
        }catch(JSONException e){
            e.printStackTrace();
        }
        return k;
    }
    public static CurrentK getDataFromTencent(String symbol){
        //因为是盘后数据，所以很多可能没有对应上。
        if (symbol.startsWith("6")){
            symbol = "sh" + symbol;
        }else{
            symbol = "sz"+symbol;
        }
        String html = "http://qt.gtimg.cn/q=" + symbol;
        StringBuilder results= new StringBuilder();
        try {
            URL url = new URL(html);
            URLConnection conn = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"gb2312"));
            String line;
            while((line = in.readLine())!=null){
                results.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        CurrentK k = new CurrentK();
        int indexEq = results.indexOf("\"");
        String result = results.substring(indexEq+1);

        System.out.println(result);

        String[] arr = result.split("~");
        /*for (int i=0;i<arr.length;i++){
            println("-------      " + arr[i]);
        }*/
        k.setName(arr[1]);//0盈康生命
        k.setSymbol(arr[2]);
        k.setClose(Float.parseFloat(arr[3]));
        k.setPreClose(Float.parseFloat(arr[4]));
        k.setOpen(Float.parseFloat(arr[5]));
        k.setHigh(Float.parseFloat(arr[33]));//4 20.680（最高）
        k.setLow(Float.parseFloat(arr[34]));//5 19.860（最低）
        k.setVolume(Float.parseFloat(arr[36]) / 10000);//成交量设置为万手//8 4590629（成交量）
        k.setExchangeMoney(Float.parseFloat(arr[37])/10000);//成交额设置为万//,9 92615053.630（成交额）
        k.setBuyAmount1(Integer.parseInt(arr[10]));//10 1593（买一数）
        k.setBuyPrice1(Float.parseFloat(arr[9]));//11 20.390（买一加）
        k.setBuyAmount2(Integer.parseInt(arr[12]));//12 38900（买二数）
        k.setBuyPrice2(Float.parseFloat(arr[11]));//13 20.280（买二价）
        k.setBuyAmount3(Integer.parseInt(arr[14]));//14 9800（买三数
        k.setBuyPrice3(Float.parseFloat(arr[13]));//15 20.270（买三价）
        k.setBuyAmount4(Integer.parseInt(arr[16]));//16 45200（买四数）
        k.setBuyPrice4(Float.parseFloat(arr[15]));//17 20.260（买四价）
        k.setBuyAmount5(Integer.parseInt(arr[18]));//18 1000（买5数）,
        k.setBuyPrice5(Float.parseFloat(arr[17]));//19 20.230（买5价）
        k.setSellAmount1(Integer.parseInt(arr[20]));//20 11400（卖一数）
        k.setSellPrice1(Float.parseFloat(arr[19]));//21 20.400（卖1价）
        k.setSellAmount2(Integer.parseInt(arr[22]));//22 3600（卖2数）
        k.setSellPrice2(Float.parseFloat(arr[21]));//23 20.410（卖2价）
        k.setSellAmount3(Integer.parseInt(arr[24]));//24 5900（卖3数）
        k.setSellPrice3(Float.parseFloat(arr[23]));//25 20.430（卖3价）
        k.setSellAmount4(Integer.parseInt(arr[26]));//26 62700（卖四数）
        k.setSellPrice4(Float.parseFloat(arr[25]));//27 20.440（卖四价）
        k.setSellAmount5(Integer.parseInt(arr[28]));//28 18500（卖5数）
        k.setSellPrice5(Float.parseFloat(arr[27]));//29 20.450（卖5价）
        k.setTime(LocalDateTime.parse(arr[30], DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));//30 20210409161503
        k.setDownLimit(Float.parseFloat(arr[48]));
        k.setUpLimit(Float.parseFloat(arr[47]));
        k.setExchangePercent(Float.parseFloat(arr[38]));
        return k;
    }
    public static CurrentK getDataFromSina(String symbol){
        if (symbol.startsWith("6")){
            symbol = "sh" + symbol;
        }else{
            symbol = "sz"+symbol;
        }
        String html = "http://hq.sinajs.cn/list=" + symbol;
        StringBuilder results= new StringBuilder();
        try {
            URL url = new URL(html);
            URLConnection conn = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"gb2312"));
            String line;
            while((line = in.readLine())!=null){
                results.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        CurrentK k = new CurrentK();
        k.setSymbol(symbol.substring(2));
        int indexEq = results.indexOf("\"");
        //String result = results.substring(indexEq+1,results.length()-2);
        String result = results.substring(indexEq+1);
        //println(result);
        String[] arr = result.split(",");
        k.setName(arr[0]);//0盈康生命
        k.setOpen(Float.parseFloat(arr[1]));//1 20.220(开盘)
        k.setPreClose(Float.parseFloat(arr[2]));//,2 20.250（昨收）
        k.setClose(Float.parseFloat(arr[3]));//3 20.390(收盘)
        k.setHigh(Float.parseFloat(arr[4]));//4 20.680（最高）
        k.setLow(Float.parseFloat(arr[5]));//5 19.860（最低）
        k.setVolume(Float.parseFloat(arr[8]) / 1000000);//成交量设置为万手//8 4590629（成交量）
        k.setExchangeMoney(Float.parseFloat(arr[9])/100000000);//成交额设置为万//,9 92615053.630（成交额）
        k.setBuyAmount1(getShou(arr[10]));//10 1593（买一数）
        k.setBuyPrice1(Float.parseFloat(arr[11]));//11 20.390（买一加）
        k.setBuyAmount2(getShou(arr[12]));//12 38900（买二数）
        k.setBuyPrice2(Float.parseFloat(arr[13]));//13 20.280（买二价）
        k.setBuyAmount3(getShou(arr[14]));//14 9800（买三数
        k.setBuyPrice3(Float.parseFloat(arr[15]));//15 20.270（买三价）
        k.setBuyAmount4(getShou(arr[16]));//16 45200（买四数）
        k.setBuyPrice4(Float.parseFloat(arr[17]));//17 20.260（买四价）
        k.setBuyAmount5(getShou(arr[18]));//18 1000（买5数）,
        k.setBuyPrice5(Float.parseFloat(arr[19]));//19 20.230（买5价）
        k.setSellAmount1(getShou(arr[20]));//20 11400（卖一数）
        k.setSellPrice1(Float.parseFloat(arr[21]));//21 20.400（卖1价）
        k.setSellAmount2(getShou(arr[22]));//22 3600（卖2数）
        k.setSellPrice2(Float.parseFloat(arr[23]));//23 20.410（卖2价）
        k.setSellAmount3(getShou(arr[24]));//24 5900（卖3数）
        k.setSellPrice3(Float.parseFloat(arr[25]));//25 20.430（卖3价）
        k.setSellAmount4(getShou(arr[26]));//26 62700（卖四数）
        k.setSellPrice4(Float.parseFloat(arr[27]));//27 20.440（卖四价）
        k.setSellAmount5(getShou(arr[28]));//28 18500（卖5数）
        k.setSellPrice5(Float.parseFloat(arr[29]));//29 20.450（卖5价）
        k.setTime(time(arr[30],arr[31]));//30 2021-04-09（日期）,31 16:30:00（时间）
        return k;
    }
    private static int getShou(String in){
        float f = Float.parseFloat(in);
        return new BigDecimal(f).divide(new BigDecimal(100), RoundingMode.HALF_UP)
                .intValue();
    }
    private static LocalDateTime time(String date,String time){
        int year =Integer.parseInt(date.split("-")[0]);
        int month =Integer.parseInt(date.split("-")[1]);
        int day =Integer.parseInt(date.split("-")[2]);
        int hour = Integer.parseInt(time.split(":")[0]);
        int minute = Integer.parseInt(time.split(":")[1]);
        int second = Integer.parseInt(time.split(":")[2]);
        return LocalDateTime.of(year,month,day,hour,minute,second);
    }
    public static void main(String[] args) throws IOException {
        /*String urlStr = "http://hq.sinajs.cn/list=sh600389";
        URL url = new URL(urlStr);
        URLConnection conn = url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"gb2312"));
        String line;
        while ((line = in.readLine()) != null){
            println(line);
        }*/
        //getDataFromSina("601006");
        CurrentK ks= getDataFromTencent("600919");
        /*CurrentK ks = getDataFromTencent("000002");*/
        System.out.println(ks);
    }
}
