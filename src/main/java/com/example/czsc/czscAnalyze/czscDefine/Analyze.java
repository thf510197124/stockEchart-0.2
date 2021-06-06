package com.example.czsc.czscAnalyze.czscDefine;

import com.example.czsc.common.Klines;
import com.example.czsc.czscAnalyze.czscDefine.FxUtils;
import com.example.czsc.czscAnalyze.czscDefine.KlineUtils;
import com.example.czsc.czscAnalyze.objects.BI;
import com.example.czsc.czscAnalyze.objects.FX;
import com.example.czsc.czscAnalyze.objects.Frequency;
import com.example.czsc.czscAnalyze.objects.XD;
import com.sun.istack.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.czsc.utils.Utils.println;

/**
 * @FileName: Analyze
 * @Author: Haifeng Tong
 * @Date: 2021/5/112:42 下午
 * @Description:
 * @History: 2021/5/1
 */
public class Analyze {
    private final Frequency frequency;
    private final Klines klines;
    public Analyze(Klines klines) {
        this.klines = klines;
        this.frequency = Frequency.getFrequency(klines);
    }
    public Analyze(Klines klines, Frequency frequency){
        this.klines = klines;
        this.frequency = frequency;
    }
    public Klines getKlines() {
        return klines;
    }
    public Klines newKlines(){
        return KlineUtils.updateBar(klines);
    }
    public List<FX> getFX(){
        if (newKlines() == null || newKlines().size() < 10){
            return null;
        }
        if (frequency.getMinutes() > 1440){
            return FxUtils.simpleFx(newKlines(),true,klines);
        }else{
            return FxUtils.divideFXUseNewBar(newKlines(),true,klines);
        }
    }
    public @Nullable List<FX> getSimpleFX(){
        return FxUtils.simpleFx(newKlines(),true,klines);
    }
    public @Nullable List<FX> getNormalFX(){
        //最后再进行一遍过滤，过滤去那些距离太近的
        return FxUtils.divideFXUseNewBar(newKlines(),false,klines);
    }
    //根据frequency确认获取哪类BI
    public @Nullable List<BI> getBIS() throws Exception {
        return  BIUtils.divideBi(getFX());
    }

    public static List<BI> getBIS(List<FX> fxs) throws Exception {
        return  BIUtils.divideBi(fxs);
    }
    public @Nullable List<BI> getNormalBIS() throws Exception {
        return  BIUtils.divideBi(getNormalFX());
    }
    public @Nullable List<BI> getSimpleBIS() throws Exception {
        return BIUtils.divideBi(getSimpleFX());
    }

    public @Nullable List<XD> getXD() throws Exception {
        if(getBIS() != null) {
            //return NewXDUtils.splitXDSWithBI(getBIS());
            return NNewXDUtils.splitXDSWithBI(getBIS());
            //return TradeXDAsFX.getXDFromFxs(getNormalFX());
        }
        else
            return null;
        //return XDUtils.getXDFromFXAndXD(klines,getFX(),getBIS());
    }
    public @Nullable List<XD> getNormalXD() throws Exception {
        return XDUtils.getXDFromFXAndXD(klines,getSimpleFX(),getSimpleBIS());
    }
    public @Nullable List<XD> getSimpleXD() throws Exception {

        return XDUtils.getXDFromFXAndXD(klines, getSimpleFX(),getSimpleBIS());
    }
}
