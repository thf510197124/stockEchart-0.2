package com.example.czsc.czscAnalyze.czscDefine;


import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MAType;
import com.tictactec.ta.lib.MInteger;
import com.example.czsc.common.Bar;
import com.example.czsc.common.Kline;
import com.example.czsc.common.Klines;

import java.io.IOException;
import java.util.*;

import static com.example.czsc.czscAnalyze.czscDefine.CzscUtils.getLast;
import static com.example.czsc.utils.Utils.println;


/**
 * @FileName: Average
 * @Author: Haifeng Tong
 * @Date: 2021/3/265:27 下午
 * @Description:
 * @History:
 */
public class TaUtils {
    private static LinkedList<Float> MA = new LinkedList<>();
    private static float sum = 0;
    private static Core core = new Core();
    public static List<Float> SMA(List<Kline> kLines, int timePeriod){
        float[] close = closeList(kLines);
        return SMA(close,timePeriod);
    }
    public static List<Float> SMA(float[] close,int timePeriod){
        MA.clear();
        sum = 0;
        List<Float> ma = new ArrayList<>();//暂时保存的ma
        if(close.length < timePeriod){
            return null;
        }
        for (int i =0;i<close.length;i++){
            if (Float.isNaN(close[i])){
                close[i] = 50;//计算KDJ时会用到
            }
            MA.add(close[i]);
            sum += close[i];
            if(i < timePeriod-1){
                ma.add(Float.NaN);
                //ma.add(close[i]);
            }else{
                ma.add(sum/timePeriod);
                try {
                    sum = sum - MA.removeFirst();
                }catch(NullPointerException e){
                    throw new RuntimeException(e);
                }
            }
        }
        return ma;
    }
    public static List<Float> EMA(List<Kline> kLines,int timePeriod){
        float[] close = closeList(kLines);
        return EMA(close,timePeriod);
    }
    public static List<Float> EMA(List<Kline> kLines){
        return EMA(kLines,5);
    }
    public static List<Float> EMA(float[] close,int timePeriod){
        List<Float> ema = new ArrayList<>();
        for(int i=0;i<close.length;i++){
            if(i==0){
                ema.add(close[i]);
            }else{
                float c = (2 * close[i] + ema.get(i-1) * (timePeriod-1)) / (timePeriod + 1);
                ema.add(c);
            }
        }
        return ema;
    }
    public static List<Float> EMA(float[] close){
        return EMA(close,5);
    }
    public static MACD MACD(List<Kline> kLines,int fastPeriod,int slowPeriod,int signalPeriod){
        float[] close = closeList(kLines);
        return MACD(close,fastPeriod,slowPeriod,signalPeriod);
    }
    public static List<SingleMACD> singleMACDList(Klines klines, int fastPeriod, int slowPeriod, int signalPeriod){
        MACD macd = MACD(TaUtils.closeList(klines),fastPeriod,slowPeriod,signalPeriod);
        List<SingleMACD> list = new ArrayList<>();
        for(int i=0;i<klines.size();i++){
            SingleMACD s = new SingleMACD(klines.get(i),macd.getDiff().get(i),macd.getDea().get(i),macd.getMacd().get(i));
            list.add(s);
        }
        return list;
    }

