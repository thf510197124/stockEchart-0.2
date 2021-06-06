package com.example.czsc.dataSystem.urlRes;

import com.example.czsc.common.Kline;
import com.example.czsc.common.Klines;
import com.example.czsc.czscAnalyze.objects.Frequency;
import com.example.czsc.dataSystem.simpleData.DataFromWangYiCSV;
import com.sun.istack.internal.Nullable;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.time.*;
import java.time.temporal.WeekFields;
import java.util.*;

import static com.example.czsc.czscAnalyze.czscDefine.CzscUtils.getLast;
import static com.example.czsc.utils.Utils.println;

/**
 * @FileName: WangYi
 * @Author: Haifeng Tong
 * @Date: 2021/4/95:00 下午
 * @Description:
 * @History:
 */
public class WangYiDayData {
    public static Klines getDayKlines(String symbol){
        LocalDateTime origin = LocalDateTime.of(1990,1,1,0,0);
        return getDayKlines("600352",date(origin));
    }
    public static Klines getDayKlines(String symbol,@Nullable LocalDateTime beginTime){
        LocalDateTime origin = LocalDateTime.of(1990,1,1,0,0);
        return getDayKlines(symbol,origin,null);
    }
    public static Klines getDayKlines(String symbol,LocalDateTime beginTime,@Nullable LocalDateTime endTime){
        LocalDateTime origin = LocalDateTime.of(1990,1,1,0,0);
        if (beginTime == null || beginTime.isBefore(origin)){
            beginTime = LocalDateTime.of(1990,1,1,0,0);
        }
        if(endTime == null){
            endTime = LocalDateTime.now();
        }
        return getDayKlines(symbol,date(beginTime),date(endTime));
    }
    public static Klines getDayKlines(String symbol,String beginDate){
        if(beginDate == null || beginDate.length() == 0){
            beginDate = "19900101";
        }
        return getDayKlines(symbol,beginDate,date(LocalDateTime.now()));
    }
    public static Klines getDayKlines(String symbol, String beginDate, String endDate){
        String html = "http://quotes.money.163.com/service/chddata.html?code=";
        if(symbol.startsWith("sh.")){//应对获取指数
            symbol = symbol.replace("sh.","0");
        }else if(symbol.startsWith("sz.")){
            symbol = symbol.replace("sz.","1");
        }
        else if(symbol.startsWith("6") || symbol.startsWith("9")){//9开头为上海B股
            symbol = "0"+symbol;
        }else{
            symbol = "1"+ symbol;
        }
        String urlStr = html + symbol + "&start="+beginDate + "&end="+ endDate;
        //System.out.println("WangYiDayData 从网上下载数据的url = " + urlStr);
        try{
            URL url = new URL(urlStr);
            URLConnection conn = url.openConnection();
            InputStream is = conn.getInputStream();
            String fileName = "/Users/haifeng/Documents/temp/" + symbol + ".csv";
            OutputStream out = new FileOutputStream(fileName);
            byte[] buffer = new byte[2048];
            int length;
            //System.out.println("WangYiDayData 从网上读取的数据为：" + is.read(buffer,0,buffer.length));
            while((length = is.read(buffer,0,buffer.length)) != -1){
                out.write(buffer,0,length);
            }
            Klines ks =  DataFromWangYiCSV.read(fileName);
            File file = new File(fileName);
            if (file.isFile() && file.exists()){
                boolean f = file.delete();
            }
            Collections.sort(ks);
            return ks;
        } catch (IOException e) {
            //网络数据出错，从本地读取
            e.printStackTrace();
        }
        return null;
    }

