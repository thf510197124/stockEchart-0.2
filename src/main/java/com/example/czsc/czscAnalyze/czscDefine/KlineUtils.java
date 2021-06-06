package com.example.czsc.czscAnalyze.czscDefine;


import com.example.czsc.common.Bar;
import com.example.czsc.common.Kline;
import com.example.czsc.common.Klines;
import com.example.czsc.czscAnalyze.objects.Direction;
import com.example.czsc.czscAnalyze.objects.FX;

import java.util.ArrayList;
import java.util.List;

import static com.example.czsc.czscAnalyze.czscDefine.CzscUtils.getLast;
import static com.example.czsc.czscAnalyze.objects.Direction.DOWN;
import static com.example.czsc.czscAnalyze.objects.Direction.UP;
import static com.example.czsc.utils.Utils.println;

/**
 * @FileName: BarUtils
 * @Author: Haifeng Tong
 * @Date: 2021/3/17:17 下午
 * @Description: 处理K线的工具类
 * @History:
 */
class KlineUtils {
    /*K线的处理：
     * 1、包含关系的K线，向上时，把两K线的最高点当高点，而两个K线中较高者，当成低点。反之
     * 向下时，两根K线最低点当成低点，两个K线的高点中的较高者，当成高点。
     * 2、K线的包含关系符合顺序原则，第1.2含有包含关系，处理后，再与第三根比，
     * 如果第三根与处理有的有包含关系，那再处理。
     * 3、假如第N根K线满足N与N+1的包含关系，而第N与N-1不是包含关系，那么Gn>= Gn-1
     * 那么N-1，N，N+1是向上的；如果Dn<Dn-1，那么称第N-1，N，N-2为向下的。
     * 如果 gn<gn-1 且 dn>dn-1，那就是包含关系
     *
     * K 线包含关系的处理，可能会导致特殊情况的极端高低点的消失，例如600619的2018年11月14日到11月25日之间。
     * 上涨线段中，高点之前有下跌。
     * * 这种情况可能经常出现
      * * */
    public static Klines updateBar(Klines ks){ //包含关系的处理的小错误在低价股，低时间级别的K线中会表现得很明显
        Klines newKs = handleLine(ks);
        //newKs.getBars().forEach(Utils::println);
        List<Kline> bars;
        int x;
        if(newKs.size() < 3){
            return null;
        }else{
            bars = new ArrayList<>();
            bars.add(new Kline(newKs.get(0)));
            x = 1;
            while(x < newKs.size() && contains(getLast(bars),newKs.get(x))){
                //如果第1根K线与后面的K线有包含关系，这时候难以解决方向问题，
                // 所以前面的含有包含关系的，直接跳过，直到遇到没有包含关系的；
                x++;
            }
        }
        for (int i = x;i< newKs.size();i++){
            Kline preBar =null;//preBar 就是 n - 1 K线
            if (bars.size() >1){
                preBar = bars.get(bars.size()-2);
            }
            Kline lastBar = getLast(bars);//就是 n 的K线
            Direction direction = getDirection(lastBar,preBar);
            Kline curr = newKs.get(i);//就是 n + 1的K线
            float curH = curr.getHigh(),curL = curr.getLow();//当前K线的高点和低点
            float lastH = lastBar.getHigh(),lastL = lastBar.getLow();//前一根K线的高点和低点
            if((curH <= lastH && curL>= lastL) || (curH >= lastH && curL<= lastL)){ //如果有包含关系
                bars.remove(lastBar); //把之前的一个移除
                if (curH <= lastH && curL >= lastL){//curH被包含，时间设置为之前的时间
                    curr.setTime(lastBar.getTime());
                }
                if (direction == UP){
                    curr.setHigh(Math.max(lastH,curH));
                    curr.setLow(Math.max(lastL,curL));
                }else{
                    curr.setHigh(Math.min(lastH,curH));
                    curr.setLow(Math.min(lastL,curL));
                }
                if (curr.getOpen() >= curr.getClose()){ //保持K线的颜色
                    curr.setOpen(curr.getHigh());
                    curr.setClose(curr.getLow());
                }else{
                    curr.setOpen(curr.getLow());
                    curr.setClose(curr.getHigh());
                }
            }
            bars.add(new Kline(curr));
        }
        return new Klines(bars);
    }

