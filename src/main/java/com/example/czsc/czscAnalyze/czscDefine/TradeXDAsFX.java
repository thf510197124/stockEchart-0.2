package com.example.czsc.czscAnalyze.czscDefine;

import com.example.czsc.czscAnalyze.objects.BI;
import com.example.czsc.czscAnalyze.objects.FX;
import com.example.czsc.czscAnalyze.objects.Mark;
import com.example.czsc.czscAnalyze.objects.XD;

import java.util.*;
import java.util.stream.Collectors;

import static com.example.czsc.czscAnalyze.czscDefine.CzscUtils.getLast;
import static com.example.czsc.utils.Utils.printSplit;
import static com.example.czsc.utils.Utils.println;

/**
 * @FileName: TradeXDAsFX
 * @Author: Haifeng Tong
 * @Date: 2021/5/67:46 上午
 * @Description:
 * @History: 2021/5/6
 */
public class TradeXDAsFX {
    private static LinkedList<FX> originalFxs;
    public static List<XD> getXDFromFxs(List<FX> fxs){
        originalFxs = new LinkedList<>(fxs);
        LinkedList<FX> result = tradeAsFX(fxs);
        printSplit("**");
        println("搜索到的分型有：");
        println(result);
        printSplit("**");
        result = checkFxs(result);//查询间隔，高低之间没有间隔的话，需要删除2个
        return getXDFromCheckFXS(result);
    }
    private static List<XD> getXDFromCheckFXS(LinkedList<FX> results){
        List<XD> xds = new ArrayList<>();
        for(int i=1;i<results.size();i++){
            int first = originalFxs.indexOf(results.get(i-1));
            int last = originalFxs.indexOf(results.get(i));
            XD xd = new XD();
            for (int j=first;j< last;j++){
                BI bi = new BI(originalFxs.get(j),originalFxs.get(j+1));
                xd.addBI(bi);
            }
            xds.add(xd);
        }
        return xds;
    }
    private static LinkedList<FX> checkFxs(LinkedList<FX> fxs){
        LinkedList<FX> results = new LinkedList<>();
        results.add(fxs.removeFirst());
        for (FX fx : fxs){
            printSplit("--");
            println("现在检验" + fx);
            FX lastFx = results.getLast();
            println("前一个fx为：" + lastFx);
            int lastIndex = originalFxs.indexOf(lastFx);
            int index = originalFxs.indexOf(fx);
            if (index -lastIndex >= 3){
                if (fx.getMark() != lastFx.getMark()){//大于3条，可以构成线段，添加一个反向的
                    println("满足3条的不同性质分型，添加");
                    results.add(fx);
                }else{//大于3条，但是方向相同，
                    if (fx.getMark() == Mark.G){
                        if (fx.getFx() > lastFx.getFx()){//移动顶分型
                            results.removeLast();
                            results.add(fx);
                            println("满足3条，但是都是顶分型，进行替代");
                        }else{
                            println("满足3条，但是都是顶分型，不变");
                        }
                    }else{
                        if (fx.getFx() < lastFx.getFx()){//移动底分型
                            results.removeLast();
                            results.add(fx);
                            println("满足3条，但是都是底分型，进行替代");
                        }else{
                            println("满足3条，但是都是底分型，不变");
                        }
                    }
                }
            }else{//小于3条，只能移动
                if (fx.getMark() == Mark.G && lastFx.getMark() == Mark.G){
                    if (fx.getFx() > lastFx.getFx()){
                        results.removeLast();
                        results.add(fx);
                        println("小于3条，都是顶分型，移动");
                    }else{
                        println("小于3条，都是顶分型，不移动");
                    }
                }else if (fx.getMark() == Mark.D && lastFx.getMark() == Mark.D){
                    if (fx.getFx() < lastFx.getFx()){
                        results.removeLast();
                        results.add(fx);
                        println("小于3条，都是底分型，移动");
                    }else{
                        println("小于3条，都是底分型，不移动");
                    }
                }
            }
        }
        return results;
    }
    private static LinkedList<FX> tradeAsFX(List<FX> fxs){
        printSplit("___");
        println("像寻找分型一样，寻找选段短点");
        LinkedList<FX> result = new LinkedList<FX>();
        List<FX> dFX = fxs.stream().filter(fx -> fx.getMark()== Mark.D).collect(Collectors.toList());
        println("所有的底分型有");
        println(dFX);
        List<FX> gFX = fxs.stream().filter(fx -> fx.getMark()==Mark.G).collect(Collectors.toList());
        println("所有的顶分型有");
        println(gFX);
        for(int i = 0;i<dFX.size(); i++){
            if (i == 0){
                if(dFX.get(i).getFx() < dFX.get(i+1).getFx()){
                    result.add(dFX.get(i));
                }
            }else if (i == dFX.size() -1){
                if(dFX.get(i).getFx() < dFX.get(i-1).getFx()){
                    result.add(dFX.get(i));
                }
            } else if (dFX.get(i).getFx() < dFX.get(i - 1).getFx() && dFX.get(i).getFx() < dFX.get(i + 1).getFx()) {
                result.add(dFX.get(i));
            }
        }

        for (int i =0;i<gFX.size();i++){
            if(i == 0){
                if(gFX.get(i).getFx() > gFX.get(i+1).getFx()){
                    result.add(gFX.get(i));
                }
            }else if(i == gFX.size() -1){
                if(gFX.get(i).getFx() > gFX.get(i-1).getFx()){
                    result.add(gFX.get(i));
                }
            }else if (gFX.get(i).getFx() > gFX.get(i - 1).getFx()
                    && gFX.get(i).getFx() > gFX.get(i + 1).getFx()) {
                result.add(gFX.get(i));
            }
        }
        result.sort((f1,f2)->{
            if (f1.getTime().isBefore(f2.getTime())){
                return -1;
            }else if(f1.getTime().isAfter(f2.getTime())){
                return 1;
            }else{
                return 0;
            }
        });
        printSplit("___");
        return result;
    }
}
