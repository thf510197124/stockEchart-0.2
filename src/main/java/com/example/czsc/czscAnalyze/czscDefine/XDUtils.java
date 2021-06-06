package com.example.czsc.czscAnalyze.czscDefine;


import com.example.czsc.common.Kline;
import com.example.czsc.common.Klines;
import com.example.czsc.czscAnalyze.objects.*;
import com.example.czsc.dataSystem.urlRes.SmallFreqKline;
import com.example.czsc.utils.Utils;
import com.github.abel533.echarts.code.X;
import org.python.modules.itertools.count;

import java.time.LocalDateTime;
import java.util.*;

import static com.example.czsc.czscAnalyze.czscDefine.CzscUtils.getLast;
import static com.example.czsc.czscAnalyze.czscDefine.CzscUtils.hasGap;
import static com.example.czsc.czscAnalyze.objects.Direction.DOWN;
import static com.example.czsc.czscAnalyze.objects.Direction.UP;
import static com.example.czsc.czscAnalyze.objects.XD.CERTAIN;
import static com.example.czsc.utils.Utils.println;

/**
 * @FileName: XDUtils
 * @Author: Haifeng Tong
 * @Date: 2021/3/17:20 下午
 * @Description:
 * @History:
 */
class XDUtils {
    private static List<Kline> globalBars;
    private static final List<XD> results = new ArrayList<>();
    private static boolean notFinished = true;
    private static List<BI> bis = null;
    private static boolean isCurrentWithUp = true;
    private static XD xd = new XD();
    private static int count = 0;
    private static Direction beginDirection;
    private static float useInGapAspectPoint;
    private static void init(Klines bars) throws Exception {
        //初始化---------------------------------------------------------------------
        List<FX> fxs;
        if (getFrequency().getMinutes() > 1440 ) {
            fxs = FxUtils.simpleFx(Objects.requireNonNull(KlineUtils.updateBar(bars)),true,bars);
        }else{
            fxs = FxUtils.divideFx(bars,true);
        }
        bis = checkBI(BIUtils.divideBi(fxs));
        init(bars,fxs,bis);

    }
    private static void init(Klines originalBars, List<FX> fies, List<BI> fromBis) {
        //初始化---------------------------------------------------------------------
        globalBars = originalBars;
        results.clear();
        notFinished = true;
        useInGapAspectPoint= Float.NaN;//初始方向错误，则可能导致全局错误
        isCurrentWithUp = fies.get(0).getMark() != Mark.G;
        if (fies.size() < 4) {
            notFinished = false;
        }
        bis = checkBI(fromBis);
        if (bis.size() <3){
            notFinished = false;
        }
        beginDirection = isCurrentWithUp ? UP : DOWN;
    }
    public static List<XD> getXDFromKlineByOrder(Klines bars,LocalDateTime startTime) throws Exception {
        if (startTime == null){
            return getXDFromKlineByOrder(bars);
        }
        List<Kline> copyBars = new ArrayList<>(bars);
        for (Kline bar : copyBars){
            if (bar.getTime().isBefore(startTime)){
                bars.remove(bar);
            }
        }
        return getXDFromKlineByOrder(bars);
    }
    public static List<XD> getXDFromKlineByOrder(Klines bars) throws Exception {
        init(bars);
        return getXDAfterInit();
    }
    public static List<XD> getXDFromFXAndXD(Klines bars,List<FX> fies,List<BI> bis ){
        if (fies == null || fies.size() == 0 || bis == null || bis.size() == 0){
            println(fies != null ? fies.size() : "fies 为空");
            return null;
        }
        init(bars,fies,bis);
        return getXDAfterInit();
    }
    private static List<XD> getXDAfterInit(){
        //因为方向错误，可能导致分析完全错误，所以增加一个相反方向的循环
        while(notFinished && count < 2){
            if (results.size() > 0) isCurrentWithUp = getLast(results).getDirection() != UP;
            if (isCurrentWithUp){
                xd = handleUpXD();
            }else{
                xd = handleDownXD();
            }
            if(isUsefulXD(xd)) {
                //删除处理过的BI
                removeBis(xd);//挪到这里处理，是为了提高handleFirstTypeXDUp的使用范围
                trimGlobalBars(xd);//删除处理过的bar
                results.add(new XD(xd));
                //这样会不会导致之前的xd为空？？？？？？？？？？？？？？？？？
                xd = new XD();//重新初始化线段 //这样会不会导致之前的xd为空？？？？？？？？？？？？？？？？？
            }else{
                notFinished = false;
            }
        }
        return results;
    }
    private static XD handleUpXD() {
        List<BI> aspectBis = new ArrayList<>();
        float highest = Float.MIN_VALUE;
        int highIndex=0;
        List<BI> noContainedAspectBis;
        for (int i = 0; i < bis.size(); i++) {
            BI bi = bis.get(i);
            if (bi.getDirection() == UP){
                if (bi.getHigh() > highest){
                    highest = bi.getHigh();
                    highIndex = i;
                    if (i >= 2){ //找到了一个新的高点
                        aspectBis.clear();
                        aspectBis.add(bis.get(i-1));//添加特征序列1
                    }
                }
            }else{
                if (i > highIndex){
                    aspectBis.add(bi);
                }
            }
            if (aspectBis.size() > 2) {
               noContainedAspectBis = handleContainAspectBI(aspectBis);//处理包含关系。处理包含关系是，不能把后面的都处理了
                println(noContainedAspectBis);
            }else{
                continue;
            }
            if (i >= highIndex + 3 && noContainedAspectBis.size() >= 3){//对先对的第一种情况进行判断，出现了笔破坏
                BI aspect1 = noContainedAspectBis.get(0);//设置的第一根特征K线是高点之前的一根
                BI aspect2 = noContainedAspectBis.get(1);//第二根特征K线是高点之后的一根
                if (hasGap(aspect1,aspect2)){
                    println("当前特征笔1 = " + aspect1);
                    println("与当前特征笔2 形成缺口，特征笔2 = " + aspect2);
                    useInGapAspectPoint = aspect1.getHigh();
                    xd = handleSecondTypeXDUp(bis,highIndex,noContainedAspectBis);
                    if(isUsefulXD(xd)){
                        useInGapAspectPoint=Float.NaN;
                        println("第二类上涨线段形成 xd = " + xd);
                    }else{
                        println("第二类上涨线段形成失败XXXXXXXXXXXXXXXXXXXXXXXXXx");
                    }
                }else{//第一类线段
                    println("当前特征选段1 = " + aspect1);
                    println("与当前特征线段2 没有形成缺口，特征线段2 = " + aspect2);
                    xd = handleFirstTypeXDUp(bis,highIndex,noContainedAspectBis);
                    if(isUsefulXD(xd)){
                        println("第一类上涨线段形成 xd = " + xd);
                    }else{
                        println("第一类上涨线段形成失败XXXXXXXXXXXXXXXXXXXXXXXXXx");
                    }
                }
                if (isUsefulXD(xd)){
                    return xd;
                }
            }
        }
        notFinished = false;
        return xd;
        //return handleRetainedBI(bis, newAspectBis, highIndex, count);
    }
    private static XD handleFirstTypeXDUp(List<BI> bis,int highIndex,List<BI> aspectBis){
        if (gaoFX(aspectBis)){
            println("第一类上涨，形成顶分型，分型的三笔为：");
            println(aspectBis.get(0));
            println(aspectBis.get(1));
            println(aspectBis.get(2));
            xd.addBis(bis.subList(0,highIndex + 1));
            xd.setStatus(CERTAIN);
        }
        return xd;
    }

