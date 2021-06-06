package com.example.czsc.czscAnalyze.objects;

/**
 * @FileName: Mark
 * @Author: Haifeng Tong
 * @Date: 2021/2/2611:06 下午
 * @Description:
 * @History:
 */
public enum Mark {
    D("底分型"),
    G("顶分型");
    private String name;
    Mark(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