    public static MACD MACD(float[] close, int fastPeriod, int slowPeriod, int signalPeriod){
        List<Float> emaFast = EMA(close,fastPeriod);
        List<Float> emaSlow = EMA(close,slowPeriod);
        List<Float> diff = new ArrayList<>();

        for(int i=0;i< emaFast.size();i++){
            diff.add(emaFast.get(i) - emaSlow.get(i));
        }
        float[] diffArray = new float[diff.size()];
        for(int i=0;i<diffArray.length;i++){
            diffArray[i]=diff.get(i);
        }
        List<Float> dea = EMA(diffArray,signalPeriod);
        List<Float> macd = new ArrayList<>();
        for(int i=0;i<diff.size();i++){
            macd.add((diff.get(i) - dea.get(i))*2);
        }
        return new MACD(diff,dea,macd);
    }
    private static List<Float> timesList(List<Float> list,int times){
        for(int i=0;i<list.size();i++){
            list.set(i,list.get(i)*times);
        }
        return list;
    }
    public static List<SingleMACD> MACDFromTA(Klines klines, int fastPeriod, int slowPeriod, int signalPeriod){
        MACD macd = MACDFromTA(TaUtils.closeList(klines),fastPeriod,slowPeriod,signalPeriod);
        List<SingleMACD> list = new ArrayList<>();
        for(int i=0;i<klines.size();i++){
            SingleMACD s = new SingleMACD(klines.get(i),macd.getDiff().get(i),macd.getDea().get(i),macd.getMacd().get(i));
            list.add(s);
        }
        return list;
    }
    public static MACD MACDFromTA(float[] close,int fastPeriod,int slowPeriod,int signalPeriod ){
        double[] diff = new double[close.length];//因为timeperiod之前的数据都是0
        double[] dea = new double[close.length];
        double[] macd = new double[close.length];
        MInteger m1 = new MInteger();
        m1.value = 0;
        MInteger m2 = new MInteger();
        m2.value = close.length;
        core.macd(0,close.length-1,close,fastPeriod,slowPeriod,signalPeriod,m1,m2,diff,dea,macd);
        LinkedList<Float> diffList = new LinkedList<>();
        LinkedList<Float> deaList = new LinkedList<>();
        LinkedList<Float> macdList = new LinkedList<>();
        for (int i=0;i<close.length;i++){
            diffList.add((float)diff[i]);
            deaList.add((float)dea[i]);
            macdList.add((float)macd[i]);
        }
        while (getLast(diffList)==0){
            diffList.removeLast();
            diffList.addFirst(Float.NaN);
        }
        while(getLast(deaList) == 0){
            deaList.removeLast();
            deaList.addFirst(Float.NaN);
        }
        while(getLast(macdList)==0){
            macdList.removeLast();
            macdList.addFirst(Float.NaN);
        }
        //处理前面的天数，MACD为空的问题

        return new MACD(diffList,deaList,macdList);
    }
    //我的计算结果与同花顺、东方财富相同
    public static KDJ KDJ(float[] close, float[] high, float[] low){
        List<Float> K = new ArrayList<>();
        List<Float> D = new ArrayList<>();
        List<Float> J = new ArrayList<>();
        List<Float> rsv = RSV(close,high,low,9);
        for(int i =0;i<rsv.size();i++){
            float k_;
            float d_;
            if (i<9){
                k_ = rsv.get(i);
                d_ = k_;
            }else{
                k_ = (2f / 3) * K.get(i-1) + (1f / 3) *rsv.get(i);
                d_ = (2f/3)*D.get(i-1) + (1f/3)*k_;
            }
            K.add(k_);
            D.add(d_);
            J.add(3*k_ - 2*d_);
        }
        /*K = SMA(turnListToArray(rsv),3);
        assert K != null;
        D= SMA(turnListToArray(K),3);
        for (int i = 0;i<K.size();i++){
            assert D != null;
            J.add(3*K.get(i) - 2 * D.get(i));
        }*/
        return new KDJ(K,D,J);
    }
    //与系统计算结果不同
    public static KDJ kdjFromTA(float[] close,float[] high,float[] low){
        int fastK_period = 9;
        int slowK_period = 3;
        int slowD_period = 3;

        double[] K = new double[close.length-fastK_period-slowK_period];
        println(K.length);
        double[] D = new double[close.length-fastK_period-slowK_period];
        List<Float> klist = new ArrayList<>();
        List<Float> dlist = new ArrayList<>();
        for(int i= 0;i < fastK_period+slowK_period; i++){
            klist.add(Float.NaN);
            dlist.add(Float.NaN);
        }
        println("K = " + klist);
        MInteger m1 = new MInteger();
        m1.value = 0;
        MInteger m2 = new MInteger();
        m2.value = close.length;
        //默认参数设置，9，3，3
        core.stoch(0,close.length-1,
                high,low,close,
                fastK_period,slowK_period,MAType.Sma,
                slowD_period,MAType.Sma,m1,m2,K,D);
        for (int i=0;i<K.length;i++){
            klist.add((float)K[i]);
            dlist.add((float)D[i]);
        }
        return new KDJ(klist,dlist,minus(klist,3,dlist,2));
    }

