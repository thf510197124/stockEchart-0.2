package com.example.demo.controller.JsonObject;

import java.io.Serializable;

/**
 * @FileName: VolumeData
 * @Author: Haifeng Tong
 * @Date: 2021/4/274:19 下午
 * @Description:
 * @History: 2021/4/27
 */
public class VolumeData implements Serializable {
    private static final long serialVersionUID = 18000000000L;
    public static final int GREEN = 1;
    public static final int RED = -1;
    private int index;
    private long volume;
    private int isGreen;

    public VolumeData() {
    }

    public VolumeData(int index, long volume, int isGreen) {
        this.index = index;
        this.volume = volume;
        this.isGreen = isGreen;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    public int getIsGreen() {
        return isGreen;
    }

    public void setIsGreen(int isGreen) {
        this.isGreen = isGreen;
    }

    @Override
    public String toString() {
        return "[" + index + ","+ volume + ","+ isGreen + "]";
    }
}
