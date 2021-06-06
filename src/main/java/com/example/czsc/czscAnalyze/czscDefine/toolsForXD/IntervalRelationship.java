package com.example.czsc.czscAnalyze.czscDefine.toolsForXD;

import com.example.czsc.common.Interval;

/**
 * @FileName: CenterRelationship
 * @Author: Haifeng Tong
 * @Date: 2021/5/21:25 下午
 * @Description:
 * @History: 2021/5/2
 */
public enum IntervalRelationship {
    INCLUDEAFTER,//前面包含后面
    ISCONTAINED,//后面包含前面
    UP,
    DOWN,
    THISNONE,
    AFTERNONE,
    UNCONTAIN_UP,
    UNCONTAIN_DOWN,
    EQUAL;
    /**
     *
     * @param interval1 center1的时间在center2之前
     * @param interval2 center1的时间在center2之前
     * @return 前者相比后边的关系
     */
    public static IntervalRelationship getRelationship(Interval interval1, Interval interval2){
        if(interval1 == null){
            return THISNONE;
        }
        if(interval2 == null){
            return AFTERNONE;
        }
        if(interval1.getHigh() == interval2.getHigh() && interval2.getLow() == interval2.getLow()){
            return EQUAL;
        }
        if (interval1.getHigh() > interval2.getHigh()){
            if (interval1.getLow() > interval2.getLow()){
                if (interval1.getLow() > interval2.getHigh()){
                    return UNCONTAIN_DOWN;
                }
                return DOWN;
            } else{
                return INCLUDEAFTER;
            }
        }else{
            if (interval1.getLow() > interval2.getLow()){
                return ISCONTAINED;
            } else{
                if(interval2.getLow() > interval1.getHigh()){
                    return UNCONTAIN_UP;
                }
                return UP;
            }
        }
    }
    public static Interval getIntersetRegion(Interval interval1, Interval interval2){
        IntervalRelationship ship = getRelationship(interval1,interval1);
        switch(ship){
            case THISNONE:
            case INCLUDEAFTER:
                return interval2;
            case AFTERNONE:
            case ISCONTAINED:
                return interval1;
            case UP:
                if (!(interval2.getLow() > interval1.getHigh())) {
                    interval2.setHigh(interval1.getHigh());
                }
                return interval2;
            case DOWN:
                if (!(interval2.getHigh() < interval1.getLow())) {
                    interval2.setLow(interval1.getLow());
                }
                return interval2;
            default:
                return null;
        }
    }

}
