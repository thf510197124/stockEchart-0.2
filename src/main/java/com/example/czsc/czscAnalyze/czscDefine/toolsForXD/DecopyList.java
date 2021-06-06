package com.example.czsc.czscAnalyze.czscDefine.toolsForXD;

import com.example.czsc.czscAnalyze.objects.BI;
import com.example.czsc.czscAnalyze.objects.Direction;

import java.time.LocalDateTime;
import java.util.*;

import static com.example.czsc.utils.Utils.println;

/**
 * @FileName: DecopyList
 * @Author: Haifeng Tong
 * @Date: 2021/5/59:00 下午
 * @Description:
 * @History: 2021/5/5
 */
public class  DecopyList extends ArrayList<BI> {

    public boolean add(BI bi){
        BI bi1 = new BI(bi);
        super.add(bi1);
        return true;
    }
    public BI getBIByTime(LocalDateTime time){
        for (BI bi : this){
            if(bi.getEndTime() == time){
                return bi;
            }
        }
        return null;
    }
    public DecopyList() {
        super();
    }

    public DecopyList(Collection<? extends BI> c) {
        for (BI b : c){
            BI bi = new BI(b);
            super.add(bi);
        }
    }


    @Override
    public boolean contains(Object o) {
        BI bi = (BI)o;
        for (BI b : this){
            if (equal(bi,b)){
                return true;
            }
        }
        return false;
    }
    private boolean equal(BI bi,BI bi1){
        return bi1.getBeginTime() == bi.getBeginTime() && bi1.getEndTime() == bi.getEndTime() &&
                bi1.getDirection() == bi.getDirection() && bi1.getHigh() == bi.getHigh() &&
                bi1.getLow() == bi.getLow();
    }
    @Override
    public int size() {
        return super.size();
    }

    @Override
    public boolean remove(Object o) {
        BI bi = (BI)o;
        println("删除" + bi);
        for (BI b : this){
            if (equal(bi,b)){
                return super.remove(b);
            }
        }
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends BI> c) {
        for (BI b : c){
            BI bi = new BI(b);
            this.add(bi);
        }
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends BI> c) {
        LinkedList<BI> newBis = new LinkedList<BI>();
        for (BI b : c){
            newBis.add(new BI(b));
        }
        return super.addAll(index, newBis);
    }

    @Override
    public void clear() {
        super.clear();
    }

    @Override
    public BI get(int index) {
        return super.get(index);
    }

    @Override
    public BI set(int index, BI element) {
        println("更改第" + (index + 1) + "元素，序号为" + index);
        BI b = new BI(element);
        return super.set(index, b);
    }

    @Override
    public void add(int index, BI element) {
        BI b = new BI(element);
        super.add(index, b);
    }

    @Override
    public BI remove(int index) {
        return super.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        for (int i=0;i<this.size();i++){
            BI b = get(i);
            BI bi = (BI)o;
            if (equal(b,bi)){
                return i;
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        int index = -1;
        for (int i=0;i<size();i++){
            BI b = get(i);
            BI bi = (BI)o;
            if (equal(bi,b)){
                index = i;
            }
        }
        return index;
    }

    @Override
    public Object clone() {
        return super.clone();
    }

    @Override
    public Object[] toArray() {
        return super.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return (T[]) super.toArray(new BI[0]);
    }

    @Override
    public Spliterator<BI> spliterator() {
        return super.spliterator();
    }

    public static void main(String[] args) {
        DecopyList dl = new DecopyList();
        BI bi = new BI();
        bi.setDirection(Direction.DOWN);
        BI bi2 = new BI();
        bi2.setDirection(Direction.UP);
        dl.add(bi);
        println(dl.size());
        dl.add(bi2);
        println(dl);
        println(dl.size());
        println(dl.get(0) == bi);
        println(dl.contains(bi));
        println(dl.get(1));
        for (BI b : dl){
            println(b);
        }
    }
}
