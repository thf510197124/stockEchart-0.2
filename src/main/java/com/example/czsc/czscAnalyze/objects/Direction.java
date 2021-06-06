package com.example.czsc.czscAnalyze.objects;

/**
 * @FileName: Direction
 * @Author: Haifeng Tong
 * @Date: 2021/2/2611:09 下午
 * @Description:
 * @History:
 */
public enum Direction {
    UP("向上"),
    DOWN("向下");
    private String name;
    Direction(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
