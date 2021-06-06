package com.example.czsc.czscAnalyze.objects;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.czsc.czscAnalyze.czscDefine.CzscUtils.getLast;
import static com.example.czsc.utils.Utils.println;


/**
 * @FileName: XD
 * @Author: Haifeng Tong
 * @Date: 2021/2/278:39 上午
 * @Description: 线段
 * @History:
 */
public class XD extends BI{
    public static final int UNCERTAIN = 0;//未确定
    public static final int CERTAIN = 1; //确定状态
    public static final int UNWANTED = 2;

    //如果status是false，表面这条线断还是一条未确认线段
    private int status = UNCERTAIN;
    private List<BI> bis;
    private Center center;
    public XD(){
        this.bis = new ArrayList<>();
        this.status = UNCERTAIN;
    }
    public XD(List<BI> bis) {
        this.bis = bis;
        this.status = UNCERTAIN;
        this.center = new Center(bis,true);
    }
    public XD(XD xd){
        this.setHigh(xd.getHigh());
        this.setLow(xd.getLow());
        this.setBeginTime(xd.getBeginTime());
        this.setEndTime(xd.getEndTime());
        this.setFx_begin(xd.getFx_begin());
        this.setFx_end(xd.getFx_end());
        this.setStatus(xd.getStatus());
        this.setDirection(xd.getDirection());
        this.setBis(xd.getBis());
    }
    public List<BI> getBis() {
        return bis;
    }

    public void setBis(List<BI> bis) {
        this.bis = bis;
    }

    public static XD createXD(BI bi){
        XD xd = new XD();
        ArrayList<BI> bis = new ArrayList<>(Collections.singleton(bi));
        xd.setBis(bis);
        xd.status = UNCERTAIN;
        xd.center = new Center(bis,true);
        return xd;
    }
    public static XD createXD(List<BI> bis){
        XD xd = new XD();
        xd.setBis(bis);
        xd.setCenter(bis);
        return xd;
    }

    @Override
    public Direction getDirection() {
        if (this.bis == null){
            return null;
        }
        return this.bis.get(0).getDirection();
    }

    @Override
    public float getLow() {
        if (getDirection() == Direction.UP){
            return this.bis.get(0).getLow();
        }
        return getLast(this.bis).getLow();
    }

    @Override
    public float getHigh() {
        if (getDirection() == Direction.UP){
            return getLast(this.bis).getHigh();
        }
        return this.bis.get(0).getHigh();
    }

    @Override
    public FX getFx_begin() {
        return this.bis.get(0).getFx_begin();
    }


    @Override
    public FX getFx_end() {
        return getLast(this.bis).getFx_end();
    }

    @Override
    public LocalDateTime getBeginTime() {
        return getFx_begin().getTime();
    }

    @Override
    public LocalDateTime getEndTime() {
        return getFx_end().getTime();
    }

    public Center getCenter() {
        return center;
    }

    public void setCenter(List<BI> bis) {
        this.center = new Center(bis,true);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static BI getLastAspectBI(XD xd){
        assert xd.getBis().size() > 2;
        BI lastBI = getLast(xd.getBis());
        if(lastBI.getDirection() != xd.getDirection()){
            return lastBI;
        }else{
            return xd.getBis().get(xd.getBis().size() - 2);
        }
    }
    public BI getLastAspectBI(){
        return getLastAspectBI(this);
    }
    public void addBI(BI bi){
        this.getBis().add(bi);
    }
    public void addBis(List<BI> bis){
        this.getBis().addAll(bis);
    }

    @Override
    public String toString() {
        return "XD{" +
                ", beginTime=" + getBeginTime() +
                ", endTime=" + getEndTime() +
                ", direction=" + getDirection() +
                ", high=" + getHigh() +
                ", low=" + getLow() +
                ",BI ="+ getBis() +
                '}';
    }
}
