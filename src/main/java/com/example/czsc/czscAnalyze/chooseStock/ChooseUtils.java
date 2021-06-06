package com.example.czsc.czscAnalyze.chooseStock;

import com.example.czsc.common.Kline;
import com.example.czsc.common.Klines;

/**
 * @FileName: ChooseUtils
 * @Author: Haifeng Tong
 * @Date: 2021/6/23:10 下午
 * @Description:
 * @History: 2021/6/2
 */
public class ChooseUtils {
    //是否活跃
    public static boolean huoYue(Klines klines){
        if (klines.size() < 5){
            return false;
        }else{
            Klines subKline;
            if (klines.size() < 30){
                subKline = klines;
            }else
                subKline = new Klines(klines.subList(klines.size() - 30,klines.size()));
            return averageBoDongXing(subKline) > 4;
        }
    }
    public static float averageBoDongXing(Klines klines){
        float sum = 0;
        for (Kline kline : klines){
            sum += zhenFu(kline);
        }
        return sum / klines.size();
    }
    //振幅，范围为10
    private static float zhenFu(Kline kline){
        return (kline.getHigh() - kline.getLow()) / kline.getLow() * 100;
    }
}