    //按照线段的定义，后面划分底分型时，是需要重新计算特征序列的，底分型的特征序列向上
    private static XD handleSecondTypeXDUp(List<BI> bis,int highIndex,List<BI> aspectBis) {
        List<BI> newXDAspectBis = new ArrayList<>();
        //两根特征K线形成缺口
        //后面的特征K线形成底分型
        if (gaoFX(aspectBis)) {
            println("上涨中的第二类形成了顶分型，判断是否形成底分型");
            //------很明显这里判断错误了
            for (int i = highIndex + 1 ;i<bis.size();i++){
                if (bis.get(i).getDirection() == UP) {//后面的取得的特征线段方向为上涨
                    newXDAspectBis.add(bis.get(i));
                }
            }
            println("新的特征线段：");
            println(newXDAspectBis);
            newXDAspectBis = handleContainAspectBI(newXDAspectBis);
            println("经过处理后新的特征线段----------------~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-------------------");
            println(newXDAspectBis);
            for (int i=0;i < newXDAspectBis.size()-2;i++){
                if (diFX(newXDAspectBis.subList(i,i+3))){
                    println("这里可能出问题，因为是假设一直找下去的，可能已经形成了很多条线段了，才形成一个底分型");
                    println("当前形成底分型的特征线段为：");
                    println(newXDAspectBis.get(i));
                    println(newXDAspectBis.get(i+1));
                    println(newXDAspectBis.get(i+2));
                    xd.addBis(bis.subList(0,highIndex + 1));
                    break;
                }
            }
        }
        return xd;
    }
    private static boolean gaoFX(List<BI> aspectBis){
        BI bi1 = aspectBis.get(0);//第一根特征K线
        BI bi2 = aspectBis.get(1);//第二根特征K线
        BI bi3 = aspectBis.get(2);
        return bi2.getHigh() > bi1.getHigh() && bi2.getHigh() > bi3.getHigh();
    }
    private static boolean diFX(List<BI> aspectBis){
        BI bi1 = aspectBis.get(0);//第一根特征K线
        BI bi2 = aspectBis.get(1);//第二根特征K线
        BI bi3 = aspectBis.get(2);
        return bi1.getLow() > bi2.getLow() && bi2.getLow() < bi3.getLow();
    }
    private static XD handleDownXD() {
        List<BI> aspectBis = new ArrayList<>();
        float lowest = Float.MAX_VALUE;
        int lowIndex=0;
        List<BI> noContainedAspectBis;
        for (int i = 0; i < bis.size(); i++) {
            BI bi = bis.get(i);
            if (bi.getDirection() == DOWN){
                if (bi.getLow() < lowest){
                    lowest = bi.getLow();
                    lowIndex = i;
                    if (i >= 2){ //找到了一个新的低点
                        aspectBis.clear();
                        aspectBis.add(bis.get(i-1));//添加特征序列1
                    }
                }
            }else{
                if (i > lowIndex) {
                    aspectBis.add(bi);
                }
            }
            if (aspectBis.size() > 2) {
                noContainedAspectBis = handleContainAspectBI(aspectBis);//处理包含关系。处理包含关系是，不能把后面的都处理了
            }else{
                continue;
            }
            //i >= lowIndex + 3,就是在低点后至少走出3笔，才能看清楚是都构成线段
            //包含关系的处理太早，可能把第一类线段，处理成了第二类线段，
            if (i >= lowIndex + 3 && noContainedAspectBis.size() >= 3){
                BI aspect1 = noContainedAspectBis.get(0);//设置的第一根特征K线是低点之前的一根
                BI aspect2 = noContainedAspectBis.get(1);//第二根特征K线是低点之后的一根
                if (hasGap(aspect1,aspect2)){
                    println("当前特征选段1 = " + aspect1);
                    println("与当前特征线段2 形成缺口，特征线段2 = " + aspect2);
                    useInGapAspectPoint = aspect1.getLow();
                    xd = handleSecondTypeXDDown(bis,lowIndex,noContainedAspectBis);
                    if(isUsefulXD(xd)){
                        useInGapAspectPoint = Float.NaN;
                        println("第二类下跌段形成 xd = " + xd);
                    }else{
                        println("第二类下跌线段形成失败XXXXXXXXXXXXXXXXXXXXXXX");
                    }
                }else{//第一类线段
                    println("当前特征选段1 = " + aspect1);
                    println("与当前特征线段2 没有形成缺口，特征线段2 = " + aspect2);
                    xd = handleFirstTypeXDDown(bis,lowIndex,noContainedAspectBis);
                    if(isUsefulXD(xd)){
                        println("第一类下跌线段形成 xd = " + xd);
                    }else{
                        println("第一类下跌线段形成失败XXXXXXXXXXXXXXXXXXXX");
                    }
                }
                if (isUsefulXD(xd)){
                    return xd;
                }
            }

        }
        notFinished = false;
        return xd;
        //--return handleRetainedBI(bis, aspectBis, lowIndex, count);
    }
    //这里怎么处理了有缺口的情况？
    private static XD handleFirstTypeXDDown(List<BI> bis,int lowIndex,List<BI> aspectBis){
        if (diFX(aspectBis)){
            xd.addBis(bis.subList(0,lowIndex + 1));
            xd.setStatus(CERTAIN);
        }
        return xd;
    }
    private static XD handleSecondTypeXDDown(List<BI> bis,int lowIndex,List<BI> noContainedBis){
        List<BI> newXDAspectBis = new ArrayList<>();
        if (diFX(noContainedBis)) {
            for (int i = lowIndex;i<bis.size();i++){
                if (bis.get(i).getDirection() == DOWN) {
                    newXDAspectBis.add(bis.get(i));
                }
            }
            newXDAspectBis = handleContainAspectBI(newXDAspectBis);
            for (int i=0;i<newXDAspectBis.size()-2;i++){
                if (gaoFX(newXDAspectBis.subList(i,i+3))){
                    xd.addBis(bis.subList(0,lowIndex + 1));
                    break;
                }
            }
        }
        return xd;
    }