    /**
     * 鉴于不给出方向，经常会在判断中出现上涨笔中，取不到最高抵，下跌中取不到最高点的问题，所以通过调用直接给出方向，
     * 这种方式的一个难点是，如何随时给出方向，是不是进行循环判断
     * 比如进行顶分型判断，找到顶分型，那么就返回经过处理的，从ks中移除这个时间点之前的K线，下次按向下放心进行处理？
     * 但是这个又产生一个问题，分型的判断是一个很复杂的问题。当下确定的底分型，可能并不是一个合格的底分型。在后续的处理中被删掉，所有这又产生一个问题
     * 或者，根据处理后的分型，来最包含关系进行重新处理，对分型进行修正，这样分型基本可以确保包含了最高点和最低点
     * 但是感觉这样处理又没什么意义、所有该方法先抛弃了。
     * @param ks
     * @param fXes
     * @return
     */
    @Deprecated
    public static List<Kline> updateFXs(Kline ks, List<FX> fXes){
        return null;
    }
    private static Direction getDirection(Kline k1,Kline k2){ // k1为第N根K线，而K2 为N - 1K线；
        Direction direction;
        if (k2 == null){ //之前没有K线，就根据K1是上涨还是下跌判断方向
            if (k1.getClose() >= k1.getOpen()){
                direction = UP;
            }else{
                direction = DOWN;
            }
        }else if(k1.getHigh() >= k2.getHigh()){
            direction = UP;
        }else if(k1.getLow() <= k2.getLow()){
            direction = DOWN;
        }else{//如果这两根K线有包含关系，还是判断不出方向，那就根据K1的涨跌判断
            if (k1.getClose() >= k1.getOpen()){
                direction = UP;
            }else{
                direction = DOWN;
            }
        }
        return direction;
    }
    private static boolean contains(Kline k1,Kline k2){
        return (k1.getHigh() > k2.getHigh() && k1.getLow() < k2.getLow()) || (k1.getHigh() < k2.getHigh() && k1.getLow() > k2.getLow());
    }
    //保持把带上下阴线的K线改为实体K线
    //把一字K线去掉
    private static Klines handleLine(Klines kines){
        Klines newKlines = new Klines();
        for(int i =0;i < kines.size();i++){
            Kline bar = new Kline(kines.get(i));
            if(bar.getClose() >= bar.getOpen()){
                bar.setClose(bar.getHigh());
                bar.setOpen(bar.getLow());
            }else{
                bar.setClose(bar.getLow());
                bar.setOpen(bar.getHigh());
            }
            if(i > 0){//当前K线的高点低与之前的K线完全相同
                Bar prevBr = new Kline(kines.get(i-1));
                if(bar.getHigh() == prevBr.getHigh() && bar.getLow() == prevBr.getLow()){
                    continue;
                }
            }
            if(bar.getClose() == bar.getOpen()){
                //如果当前K线是一字K线，那么它是前一条K线的高低点，或者是后一条K线的高低点，那就跳过，不添加到Ks中；
                // 保留跳空的一字K线
                if(i > 0){
                    Bar prevBr = new Kline(kines.get(i-1));
                    if(prevBr.getHigh() == bar.getClose() || prevBr.getLow() == bar.getClose()){
                        continue;
                    }
                }
                if(i < kines.size()-1){
                    Bar nextBr = new Kline(kines.get(i+1));
                    if(nextBr.getHigh() == bar.getClose() || nextBr.getLow() == bar.getClose()){
                        continue;
                    }
                }
            }
            newKlines.add(bar);
        }
        return newKlines;
    }
}