    /**
     *与系统数据近似，但是稍有差别
     * @param close
     * @param timePeriod 默认为20
     * @param nonBiasedUp 默认为2
     * @return
     */
    public static BOLL BOLLFromTa(float[] close,int timePeriod,int nonBiasedUp){
        //Core core = new Core();
        double[] upper = new double[close.length-timePeriod+1];//因为timeperiod之前的数据都是0
        double[] lower = new double[close.length-timePeriod+1];
        double[] middle = new double[close.length-timePeriod+1];
        List<Float> upperList = new ArrayList<>();
        List<Float> middleList = new ArrayList<>();
        List<Float> lowerList = new ArrayList<>();
        MInteger m1 = new MInteger();
        m1.value = 0;
        MInteger m2 = new MInteger();
        m2.value = close.length;
        core.bbands(0,close.length-1,close,20,
                nonBiasedUp,nonBiasedUp, MAType.Sma,m1,m2,upper,middle,lower);
        for(int i= 0;i<timePeriod;i++){
            upperList.add(Float.NaN);
            middleList.add(Float.NaN);
            lowerList.add(Float.NaN);
        }
        for (int i = 0;i< upper.length;i++){
            upperList.add((float) upper[i]);
            middleList.add((float)middle[i]);
            lowerList.add((float)lower[i]);
        }
        return new BOLL(upperList,middleList,lowerList);
    }
    private static List<Float> RSV(float[] close,float[] high,float[] low){
        return RSV(close,high,low,9);
    }
    private static List<Float> RSV(float[] close,float[] high,float[] low,int timePeriod){
        List<Float> highest = new ArrayList<>();
        List<Float> lowest = new ArrayList<>();
        List<Float> rsv = new ArrayList<>();
        for(int i=0;i<close.length;i++){
            float[] h_,l_;
            if(i<timePeriod){
                h_= subArray(high,0,i);
                l_ = subArray(low,0,i);
            }else{
                h_ = subArray(high,i-8,i);
                l_ = subArray(low,i-8,i);
            }
            highest.add(max(h_));
            lowest.add(min(l_));
            rsv.add(highest.get(i).equals(lowest.get(i)) ? 0 : (close[i] - lowest.get(i)) / (highest.get(i) - lowest.get(i)) * 100);
        }
        return rsv;
    }
    public static class MACD{
        private List<Float> diff;
        private List<Float> dea;
        private List<Float> macd;

        public MACD(List<Float> diff, List<Float> dea, List<Float> macd) {
            this.diff = diff;
            this.dea = dea;
            this.macd = macd;
        }

        public List<Float> getDiff() {
            return diff;
        }

        public List<Float> getDea() {
            return dea;
        }

        public List<Float> getMacd() {
            return macd;
        }
    }
    public static class SingleMACD {
        private Kline kline;
        private float diff;
        private float dea;
        private float macd;

        public SingleMACD(Kline kline, float diff, float dea, float macd) {
            this.kline = kline;
            this.diff = diff;
            this.dea = dea;
            this.macd = macd;
        }

        public Kline getKline() {
            return kline;
        }

        public void setKline(Kline kline) {
            this.kline = kline;
        }

        public float getDiff() {
            return diff;
        }

        public void setDiff(float diff) {
            this.diff = diff;
        }

        public float getDea() {
            return dea;
        }

        public void setDea(float dea) {
            this.dea = dea;
        }

        public float getMacd() {
            return macd;
        }

        public void setMacd(float macd) {
            this.macd = macd;
        }
    }
    public static class KDJ{
        List<Float> k;
        List<Float> d;
        List<Float> j;

        public KDJ(List<Float> k, List<Float> d, List<Float> j) {
            this.k = k;
            this.d = d;
            this.j = j;
        }