    private  static Frequency getFrequency(){
        Frequency freq1 = Frequency.frequency(globalBars.get(0).getTime(),globalBars.get(1).getTime());
        Frequency freq2 = Frequency.frequency(globalBars.get(1).getTime(),globalBars.get(2).getTime());
        return Frequency.frequency(Math.min(freq1.getMinutes(),freq2.getMinutes()));
    }

    private static void removeBis(XD xd){
        LocalDateTime time = xd.getEndTime();
        List<BI> beforeTime = new ArrayList<>();
        for (BI bi : bis){
            if (bi.getEndTime().isBefore(time.plusSeconds(1))){
                beforeTime.add(bi);
            }
        }
        bis.removeAll(beforeTime);
    }

    private static void trimGlobalBars(XD xd){//删除globals中用不到的K线
        LocalDateTime time = xd.getEndTime();
        for (int i = 1;i<globalBars.size()-1;i++){
            if (globalBars.get(i).getTime().isBefore(time.plusSeconds(1)) && globalBars.get(i+1).getTime().isAfter(time)){
                globalBars = globalBars.subList(i-1,globalBars.size()-1);
                return;
            }
        }
    }

    private static boolean isUsefulXD(XD xd){
        return xd != null && xd.getBis().size() != 0;
    }
    //这种处理方式，只能处理第一类的线段的包含关系

