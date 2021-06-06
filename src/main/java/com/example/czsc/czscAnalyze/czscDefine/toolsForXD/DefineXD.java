package com.example.czsc.czscAnalyze.czscDefine.toolsForXD;

import com.example.czsc.common.Interval;
import com.example.czsc.czscAnalyze.objects.BI;
import com.example.czsc.czscAnalyze.objects.Center;
import com.example.czsc.czscAnalyze.objects.Direction;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.czsc.czscAnalyze.czscDefine.toolsForXD.IntervalRelationship.UNCONTAIN_DOWN;
import static com.example.czsc.czscAnalyze.czscDefine.toolsForXD.IntervalRelationship.UNCONTAIN_UP;

/**
 * @FileName: DefineXD
 * @Author: Haifeng Tong
 * @Date: 2021/5/22:21 下午
 * @Description:
 * @History: 2021/5/2
 */
public class DefineXD {
    private List<BI> bis;

    public DefineXD(List<BI> bis) {
        this.bis = bis;
    }
    public DefineXD(List<BI> bis,Direction direction){
        this(bis);
    }

    private List<BI> oddNumberBis(){
        return bis.stream()
                .filter(bi->bis.indexOf(bi) % 2 == 1)
                .collect(Collectors.toList());
    }
    public DefineXD addBI(BI bi1){
        this.bis.add(bi1);
        return this;
    }
    public List<BI> getBis() {
        return bis;
    }

    public Direction getDirection() {
        return bis.get(0).getDirection();
    }

    public List<Interval> getCenters() {
        List<Interval> centers = new ArrayList<>();
        for(int i=0;i<bis.size()-2;i++){
            Center center = new Center(bis.get(i),bis.get(i+1),bis.get(i+2));
            Center.Region region = center.getRecentRegion();
            if(center.getRecentRegion() != null){
                centers.add(center.getRecentRegion());
            }
        }
        return centers;
    }
    public List<Interval> filterCenter(){
        List<Interval> centers = getCenters();
        List<Interval> newIntervals = new ArrayList<>();
        for(int i=1;i< centers.size();i++){
            IntervalRelationship ship = IntervalRelationship.getRelationship(centers.get(i-1),centers.get(i));
            if(ship == UNCONTAIN_UP || ship == UNCONTAIN_DOWN){
                newIntervals.add(centers.get(i-1));
            }
        }
        return null;
    }



    public List<BI> getAspectBis() {
        return bis.stream()
                .filter(bi->bis.indexOf(bi) % 2 ==0)
                .collect(Collectors.toList());
    }

    public void setBis(List<BI> bis) {
        this.bis = bis;
    }



    public enum ErrorType{
        TOO_SHORT_BIS,//数量太少
        NUMBER_EVEN,//bis数量为偶数
        BAD_START,//开始点不是最低最高点
        DIRECTION_CHAOS,//方向混乱
        UNCERTAIN,
        SUCCESS;
    }

}
