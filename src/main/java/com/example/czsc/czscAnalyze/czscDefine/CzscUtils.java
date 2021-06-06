package com.example.czsc.czscAnalyze.czscDefine;

import com.example.czsc.common.Bar;
import com.example.czsc.czscAnalyze.objects.BI;

import java.util.*;


/**
 * @FileName: Utils
 * @Author: Haifeng Tong
 * @Date: 2021/2/278:37 上午
 * @Description:
 * @History:
 */
public class CzscUtils {

    public static boolean hasGap(Bar bar1, Bar bar2){
        assert bar1 != null;
        assert bar2 != null;
        assert bar2.getTime().isAfter(bar1.getTime());
        // 把gap设置成0.02元
        return bar1.getHigh() + 0.01 < bar2.getLow() || bar2.getHigh() + 0.01 < bar1.getLow() ;
    }

    public static boolean hasGap(BI bi1, BI bi2){
        assert bi2.getFx_end().getTime().isAfter(bi1.getFx_begin().getTime());
        return bi1.getHigh()+ 0.01 < bi2.getLow() || bi2.getHigh() + 0.01 < bi1.getLow();
    }


    public static <T> T getLast(List<T> elements){
        int length = elements.size();
        return elements.get(length - 1);
    }

}