    private static List<BI> handleContainAspectBI(List<BI> aspectBis){
        LinkedList<BI> newBis = new LinkedList<>();
        newBis.add(aspectBis.get(0));//0为高点或者低点之前的一条，所以不用处理包含关系
        //引用类型，同属分属两个容器时，改变其中的一个，另一个也同时改变，所以必须重新创建
        newBis.add(new BI(aspectBis.get(1)));
        for (int i =2;i<aspectBis.size();i++){
            BI thisBI = aspectBis.get(i);
            BI prevBI = newBis.removeLast();
            //后包含
            if (thisBI.getHigh() <= prevBI.getHigh() && thisBI.getLow()>=prevBI.getLow()){
                if(thisBI.getDirection()==UP){//代表判断的线段是下跌的
                    if (!Float.isNaN(useInGapAspectPoint)){//判断缺口

                    }
                    prevBI.setHigh(thisBI.getHigh());
                }else{
                    prevBI.setLow(thisBI.getLow());
                }
                prevBI.setEndTime(thisBI.getEndTime());
                newBis.add(prevBI);
            }
            //前包含
            else if(thisBI.getHigh() >= prevBI.getHigh() && thisBI.getLow()<= prevBI.getLow()){
                if(thisBI.getDirection() == UP){

                }
            }
            else{
                newBis.add(new BI(prevBI));
                newBis.add(new BI(thisBI));
            }
        }
        return newBis;
    }
    /**
     * 因为bis是从最低点或者最高点开始的，所以如果同在1笔，必然是在开头
     * @param bis
     * @return
     */
    private static List<BI> checkBI(List<BI> bis){
        BI lowBI = null;
        BI highBI = null;
        for (BI bi : bis){
            if (lowBI == null){
                lowBI = bi;
                highBI = bi;
            }
            if (bi.getHigh() > highBI.getHigh()){
                highBI = bi;
            }else if(bi.getLow() < lowBI.getLow()){
                lowBI = bi;
            }
        }
        if (lowBI == highBI && highBI == bis.get(0)){
            return bis.subList(1,bis.size());
        }else{
            return bis;
        }
    }
    public static void main(String[] args) throws Exception {
        String path = "/Users/haifeng/Documents/stock_database/trading163/002707.csv";
        //Klines klines = DataFromWangYiCSV.read(path, LocalDateTime.of(2018, 1, 1, 0, 0, 0));
        Klines klines = SmallFreqKline.minuteKline("002707",Frequency.MIN5);
        List<XD> xds = getXDFromKlineByOrder(klines,LocalDateTime.of(2018, 1, 1, 0, 0, 0));
        if(xds.size() > 0) {
            //关于南京银行的判断，出了一些问题，看来是需要进行一些修正
            //应该在getXDFromKlineByOrder中轮流的进行判断。
            // 如果已经添加了一个上涨的，那么下一个就判断下跌的，
            // 如果下一个发现了更高的点，且下跌不构成线段，那么就认为之前的上涨判断错误，对上一个上涨进行修正
            xds.forEach(Utils::println);
        }
    }
}
