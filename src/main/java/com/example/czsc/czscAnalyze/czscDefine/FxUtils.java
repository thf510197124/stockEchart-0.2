package com.example.czsc.czscAnalyze.czscDefine;

import com.example.czsc.common.Bar;
import com.example.czsc.common.Kline;
import com.example.czsc.common.Klines;
import com.example.czsc.czscAnalyze.objects.FX;
import com.example.czsc.czscAnalyze.objects.Frequency;
import com.example.czsc.czscAnalyze.objects.Mark;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.example.czsc.czscAnalyze.czscDefine.CzscUtils.getLast;
import static com.example.czsc.utils.Utils.println;


/**
 * @FileName: FxUtils
 * @Author: Haifeng Tong
 * @Date: 2021/3/17:18 下午
 * @Description: FX 工具类
 * @History:
 */
public class FxUtils {
    public static FX createFX(Kline bar1, Kline bar2, Kline bar3, Mark mark){
        FX fx = new FX();
        fx.setTime(bar2.getTime());
        fx.setBars(Arrays.asList(bar1,bar2,bar3));
        if(mark == Mark.D){
            fx.setLow(bar2.getLow());
            fx.setFx(fx.getLow());
            fx.setHigh(Math.max(Math.max(fx.getBars().get(0).getHigh(),fx.getBars().get(2).getHigh()),fx.getBars().get(1).getHigh()));
            fx.setMark(Mark.D);
        }else{
            fx.setHigh(bar2.getHigh());
            fx.setFx(fx.getHigh());
            fx.setLow(Math.min(Math.min(fx.getBars().get(0).getLow(),fx.getBars().get(2).getLow()),fx.getBars().get(1).getLow()));
            fx.setMark(Mark.G);
        }
        return fx;
    }
    // 判断是否构成分型
    public static Mark confirmMark(Kline k1,Kline k2,Kline k3){
        if (k1.getHigh() < k2.getHigh() && k2.getHigh() > k3.getHigh())
        {
            return Mark.G;
        }else if(k1.getLow() > k2.getLow() && k2.getLow() < k3.getLow()){
            return Mark.D;
        }else {
            //分型移动
            return null;
        }
    }
    public static FX createFX(Kline k1,Kline k2,Kline k3){
        if (confirmMark(k1,k2,k3) == Mark.G){
            return createFX(k1,k2,k3,Mark.G);
        }else if(confirmMark(k1,k2,k3) == Mark.D){
            return createFX(k1,k2,k3,Mark.D);
        }else{
            return null;
        }
    }
    /**
     * 用于跟原有的笔更新笔，确保新的K线序列包含之前笔中的K线。
     * @param fxs 原有分笔
     * @param kSeries K线序列
     * @return 新的分笔
     * @throws Exception K线序列必须包含原有的K线
     */
    public static List<FX> updateFX(List<FX> fxs, Klines kSeries) throws Exception {
        if (fxs.size() == 0){
            return divideFx(kSeries,true);
        }
        FX lastFx = getLast(fxs);
        Kline lastBar = getLast(lastFx.getBars());
        //第一步，先把K线过滤一下，把新的K线挑出来。
        Klines copyKlines = new Klines(kSeries);
        for (Kline k : copyKlines){
            if(k.getTime().isBefore(lastBar.getTime())){
                kSeries.remove(k);
            }
        }
        //如果之后的没有包含前面一根，那就把前面一根加上。
        if (kSeries.get(0).getTime().isAfter(lastFx.getTime())){
            kSeries.add(0,lastBar);
        }
        List<Kline> newBars = KlineUtils.updateBar(kSeries);
        if(newBars == null){
            return fxs;
        }
        return divideFX(fxs,new Klines(newBars),kSeries);
    }
    // 针对大周期，比如日线、周线、月线等，只要构成分型就可以
    public static List<FX> simpleFx(boolean isNew,Klines klines){
        Klines newBars = new Klines();
        return simpleFx(newBars,false,klines);
    }