    public static void combineLostDays(Klines ks) {
        if (ks.size() == 0){
            return;
        }
        Kline lastKline = getLast(ks);
        LocalDateTime today = LocalDateTime.now();
        if (lastKline.getTime().isBefore(today)){
            Klines thirties = SmallFreqKline.minuteKline(getLast(ks).getSymbol(),Frequency.MIN30);
            combineMinute30(thirties);
            for (Kline kline : thirties){
                if(!kline.getTime().isBefore(lastKline.getTime())) {//最后一天之前的，不能添加
                    LocalDateTime newTime = kline.getTime();
                    LocalDateTime lastTime = lastTime(ks);
                    if (newTime.getMonth() == lastTime.getMonth() && newTime.getDayOfMonth() == lastTime.getDayOfMonth()) {
                        combineKline(getLast(ks), kline);
                    } else {
                        ks.add(kline);
                    }
                }
            }
        }
    }
    public static void combineMinute30(Klines klines){
        /*Klines oneMinuteKs = SmallFreqKline.oneMinuteKline(getLast(klines).getSymbol());
        for (Kline kline : oneMinuteKs){
            if(kline.getTime().isAfter(lastTime(klines))) {
                if (canCombineThirtyTime(lastTime(klines), kline.getTime())) {
                    combineWithNewKline(klines, kline);
                } else {
                    klines.add(new Kline(kline));
                }
            }
        }*/
    }
    private static boolean canCombineThirtyTime(LocalDateTime t1,LocalDateTime t2){
        if(t1.getYear() ==t2.getYear() && t1.getMonth() == t2.getMonth() && t1.getDayOfMonth() == t2.getDayOfMonth()) {
            int m1 = t1.getMinute();
            int m2 = t2.getMinute();
            if (t1.getHour() == t2.getHour()) {
                /*if (t1.getHour() == 15){
                    return true;
                }*/
                return (m1 > 0 && m1 <= 30 && m2 > 0 && m2 <= 30) || (m1 > 30 && m2 > 30) || t1.getHour() == 15;
            }else if (t1.getHour() == t2.getHour() - 1){
                return m1 == 59 && m2 == 0;
            }
        }
        return false;
    }
    public static Klines lostFiveMinute(LocalDateTime endTime,String symbol){
        Klines oneMinuteKs = SmallFreqKline.oneMinuteKline(symbol);
        List<Kline> lastKs = new ArrayList<>();
        for(Kline  kline : oneMinuteKs){
            if (kline.getTime().isAfter(endTime)){
                lastKs.add(kline);
            }
        }
        Collections.sort(lastKs);
        //之前的K线时间并不是5分钟的结束点，而下一个K线与之间隔不到5分钟，并且下一个K线也不是新的开始，那就把它添加到之前
        List<List<Kline>> klineList = new ArrayList<>();
        List<Kline> fiveMinuteK = new ArrayList<>();
        //首先分组
        for(Kline kline : lastKs){
            fiveMinuteK.add(kline);
            if(kline.getTime().getMinute() % 5 ==0){
                klineList.add(fiveMinuteK);
                fiveMinuteK = new ArrayList<>();
            }
        }
        List<Kline> combinedKline = new ArrayList<>();
        for(List<Kline> klist : klineList){
            if(klist.size() ==1){
                combinedKline.add(klist.get(0));
            }else if (klist.size() > 1){
                combinedKline.add(combineKline(klist));
            }
        }
        return new Klines(combinedKline);
    }
    //该方法不能用于 完全用1分钟组合5分钟k线
    private static Klines lostFiveMinute(Klines klines,String symbol){
        if(isNull(klines)){
            throw new IllegalArgumentException("klines 不能为空klines");
        }
       Klines combinedKline = lostFiveMinute(getLast(klines).getTime(),symbol);
        for (Kline kline : combinedKline){
            if(canCombineFiveTime(lastTime(klines),kline.getTime())){
                combineWithNewKline(klines,kline);
            }else{
                klines.add(kline);
            }
        }
        return klines;
    }
    private static void combineWithNewKline(Klines klines,Kline kline){
        Kline k = combineKline(getLast(klines),kline);
        if (lastTime(klines).getHour() == 15){//15点后的时间设置为15点
            k.setTime(lastTime(klines));
        }
        klines.set(klines.size() -1,k);
    }
    private static boolean canCombineFiveTime(LocalDateTime t1,LocalDateTime t2){
        if(t1.getYear() ==t2.getYear() && t1.getMonth() == t2.getMonth() && t1.getDayOfMonth() == t2.getDayOfMonth()){
            int i1 = t1.getMinute() / 5;
            int i2 = t2.getMinute() / 5;
            int[][] minutes = {{1,2,3,4,5},{6,7,8,9,10},{11,12,13,14,15},{15,16,18,19,20},{21,22,23,24,25},{26,27,28,29,30},
                    {31,32,33,34,35},{36,37,38,39,40},{41,42,43,44,45},{46,47,48,49,50},{51,52,53,54,55},{56,57,58,59,0}};
            int index1 = -1;
            int index2 = -1;
            for (int i=0;i<minutes.length;i++){
                for(int j = 0;j<minutes[i].length;j++){
                    if(minutes[i][j] == t1.getMinute()){
                        index1 = i;
                    }
                    if (minutes[i][j] == t2.getMinute()){
                        index2 = i;
                    }
                    if (index1 != -1 && index2 != -1){
                        break;
                    }
                }
            }
            if (t1.getHour() == t2.getHour()){
                if (index1 == index2){
                    return true;
                }
                //如果都是3点，那么不管index2等于多少分钟，都可以合并
                if (t2.getHour() == 9){
                    return t1.getMinute() == 30 && t2.getMinute() == 31;
                }else return t1.getHour() == 15;
            }else if (t1.getHour() == t2.getHour() -1){//例如 9点59分和10点在一个时间段、10：59与11点在同一个时间段
                return index1 == index2 && index1 == 11;
            }
        }

        return false;
    }
    /**
     * 用于动态输出最后一个K线的时间，如果为空，就返回1900年
     * @param klines
     * @return
     */
    private static LocalDateTime lastTime(Klines klines){
        if (isNull(klines)){
            return LocalDateTime.of(1900,1,1,0,0,0);
        }
        return getLast(klines).getTime();
    }

