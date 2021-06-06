package com.example.dataToEchart;

import com.example.czsc.common.Kline;
import com.example.czsc.common.Klines;
import com.github.abel533.echarts.data.KData;
import com.github.abel533.echarts.series.Candlestick;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @FileName: KlineToKData
 * @Author: Haifeng Tong
 * @Date: 2021/4/234:13 下午
 * @Description:
 * @History: 2021/4/23
 */
public class KlineToKData {
    public static Candlestick getKData(Kline kline,String datePattern){
        double open = kline.getOpen();
        double close = kline.getClose();
        double min = kline.getLow();
        double max = kline.getHigh();
        Candlestick c = new Candlestick();
        SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
        c.setName(sdf.format(kline.getTime()));
        c.data(open,close,min,max);
        return c;
    }
    public static List<Candlestick> getDatas(Klines klines,String datePattern){
        List<Candlestick> ks = new ArrayList<>();
        for(Kline kline : klines){
            ks.add(getKData(kline,datePattern));
        }
        return ks;
    }
}
