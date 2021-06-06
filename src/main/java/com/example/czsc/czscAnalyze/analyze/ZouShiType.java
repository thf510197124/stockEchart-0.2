package com.example.czsc.czscAnalyze.analyze;

import com.example.czsc.czscAnalyze.objects.BI;

/**
 * @FileName: ZouShi
 * @Author: Haifeng Tong
 * @Date: 2021/5/156:08 下午
 * @Description:
 * @History: 2021/5/15
 */
public enum ZouShiType {
    GO_UP,
    GO_DOWN,
    GO_STAY;
    public static ZouShiType getZouShi(BI bi1, BI bi2, BI bi3){
        assert bi1.getEndTime() == bi2.getBeginTime();
        assert bi2.getEndTime() == bi3.getBeginTime();
        if (bi1.getHigh() < bi3.getHigh()){
            if (bi1.getLow() < bi3.getLow()){//高低点同时升高，上涨
                return GO_UP;
            }else{ //高点升高，低点降低
                return GO_STAY;
            }
        }else{
            if (bi1.getLow()> bi3.getLow()){//高低点同时降低
                return GO_DOWN;
            }else{
                return GO_STAY;
            }
        }
    }
}
