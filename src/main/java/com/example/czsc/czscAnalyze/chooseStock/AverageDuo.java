package com.example.czsc.czscAnalyze.chooseStock;

import com.example.czsc.common.Klines;
import com.example.czsc.czscAnalyze.czscDefine.Analyze;
import com.example.czsc.czscAnalyze.czscDefine.TaUtils;
import com.example.czsc.dataSystem.UnifiedDataFromBaoData;
import com.example.czsc.dataSystem.database.DataSourceConnection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.czsc.czscAnalyze.chooseStock.ThirdBuyPoint.thirdBuyPoint;
import static com.example.czsc.czscAnalyze.czscDefine.CzscUtils.getLast;
import static com.example.czsc.czscAnalyze.objects.Frequency.DAY;

/**
 * @FileName: AverageDuo
 * @Author: Haifeng Tong
 * @Date: 2021/6/23:08 下午
 * @Description:
 * @History: 2021/6/2
 */
public class AverageDuo {
    public static boolean averageDuo(Klines klines){
        if (klines.size() < 10){
            return false;
        }
        List<Float> ema5 = TaUtils.EMA(klines,5);
        List<Float> ema10 = TaUtils.EMA(klines,10);
        /*if (getLast(ema5) > getLast(ema10)){
            return false;//回调
        }*/
        if (klines.size() < 20){
            return getLast(ema10) < getLast(ema5);
        }
        List<Float> ema20 = TaUtils.EMA(klines,20);
        if (getLast(ema20) > getLast(ema10)){
            return false;
        }
        if (klines.size() < 30){
            return getLast(ema20) < getLast(ema10);
        }

        List<Float> ema30 = TaUtils.EMA(klines,30);
        if (getLast(ema30) > getLast(ema20)){
            return false;
        }
        if (klines.size() < 60){
            return getLast(ema30) < getLast(ema20);
        }
        List<Float> ema60 = TaUtils.EMA(klines,60);
        if (getLast(ema60) > getLast(ema30)){
            return false;
        }
        if (klines.size() < 120){
            return getLast(ema60) < getLast(ema30);
        }
        List<Float> ema120 = TaUtils.EMA(klines,120);
        if (getLast(ema120) > getLast(ema60)){
            return false;
        }
        if (klines.size() < 250){
            return getLast(ema60) < getLast(ema30);
        }
        List<Float> ema250 = TaUtils.EMA(klines,250);
        return getLast(ema250) <getLast(ema120);
    }

    public static void main(String[] args) throws IOException {
        List<String[]> result = DataSourceConnection.getResultSet("select symbol from symbol_name");
        List<String> has = new ArrayList<>();
        /*List<String[]> result = new ArrayList<>();
        result.add(new String[]{"600368"});*/
        for(String[] s : result){
            try {
                Klines klines = UnifiedDataFromBaoData.getKlines(s[0], null, null, DAY);
                if (averageDuo(klines)) {
                    has.add(s[0]);
                }
            }catch (Exception ignored){
            }
        }
        System.out.println(has);
        //System.out.println(isThirdBuyPoint("002603",Frequency.DAY));
    }

}
