package com.example.demo.controller;

import java.io.Serializable;

/**
 * @FileName: JSONArrayUtils
 * @Author: Haifeng Tong
 * @Date: 2021/4/2511:05 下午
 * @Description:
 * @History: 2021/4/25
 */
public class JSONArrayUtils implements Serializable {
    private static final long serialVersionUID = 1L;
    private final static String pre = "[";
    private final static String fix = "]";
    private final StringBuilder builder = new StringBuilder();
    private boolean isClosed = false;
    public JSONArrayUtils() {
        builder.append(pre);
    }
    public static JSONArrayUtils create(){
        return new JSONArrayUtils();
    }
    public void add(String str){
        if (builder.toString().length() !=1){
            builder.append(",");
        }
        builder.append(str);
        /*if(str.startsWith("[")){
            builder.append(str);
        }else {
            builder.append("\'");
            builder.append(str);
            builder.append("\'");
        }*/
    }
    public void add(Object obj){
        if (builder.toString().length() !=1){
            builder.append(",");
        }
        builder.append(obj.toString());
    }
    public void add(float number){
        if (builder.toString().length() !=1){
            builder.append(",");
        }
        builder.append(getScaleString(number));
    }
    public String close(){
        builder.append(fix);
        isClosed = true;
        return builder.toString();
    }
    private String getScaleString(float number){
        return String.format("%.2f",number);
    }
    //保证必须关闭，否则会出现没有"]";
    public String toString(){
        if (!isClosed){
            return close();
        }else{
            return builder.toString();
        }
    }
}
