package com.example.czsc.czscAnalyze.czscDefine;

import com.example.czsc.common.Klines;
import com.example.czsc.czscAnalyze.objects.BI;
import com.example.czsc.czscAnalyze.objects.Center;
import com.example.czsc.czscAnalyze.objects.XD;

import java.util.List;

/**
 * 输出中枢区间，包括笔中枢，和先段中枢
 * @FileName: CenterUtils
 * @Author: Haifeng Tong
 * @Date: 2021/5/111:05 上午
 * @Description:
 * @History: 2021/5/1
 */
public class CenterUtils {
    public static Center getXDCenter(List<XD> xds){
        if (xds.size() >= 3) {
            Center center = new Center(xds, true);
            if (center.getRecentRegion() != null) {
                return center;
            }
        }
        return null;
    }
    public static Center getBICenter(List<BI> bis){
        if (bis.size() >= 3) {
            Center center = new Center(bis, false);
            if (center.getRecentRegion() != null) {
                return center;
            }
        }
        return null;
    }
}