    public static List<FX> simpleFx(@NotNull List<Kline> newBars, boolean isNew,Klines klines){
        if (!isNew){
            newBars = KlineUtils.updateBar(new Klines(newBars));
        }
        List<FX> fies = new ArrayList<>();
        int startIndex = getStartIndex(klines); //从K线的最低点或者最高点开始分析,之前的K线不用分析
        assert newBars != null;
        if(startIndex  == 0){
            FX fx = new FX(newBars.get(0),newBars.get(1));
            fies.add(fx);
        }
        for(int i = 1;i < newBars.size() -1;i++){
            FX fx = createFX(newBars.get(i-1),newBars.get(i),newBars.get(i+1));
            if(fx != null){
                if(fies.size() == 0){
                    fies.add(fx);
                }else{
                    FX lastFX = getLast(fies);
                    if(lastFX.getMark() == fx.getMark()){
                        if(lastFX.getMark() == Mark.D && fx.getFx() <= lastFX.getFx()
                                || lastFX.getMark() == Mark.G && fx.getFx() >= lastFX.getFx()){
                            fies.remove(lastFX);
                            fies.add(fx);
                        }
                    }else{
                        if (fx.getMark() == Mark.D) {
                            //fx为底分型
                            //使用新笔的定义，在大周期上，不能使用太严格的分笔
                            if (fx.getBars().get(1).getHigh() < lastFX.getBars().get(1).getLow()) {
                                //顶和底的核心一笔不重合
                                fies.add(fx);
                            }
                        } else {
                            //fx为顶分型，lastFX为底分型
                            if (fx.getBars().get(1).getLow() > lastFX.getBars().get(1).getHigh()) {
                                fies.add(fx);
                            }
                        }
                    }
                }
            }
        }
        return fies;
    }

    private static List<FX> adjustFxLoop(List<FX> fies,List<Kline> newBars){
        boolean needAdjust = true;
        List<FX> original = new ArrayList<>(fies);
        List<FX> result = adjustFX(original,newBars);
        while(needAdjust){
            needAdjust = false;
            original = new ArrayList<>(result);
            result = adjustFX(original,newBars);
            for (int i= 0;i<result.size();i++){//调整，直到两者相同
                if (original.get(i) != result.get(i)){
                    needAdjust = true;
                    break;
                }
            }
        }
        return result;
    }
    /**
     *
     * @param oldFX 原有的分型
     * @param newBars 经过包含关系处理的K线
     * @param originalBars 没有经过包含关系处理的K线
     * @return 添加新的分型
     */
    private static List<FX> divideFX(@Nullable List<FX> oldFX, Klines newBars,Klines originalBars){
        if (oldFX == null){
            oldFX = new ArrayList<>();
        }
        for(int i = 1;i < newBars.size() -1;i++){
            FX fx = createFX(newBars.get(i-1),newBars.get(i),newBars.get(i+1));
            if(fx != null){
                if(oldFX.size() == 0){
                    oldFX.add(fx);
                }else{
                    FX lastFX = getLast(oldFX);
                    if(lastFX.getMark() == fx.getMark()){//分型的移动替代
                        if(lastFX.getMark() == Mark.D && fx.getFx() < lastFX.getFx()
                                || lastFX.getMark() == Mark.G && fx.getFx() > lastFX.getFx()) {
                            //例如，当前的低点更低，但是两个低点之间，有个更高的点，却因为距离lastFX距离不够，而没有添加成功。
                            oldFX.remove(lastFX);
                            //删除前一个后，可能再前一个需要更新？
                            //测试一下往前回溯
                            oldFX.add(fx);
                        }
                    }else {//前后分型不相同，看看是不是能够添加
                        //获取前一个分型最后一根K线在处理过K线中的位置
                        int index = newBars.indexOf(getLast(lastFX.getBars()));
                        Kline k1 = findBarByTime(originalBars, getLast(lastFX.getBars()).getTime());//前一个分型的最后一根
                        Kline k2 = findBarByTime(originalBars, newBars.get(i).getTime());//后一个分型的第一根
                        assert k1 != null;
                        assert k2 != null;
                        int gap = Math.abs(originalBars.indexOf(k1) - originalBars.indexOf(k2));
                        Kline k = lastFX.getBars().size() == 3 ? lastFX.getBars().get(1) : lastFX.getBars().get(0);
                        //i为当前的顶点，index为上一个的最后一条
                        if (i - index > 2) { //顶分型的顶K线不能与底分型的底K线有重合
                            if (lastFX.getMark() == Mark.G) {
                                //fx为底分型
                                if (newBars.get(i).getHigh() < k.getLow()) {//底分型中间一根K线的高点，低于顶分型中间一根K线的低点
                                    //底的最低点，至少要低于顶的最低点
                                    //底的最高点，要低于顶的最高点
                                    oldFX.add(fx);
                                }
                            } else {
                                //fx为顶分型，lastFX为底分型
                                if (newBars.get(i).getLow() > k.getHigh()) {
                                    oldFX.add(fx);
                                }
                            }
                        } else if ((i - index > 1 && gap > 2) || (i - index > 0 && gap >3) )  {
                            if (lastFX.getMark() == Mark.G) {
                                if (newBars.get(i).getHigh() < k.getLow()) {//底的最低点，至少要低于顶的最低点
                                    oldFX.add(fx);
                                }
                            } else {
                                if (newBars.get(i).getLow() > k.getHigh()) {
                                    oldFX.add(fx);
                                }
                            }
                        }
                    }
                }
            }
        }
        if(oldFX.size() > 0){
            //有一种情况需要解决，就是最高点和最低点时相连的情况
            return adjustFxLoop(oldFX,newBars);
        }
        return null;
    }

