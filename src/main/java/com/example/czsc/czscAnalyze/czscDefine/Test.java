package com.example.czsc.czscAnalyze.czscDefine;


import com.example.czsc.common.Klines;
import com.example.czsc.czscAnalyze.objects.BI;
import com.example.czsc.czscAnalyze.objects.FX;
import com.example.czsc.dataSystem.simpleData.DataFromBockStockCSV;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.czsc.utils.Utils.println;


/**
 * @FileName: Test
 * @Author: Haifeng Tong
 * @Date: 2021/3/39:43 下午
 * @Description:
 * @History:
 */
public class Test {
    public static void main(String[] args) throws Exception {
        String fileName = "/Users/haifeng/Documents/stock_database/baostock/000001.csv";
        Klines klines = DataFromBockStockCSV.read(fileName, LocalDateTime.of(2020,1,1,0,0,0));
        Klines newKlines = new Klines(KlineUtils.updateBar(klines));
        List<FX> fies = FxUtils.divideFx(newKlines,true);
        assert fies != null;
        List<BI> bis = BIUtils.divideBi(fies);
        println(bis);
    }

}