        public List<Float> getK() {
            return k;
        }

        public List<Float> getD() {
            return d;
        }

        public List<Float> getJ() {
            return j;
        }
    }
    public static class BOLL{
        List<Float> upper = new ArrayList<>();
        List<Float> middle = new ArrayList<>();
        List<Float> lower = new ArrayList<>();

        public BOLL(List<Float> upper, List<Float> middle, List<Float> lower) {
            this.upper = upper;
            this.middle = middle;
            this.lower = lower;
        }

        public List<Float> getUpper() {
            return upper;
        }

        public List<Float> getMiddle() {
            return middle;
        }

        public List<Float> getLower() {
            return lower;
        }
    }

    public static float[] closeList(List<Kline> klass){
        float[] close = new float[klass.size()];
        for(int i=0;i<klass.size();i++){
            close[i] = klass.get(i).getClose();
        }
        return close;
    }
    private static float[] highList(List<Bar> kLines){
        float[] high = new float[kLines.size()];
        for(int i=0;i<kLines.size();i++){
            high[i] = kLines.get(i).getHigh();
        }
        return high;
    }
    private static float[] lowList(List<Bar> kLines){
        float[] low = new float[kLines.size()];
        for(int i=0;i<kLines.size();i++){
            low[i] = kLines.get(i).getLow();
        }
        return low;
    }
    //包含最后一个元素
    private static float[] subArray(float[] data,int start,int end){
        float[] c = new float[end - start+1];
        for(int i = start;i< end+1;i++){
            c[i-start] = data[i];
        }
        return c;
    }
    public static float max(float[] data){
        float max = Float.MIN_VALUE;
        for(float d : data){
            if(d > max){
                max = d;
            }
        }
        return max;
    }
    public static float min(float[] data){
        float min = Float.MAX_VALUE;
        for(float d : data){
            if(d < min){
                min = d;
            }
        }
        return min;
    }
    private static float[] turnListToArray(List<Float> list){
        float[] f = new float[list.size()];
        for(int i=0;i<list.size();i++){
            f[i] = list.get(i);
        }
        return f;
    }
    private static float[] turnDoubleToFloat(double[] doubles){
        List<Float> fs = new ArrayList<Float>();
        for(Double d : doubles){
            fs.add(d.floatValue());
        }
        return turnListToArray(fs);
    }
    private static float[] minus(float[] f1,float timesF1,float[] f2,float timesF2){
        assert f1.length==f2.length;
        float[] result = new float[f1.length];
        for(int i=0;i<f1.length;i++){
            if (Float.isNaN(f1[i])){
                f1[i]=0;
            }
            if(Float.isNaN(f2[i])){
                f2[i]=0;
            }
            result[i] = f1[i] * timesF1-f2[i] * timesF2;
        }
        return result;
    }
    private static List<Float> minus(List<Float> f1,float timesF1,List<Float> f2,float timesF2){
        assert f1.size()==f2.size();
        List<Float> result = new ArrayList<>();
        for(int i=0;i<f1.size();i++){
            if (Float.isNaN(f1.get(i))){
                f1.set(i,0f);
            }
            if(Float.isNaN(f2.get(i))){
                f2.set(i,0f);
            }
            result.add(f1.get(i) * timesF1 - f2.get(i) * timesF2);
        }
        return result;
    }
    private static Float[] floatBox(float[] f1){
        Float[] F = new Float[f1.length];
        for(int i = 0;i<f1.length;i++){
            F[i] = f1[i];
        }
        return F;
    }
    public static void main(String[] args) throws IOException {
        float[] closes ={35.98f,44.65f};
        println("EMA12 = " + EMA(closes,12));
        println("EMA26 = " + EMA(closes,26));
        float diff2 = (EMA(closes,12).get(1) - EMA(closes,26).get(1));
        float[] diff = {0,diff2};
        List<Float> dea = EMA(diff,9);
        println("diff2 = " + diff2);
        println("dea2 = " + dea.get(1));
        println("macd = " + (diff2 - dea.get(1))*2);
    }
}