    /**
     * 处理后，可能会有间隔不符合要求，或者低点在高点中，或者高点在低点中
     * @param oldFX
     * @param newBars
     * @return
     */
    private static List<FX> adjustFX(List<FX> oldFX,List<Kline> newBars) {
        for (int i=1;i<oldFX.size() - 1;i++){
            FX current = oldFX.get(i);
            int prevIndex = getFXPosition(newBars,oldFX.get(i-1));//确定的是分型的中间一根的具体位置
            int nextIndex = getFXPosition(newBars,oldFX.get(i+1));
            Bar currentBar = current.getBars().get(1);
            int needChange = -1;
            if (current.getMark() == Mark.G){
                if (prevIndex >= 0  && nextIndex > 0){
                    for (int j = prevIndex+ 2;j < nextIndex-1;j++){//不是从前面的底开始到后面的底结束，因为要至少保留一根不共用的
                        if (newBars.get(j).getHigh() > currentBar.getHigh()){
                            currentBar = newBars.get(j);
                            needChange = j;
                        }
                    }
                }
            }else{
                if (prevIndex >= 0 && nextIndex > 0){
                    for (int j = prevIndex + 2;j < nextIndex-1;j++){
                        if (newBars.get(j).getLow() <  currentBar.getLow()){
                            currentBar = newBars.get(j);
                            needChange = j;
                        }
                    }
                }
            }
            if (needChange > 0){
                FX newFX= createFX(newBars.get(needChange-1),newBars.get(needChange),newBars.get(needChange+1));
                if(newFX != null && newFX.getMark() == current.getMark()){
                    oldFX.set(i,new FX(newBars.get(needChange-1),newBars.get(needChange),newBars.get(needChange+1)));
                }
            }
        }
        return oldFX;
    }
    private static int getFXPosition(List<Kline> newBars,FX fx){
        for (int j=0;j<newBars.size();j++){
            if(newBars.get(j).getTime() == fx.getTime()){
                return j;
            }
        }
        return -1;
    }

    public static List<FX> divideFXUseNewBar(@Nullable Klines newBars,boolean needFindStartIndex,Klines bars){
        return getFxes(newBars, needFindStartIndex, bars);
    }

    private static List<FX> getFxes(@Nullable Klines newBars, boolean needFindStartIndex, @NotNull Klines bars) {
        if(newBars== null){
            if(bars == null || bars.size() == 0){
                return null;
            }
            newBars = KlineUtils.updateBar(bars);
        }
        assert newBars != null;
        if(newBars.size() < 10){
            return null;
        }
        int startIndex;
        List<FX> fies= null;
        if(needFindStartIndex) {
            startIndex = getStartIndex(newBars); //从K线的最低点或者最高点开始分析,之前的K线不用分析
            //可能会遇到第一条K线就是最高点或者最低点的情况，这时候可能就完全混乱了
            if(startIndex  == 0){
                FX fx = new FX(newBars.get(0),newBars.get(1));
                fies = new ArrayList<>();
                fies.add(fx);
            }
        }else{
            startIndex = 1;
        }
        if(startIndex >=1){
            startIndex = startIndex - 1;//可能会错过第一个分析点
        }

        return divideFX(fies,new Klines(newBars.subList(startIndex,newBars.size())),bars);
    }

    /**
     * 划分分型 ，严格分笔
     * @param bars :输入K线
     * @return FX
     */
    public static List<FX> divideFx(Klines bars,boolean needFindStartIndex){
        assert bars.size() > 0;//该方法有问题，导致划分出来的线段有问题
        Collections.sort(bars);
        Klines newBars = new Klines(KlineUtils.updateBar(bars));
        return getFxes(newBars, needFindStartIndex, bars);
    }
    private static Kline findBarByTime(Klines bars,LocalDateTime time){
        for (int i=0;i< bars.size();i++){
            if (bars.get(i).getTime() == time){
                return bars.get(i);
            }
            if (i < bars.size() - 1){
                if (bars.get(i).getTime().isBefore(time) && bars.get(i+1).getTime().isAfter(time)){
                    return bars.get(i);
                }
            }
        }
        return null;
    }
    //配合在线段中的使用
    public static int getStartIndex(Klines bars){
        int highIndex=0,lowIndex=0;
        float high=Float.MIN_VALUE,low=Float.MAX_VALUE;
        for (int i=0;i<bars.size();i++){
            Bar bar = bars.get(i);
            if (bar.getHigh() > high) {
                high=bar.getHigh();//同一根K线可能同时包含低点和高点
                highIndex = i;
            }
            if(bar.getLow() < low) {
                low = bar.getLow();
                lowIndex = i;
            }
        }
        return Math.min(highIndex, lowIndex);
    }
}
