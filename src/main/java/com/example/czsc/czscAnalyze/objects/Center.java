package com.example.czsc.czscAnalyze.objects;

import com.example.czsc.common.Interval;
import com.example.czsc.common.Klines;
import com.sun.istack.Nullable;

import javax.transaction.NotSupportedException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.example.czsc.czscAnalyze.czscDefine.CzscUtils.getLast;
import static com.example.czsc.utils.Utils.printSplit;
import static com.example.czsc.utils.Utils.println;

/**
 * @FileName: Center
 * @Author: Haifeng Tong
 * @Date: 2021/5/111:06 上午
 * @Description:
 * @History: 2021/5/1
 */
public class Center {
    private List<? extends BI> bis;
    private boolean isXD;
    public Center() {
    }
    public Center(List<? extends BI> bis,boolean isXD) {
        this.bis = bis;
        this.isXD = isXD;
    }
    public Center(BI bi1,BI bi2,BI bi3){
        List<BI> b = new ArrayList<>();
        b.add(bi1);
        b.add(bi2);
        b.add(bi3);
        this.bis = b;
    }

    public List<? extends BI> getBis() {
        return bis;
    }

    public void setBis(List<? extends BI> bis) {
        this.bis = bis;
    }

    public boolean isXD() {
        return isXD;
    }

    public void setXD(boolean XD) {
        isXD = XD;
    }

    /**
     * region尽可能多的K线的中枢
     * @return
     */
    public Region getRecentRegion(){
        Region region = null;
        float low,high;
        float lowest = Float.NaN;
        float highest = Float.NaN;
        for (int i = 1;i< bis.size()-1; i++){
            BI prev = bis.get(i-1);
            BI bi = bis.get(i);
            BI next = bis.get(i+1);
            lowest = Math.min(prev.getLow(), next.getLow());
            highest = Math.max(prev.getHigh(), next.getHigh());
            low = Math.max(prev.getLow(),next.getLow());
            high = Math.min(prev.getHigh(),next.getHigh());
            if (high > low){
                if(region != null){
                    float newH = Math.min(high,region.getHigh());
                    float newLow = Math.max(low,region.getLow());
                    if (newH > newLow){//中枢的移动
                        region.setLow(newLow);
                        region.setHigh(newH);
                        region.setHighest(Math.max(highest, region.getHighest()));
                        region.setLowest(Math.min(lowest, region.getLowest()));
                        region.addBi(prev);
                        region.addBi(next);
                        region.addBi(bis.get(i));
                        continue;
                    }else{//如果跳空，不能漏过这一笔。
                        region = null;
                        i = i -1;
                    }
                }
                region = new Region();
                region.setXD(this.isXD());
                region.setLow(low);
                region.setHigh(high);
                region.setLowest(lowest);
                region.setHighest(highest);
                region.addBi(prev);
                region.addBi(next);
                region.addBi(bis.get(i));
            }
        }
        return region;
    }
    public List<Region> getRegionList() throws NotSupportedException {
        if (!isXD()){
            throw new NotSupportedException("不支持非线段多中枢查找");
        }
        List<Region> regions = new ArrayList<>();
        Region region = null;
        float low,high;
        float lowest = Float.NaN;
        float highest = Float.NaN;
        for (int i = 1;i< bis.size()-1; i++){
            if (regions.size() == 0){
                region = new Region();
            }else{
                region = getLast(regions);
            }
            BI prev = bis.get(i-1);
            BI next = bis.get(i+1);
            lowest = Math.min(prev.getLow(), next.getLow());
            highest = Math.max(prev.getHigh(), next.getHigh());
            low = Math.max(prev.getLow(),next.getLow());
            high = Math.min(prev.getHigh(),next.getHigh());
            if (high > low){
                if(region != null){//判断是中枢的移动，还是产生新中枢
                    float newH = Math.min(high,region.getHigh());
                    float newLow = Math.max(low,region.getLow());
                    if (newH > newLow){//中枢区间的缩小
                        region.setLow(newLow);
                        region.setHigh(newH);
                        region.setHighest(Math.max(highest, region.getHighest()));
                        region.setLowest(Math.min(lowest, region.getLowest()));
                        region.addBi(prev);
                        region.addBi(next);
                        region.addBi(bis.get(i));
                        continue;
                    }
                }
                Region newRegion = new Region();
                newRegion.setXD(this.isXD());
                newRegion.setLow(low);
                newRegion.setHigh(high);
                newRegion.setLowest(lowest);
                newRegion.setHighest(highest);
                newRegion.addBi(prev);
                newRegion.addBi(next);
                newRegion.addBi(bis.get(i));
                regions.add(newRegion);
            }
        }
        return regions;
    }
    public static class Region implements Interval {
        private float low;
        private float high;
        private float lowest;
        private float highest;
        private boolean isXD;
        private List<BI> bis;
        public Region() {
            this.bis = new ArrayList<>();
        }
        public void addBi(BI bi){
            this.bis.add(bi);
        }
        public float getLow() {
            return low;
        }

        public void setLow(float low) {
            this.low = low;
        }

        public float getHigh() {
            return high;
        }

        public void setHigh(float high) {
            this.high = high;
        }

        public float getLowest() {
            return lowest;
        }

        public void setLowest(float lowest) {
            this.lowest = lowest;
        }

        public float getHighest() {
            return highest;
        }

        public void setHighest(float highest) {
            this.highest = highest;
        }

        public boolean isXD() {
            return isXD;
        }

        public void setXD(boolean XD) {
            isXD = XD;
        }

        public List<BI> getBis() {
            return bis;
        }

        public void setBis(List<BI> bis) {
            this.bis = bis;
        }
    }
}