    /**
     * 用于动态判断是都为空
     * @param klines 输入的Kline
     * @return 如果为null 或 size = 0，返回true
     */
    private static boolean isNull(Klines klines){
        return klines == null || klines.size() == 0;
    }
    public static Klines getWeekKlines(Klines dayKlines){
        int weeks;
        Klines weekKlines = new Klines();
        weekKlines.add(new Kline(dayKlines.get(0)));
        WeekFields weekField = WeekFields.of(DayOfWeek.MONDAY,1);
        weeks = dayKlines.get(0).getTime().get(weekField.weekOfYear());
        for (int i = 1;i<dayKlines.size();i++){
            LocalDateTime time = dayKlines.get(i).getTime();
            int thisWeek = time.get(weekField.weekOfYear());
            if (thisWeek == weeks){
                weekKlines.set(weekKlines.size() - 1,combineKline(getLast(weekKlines),dayKlines.get(i)));
            }else{
                weeks = thisWeek;
                weekKlines.add(new Kline(dayKlines.get(i)));
            }
        }
        return weekKlines;
    }
    public static Klines getWeekKlines(String symbol,LocalDateTime beginDate,LocalDateTime endDate){
        Klines dayKlines = getDayKlines(symbol,beginDate,endDate);
        return getWeekKlines(dayKlines);
    }
    private static String date(LocalDateTime time){
        int year = time.getYear();
        int month = time.getMonthValue();
        int day = time.getDayOfMonth();
        String date = ""+year;
        if (month > 9){
            date = date + month;
        }else{
            date = date + "0" + month;
        }
        if(day > 9){
            date = date + day;
        }else{
            date = date + "0" + day;
        }
        return date;
    }
    public static void main(String[] args) throws IOException {

        Klines klines = getDayKlines("002707","20200102");
        klines.forEach(com.example.czsc.utils.Utils::println);
    }
    public static Klines getMonthKlines(Klines dayKlines){
        Klines result = new Klines();
        for (Kline kline : dayKlines){
            if (result.size() != 0){
                Kline lastK = getLast(result);
                if (lastK.getTime().getMonth() == kline.getTime().getMonth() ){
                    result.set(result.size()-1,combineKline(lastK,kline));
                }else{
                    result.add(new Kline(kline));
                }
            }else{
                result.add(new Kline(kline));
            }
        }
        return result;
    }
    public static Klines getMonthKlines(String symbol, LocalDateTime dateBegin, LocalDateTime dateEnd) {
        Klines dayKlines = getDayKlines(symbol,dateBegin,dateEnd);
        return getMonthKlines(dayKlines);
    }

    /**
     * @param k1 原始K线，就是开始K线
     * @param k2 结束K线
     * @return 组合K线
     */
    private static Kline combineKline(Kline k1,Kline k2){
        Kline k = new Kline();
        k.setOpen(k1.getOpen());
        k.setClose(k2.getClose());
        k.setLow(Math.min(k1.getLow(), k2.getLow()));
        k.setHigh(Math.max(k1.getHigh(),k2.getHigh()));
        k.setTime(k2.getTime());
        k.setVolume(k1.getVolume() + k2.getVolume());
        k.setExchangeMoney(k1.getExchangeMoney() + k2.getExchangeMoney());
        k.setExchangePercent(k1.getExchangePercent() + k2.getExchangePercent());
        return k;
    }
    private static Kline combineKline(List<Kline> ks){
        Kline kline = new Kline(ks.get(0));
        for (int i=1;i<ks.size(); i++){
            kline = combineKline(kline,ks.get(i));
        }
        return kline;
    }
    public static Klines getYearKlines(Klines monthsKlines){
        Klines result = new Klines();
        for (Kline kline : monthsKlines){
            if (result.size() != 0){
                Kline lastK = getLast(result);
                if (lastK.getTime().getYear() == kline.getTime().getYear() ){
                    result.set(result.size()-1,combineKline(lastK,kline));
                }else{
                    result.add(new Kline(kline));
                }
            }else{
                result.add(new Kline(kline));
            }
        }
        return result;
    }
    public static Klines getYearKlines(String symbol, LocalDateTime dateBegin, LocalDateTime dateEnd) {
        Klines months = getMonthKlines(symbol,dateBegin,dateEnd);
        return getYearKlines(months);
    }
    public static Klines getQuarterKlines(Klines monthsKlines){
        Klines result = new Klines();
        for (Kline kline : monthsKlines){
            if (result.size() != 0){
                Kline lastK = getLast(result);
                //是不同的季度
                if (lastK.getTime().getMonthValue() % 3 ==0 && kline.getTime().getMonthValue() % 3 != 0){
                    result.add(new Kline(kline));
                }else{
                    result.set(result.size()-1,combineKline(lastK,kline));
                }
            }else{
                result.add(new Kline(kline));
            }
        }
        return result;
    }
    public static Klines getQuarterKlines(String symbol, LocalDateTime dateBegin, LocalDateTime dateEnd) {
        Klines months = getMonthKlines(symbol,dateBegin,dateEnd);
        return getQuarterKlines(months);
    }
}
