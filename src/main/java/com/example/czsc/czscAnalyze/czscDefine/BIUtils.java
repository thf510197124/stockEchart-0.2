package com.example.czsc.czscAnalyze.czscDefine;



import com.example.czsc.common.Klines;
import com.example.czsc.czscAnalyze.objects.BI;
import com.example.czsc.czscAnalyze.objects.Direction;
import com.example.czsc.czscAnalyze.objects.FX;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.czsc.czscAnalyze.czscDefine.CzscUtils.getLast;
import static com.example.czsc.utils.Utils.printSplit;
import static com.example.czsc.utils.Utils.println;

/**
 * @FileName: BIUtils
 * @Author: Haifeng Tong
 * @Date: 2021/3/17:19 下午
 * @Description: 笔 工具类
 * @History:
 */
class BIUtils {

    private static BI createBI(FX fx1, FX fx2) throws Exception {
        if (fx1.getMark() != fx2.getMark()){
            BI bi = new BI();
            bi.setFx_begin(fx1.getTime().isBefore(fx2.getTime()) ? fx1 : fx2);
            bi.setFx_end(fx1.getTime().isBefore(fx2.getTime()) ? fx2 : fx1);
            bi.setBeginTime(bi.getFx_begin().getTime());
            bi.setEndTime(bi.getFx_end().getTime());
            bi.setDirection(fx1.getFx() > fx2.getFx() ? Direction.DOWN : Direction.UP);
            bi.setHigh(Math.max(fx1.getFx(), fx2.getFx()));
            bi.setLow(Math.min(fx2.getFx(),fx1.getFx()));
            return bi;
        }else{
            System.out.println(fx1);
            System.out.println(fx2);
            throw new Exception("两个相邻的分型，类型相同，请检查分型的定义");
        }
    }

    public static List<BI> divideBi(List<FX> fies) throws Exception {
        if (fies == null || fies.size() == 0 || fies.size() < 2){
            return null;
        }
        List<BI> bis = new ArrayList<>();

        for (int i = 0;i < fies.size()-1; i++){
            try {
                BI bi = createBI(fies.get(i), fies.get(i + 1));
                bis.add(bi);
            }catch(Exception e){
                throw new Exception("出现两个分型相同的错误");
            }

        }
        return bis;
    }

    /**
     * 这里必须保证时间的连续性，这个是由调用方法来控制。
     * @param  bis 原笔
     * @param fies 新笔
     * @return 新连接成的笔
     * @throws Exception 出现时间上的不连续性
     */
    private static List<BI> updateBIByFX(List<BI> bis,List<FX> fies) throws Exception
    {
        List<BI> newBI = divideBi(fies);
        FX preFX = getLast(bis).getFx_end();
        if (preFX.getTime() != fies.get(0).getTime()){
            throw new Exception("时间不连续");
        }
        BI betweenBi = createBI(preFX,fies.get(0));
        bis.add(betweenBi);
        bis.addAll(divideBi(fies));
        return bis;
    }
    public static List<BI> updateBI(List<BI> bis, Klines bars) throws Exception {
        LocalDateTime times = getLast(bis).getFx_end().getTime();
        if (times.isBefore(bars.get(0).getTime())){
            throw new Exception("时间不连续");
        }
        List<FX> fies = FxUtils.divideFx(bars,true);
        return updateBIByFX(bis,fies);
    }
    private static List<BI> createBis(List<FX> fies) throws Exception {
        List<BI> bis = new ArrayList<BI>();
        for (int i=1;i<fies.size();i++){
            bis.add(createBI(fies.get(i-1),fies.get(i)));
        }
        return bis;
    }
}
