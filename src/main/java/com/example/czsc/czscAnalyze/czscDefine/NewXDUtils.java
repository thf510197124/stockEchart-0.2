package com.example.czsc.czscAnalyze.czscDefine;

import com.example.czsc.common.Klines;
import com.example.czsc.czscAnalyze.czscDefine.toolsForXD.IntervalRelationship;
import com.example.czsc.czscAnalyze.objects.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.czsc.czscAnalyze.czscDefine.CzscUtils.getLast;
import static com.example.czsc.czscAnalyze.czscDefine.CzscUtils.hasGap;
import static com.example.czsc.czscAnalyze.objects.Direction.DOWN;
import static com.example.czsc.czscAnalyze.objects.Direction.UP;
import static com.example.czsc.utils.Utils.*;

/**
 * @FileName: NewXDUtils
 * @Author: Haifeng Tong
 * @Date: 2021/5/27:52 下午
 * @Description:
 * @History: 2021/5/2
 */
public class NewXDUtils {
    private static List<BI> originalBI;
    private static final LinkedList<BI> unhandled_BI = new LinkedList<>();//一次上涨，或者一次下跌形成结束后变动
    private static final LinkedList<BI> before_aspect_BI = new LinkedList<>();
    private static final LinkedList<BI> after_aspect_BI = new LinkedList<>();
    private static final List<XD> results = new ArrayList<>();
    private static boolean isDirectionUP = false;
    private static boolean isFinished = false;
    private static int countInitWhenFindXD = 0;
    public static List<XD> splitXDSWithKlines(Klines klines,boolean isSimpleBI) throws Exception {
        Analyze analyze = new Analyze(klines);
        List<FX> fies;
        if (isSimpleBI){
            fies = analyze.getSimpleFX();
        }else{
            fies = analyze.getNormalFX();
        }
        return splitXDSWithFX(fies);
    }

    public static List<XD> splitXDSWithKlines(Klines klines) throws Exception {
        Analyze analyze = new Analyze(klines);
        return splitXDSWithFX(analyze.getFX());
    }
    public static List<XD> splitXDSWithFX(List<FX> fis) throws Exception {
        return splitXDSWithBI(Analyze.getBIS(fis));
    }

    private static void init(List<BI> bis) {
        originalBI = bis;
        unhandled_BI.clear();
        unhandled_BI.addAll(bis);
        isDirectionUP = bis.get(0).getDirection() == UP;
        isFinished = false;
        countInitWhenFindXD = 0;//防止无限递归
        results.clear();
    }
    public static List<XD> splitXDSWithBI(List<BI> bis){
        init(bis);
        println("所有的笔有：");
        println(bis);
        printSplit("~!");
        while(!isFinished){
            if (results.size() > 0) isDirectionUP = getLast(results).getDirection() != UP;//每次判断完一条线段时。
            XD xd;
            if (isDirectionUP){
                xd = handleUpXD(unhandled_BI);
            }else{
                xd = handleDownXD(unhandled_BI);
            }
            if(isUsefulXD(xd)) {
                xd = resumeXD(xd);
                trimUnHandled_BI(xd);//从unhandled_BI中删除currentXD；
                results.add(xd);
            }else{
                isFinished = true;
            }
        }
        return results;
    }
    /**
     *
     * @param bis ,添加这个参数是为了递归判断，为current_unhandled_BI
     * @return
     */
    private static XD handleUpXD(List<BI> bis) {
        XD xd = new XD();
        initWhenFindXD(bis);

        //step 1:找到破坏线段,核心是找到split。
        //split的位置，是最高点的位置
        int split = foundSplitXDIndex(bis);
        if (split == -1){
            isFinished = true;
            return null;
        }
        //step 2：确定是第一类线段，还是第二类线段
        //这时候需要对前面的before_aspect_BI，after_aspect_BI，都进行处理
        splitAspectBI(split);
        handleUpContains();
        BI aspectBI1 = getLast(before_aspect_BI);
        BI aspectBI2 = after_aspect_BI.get(0);
        int index = unhandled_BI.indexOf(bis.get(split));//因为bis与unhandled可能不同
        //step3 确定是第一类的线段破坏，还是第二类的线段破坏
        if(!hasGap(aspectBI1,aspectBI2)){//破坏线段形成已经经过判断，所以，如果没有缺口，则直接判断上涨线段结束
            println("第一类线段形成");
            xd.setBis(unhandled_BI.subList(0,index + 1));//因为sublist不取最后一个元素
            println("第一类线段形成");
            println(xd);
            return xd;
        }else{//需要查找最低点的位置。
            println("判断是否形成第二类线段");
            LinkedList<BI> afterBI = new LinkedList<>(unhandled_BI.subList(index + 1,unhandled_BI.size()));
            BI interval = after_aspect_BI.get(0);

            println("下跌的第一区间为" + interval.getLow() + "--" + interval.getHigh());

            for (BI bi : after_aspect_BI){
                IntervalRelationship ship = IntervalRelationship.getRelationship(interval,bi);
                if(ship == IntervalRelationship.DOWN || ship == IntervalRelationship.UNCONTAIN_DOWN
                        || ship == IntervalRelationship.ISCONTAINED){
                    interval = bi;//因为已经找到了破坏线段，所以interval一定被更新了
                }else if(ship == IntervalRelationship.UP || ship == IntervalRelationship.UNCONTAIN_UP ||
                        ship == IntervalRelationship.INCLUDEAFTER){
                    break;
                }
            }

            println("下跌的第二区间为" + interval.getLow() + "--" + interval.getHigh());
            int lowPointIndex = afterBI.indexOf(interval);
            println("最低点在的位置" + afterBI.get(lowPointIndex));

            List<BI> leftBI = afterBI.subList(0,lowPointIndex + 1);
            List<BI> leftAspect = leftBI.stream().filter(bi ->bi.getDirection() == UP)
                    .collect(Collectors.toList());
            println("leftAspect");
            println(leftAspect);
            List<BI> rightAspect = afterBI.subList(lowPointIndex +1,afterBI.size())
                    .stream().
                    filter(bi->bi.getDirection() == UP).collect(Collectors.toList());
            if (rightAspect.size() == 0){//因为这时候已经结束了
                return null;
            }
            println("rightAspect");
            println(rightAspect);
            BI di = rightAspect.get(0);
            BI leftGao = null;//底分型左边的一边， 现在开始查找
            for(int i = leftAspect.size()-1;i >= 0;i--){//左边是倒着找
                BI bi = leftAspect.get(i);
                if (bi.getHigh() > di.getHigh() && bi.getLow() > di.getLow()){
                    leftGao = bi;
                    println("第二类上涨中，线段找到 底分型的左边" + leftGao);
                    break;
                }
            }
            BI rightGao = null;
            for (BI bi : rightAspect){
                if (bi.getHigh() > di.getHigh() && bi.getLow() > di.getLow()){
                    rightGao = bi;
                    println("第二类上涨中，线段找到 底分型的右边" + rightGao);
                    break;
                }
            }
            if (leftGao != null && rightGao != null){ //形成了底分型
                println("第二类线段形成");
                xd.setBis(unhandled_BI.subList(0,index + 1));
                println("第二类下跌线段 + " + xd);
                return xd;
            }else{
                //为了防止出现下面一笔向上突破后上涨线段结束，所以不能从目前的低点开始分析，因为这样会丢失了特征线段。
                //所以对unhandled_BI进行处理，把下跌部分合并成一笔。
                handleFakeXD(index, interval);
                xd = handleUpXD(unhandled_BI);
                return xd;
            }
        }
    }

    private static void handleFakeXD(int startIndex, BI endInterval) {
        int endIndex = unhandled_BI.indexOf(endInterval);
        BI startBI = unhandled_BI.get(startIndex+ 1);
        BI fakeXD = createFakeXD(startBI,endInterval);
        unhandled_BI.set(startIndex+1,fakeXD);
        if (endIndex >= startIndex + 2) {
            unhandled_BI.subList(startIndex + 2, endIndex + 1).clear();
        }
    }
    private static BI createFakeXD(BI startBI,BI endBi){
        BI fakeXD = new BI();
        fakeXD.setBeginTime(startBI.getBeginTime());
        fakeXD.setFx_begin(startBI.getFx_begin());
        fakeXD.setFx_end(endBi.getFx_end());
        fakeXD.setEndTime(endBi.getEndTime());
        fakeXD.setHigh(startBI.getHigh());
        fakeXD.setLow(endBi.getLow());
        fakeXD.setDirection(startBI.getDirection());
        return fakeXD;
    }


    private static void initWhenFindXD(List<BI> bis){
        countInitWhenFindXD++;
        before_aspect_BI.clear();
        after_aspect_BI.clear();
        for (int i=0;i< bis.size();i++){
            if(i % 2 != 0){
                after_aspect_BI.add(bis.get(i));
            }
        }
        if(countInitWhenFindXD > 100){
            println("判断出错，退出");
            System.exit(0);
        }
    }

    private static XD handleDownXD(List<BI> bis) {
        println("进入下跌判断----------------------------------------------------------------------------------------------------------------------");
        XD xd = new XD();
        initWhenFindXD(bis);

        int split = foundSplitXDIndex(bis);
        if (split == -1){
            isFinished = true;
            return null;
        }
        println("当前判断的低点为" + bis.get(split).getEndTime());
        //step 2：确定是第一类线段，还是第二类线段
        //这时候需要对前面的before_aspect_BI，after_aspect_BI，都进行处理
        splitAspectBI(split);
        handleDownContains();
        BI aspectBI1 = getLast(before_aspect_BI);
        BI aspectBI2 = after_aspect_BI.get(0);
        //分割点在unhandled_BI的位置，因为bis是unhandled_BI的子集
        int index = unhandled_BI.indexOf(bis.get(split));
        println("进入第一类判断吗？" + !hasGap(aspectBI1,aspectBI2));
        println("第一特征笔" + aspectBI1);
        println("第二特征笔" + aspectBI2);
        if(!hasGap(aspectBI1,aspectBI2)){//破坏线段形成已经经过判断，所以，如果没有缺口，则直接判断上涨线段结束
            xd.setBis(unhandled_BI.subList(0,index + 1));
            println("第一类线段形成");
            println(xd);
            return xd;
        }
        else{//需要查找最低点的位置。
            println("------------------------------------------------------------------------------------------------------------------------------------------------------");

            println("判断是否形成第二类线段");
            //这样会漏过一些下跌线段，这里线不处理
            LinkedList<BI> afterBI = new LinkedList<>(unhandled_BI.subList(index + 1,unhandled_BI.size()));
            BI interval = after_aspect_BI.get(0);
            for (BI bi : after_aspect_BI){
                IntervalRelationship ship = IntervalRelationship.getRelationship(interval,bi);
                if(ship == IntervalRelationship.UP || ship == IntervalRelationship.UNCONTAIN_UP ||
                    ship == IntervalRelationship.ISCONTAINED){
                    interval = bi;//因为已经找到了破坏线段，所以interval一定被更新了
                    println("更新上涨区间为" + interval.getLow() + "--" + interval.getHigh());
                }else if(ship == IntervalRelationship.DOWN || ship == IntervalRelationship.UNCONTAIN_DOWN  ||
                    ship == IntervalRelationship.INCLUDEAFTER){
                    println("查询到上涨区间结束时" + bi.getEndTime() + "关系为：" + ship);
                    println("上一个上涨区间为：" + interval.getBeginTime() + ";结束时间为：" + interval.getEndTime());
                    break;
                }
            }

            println("上涨的第二区间为" + interval.getLow() + "--" + interval.getHigh());
            int highPointIndex = afterBI.indexOf(interval);
            println("最高点所在的位置" + afterBI.get(highPointIndex));

            List<BI> leftBI = afterBI.subList(0,highPointIndex + 1);
            List<BI> leftAspect = leftBI.stream().filter(bi ->bi.getDirection() == DOWN)
                    .collect(Collectors.toList());
            List<BI> rightAspect = afterBI.subList(highPointIndex +1,afterBI.size())
                    .stream().filter(bi->bi.getDirection() == DOWN)
                    .collect(Collectors.toList());
            if (rightAspect.size() == 0){
                return null;
            }
            BI gao = rightAspect.get(0);
            BI leftDi = null;//底分型左边的一边， 现在开始查找
            printSplit("**");
            println("左侧特征线段分别为：");
            for (int i = leftAspect.size()-1;i>= 0;i--){
                BI bi = leftAspect.get(i);
                println(bi);
                if (bi.getHigh() < gao.getHigh() && bi.getLow() < gao.getLow()){
                    leftDi = bi;
                    println("第二类上涨线段找到 顶分型的左边" + leftDi);
                    break;
                }
            }
            printSplit("**");
            println("右侧特征线段分别为：");
            BI rightDi = null;
            for (BI bi : rightAspect){
                println(bi);
                if (bi.getHigh() < gao.getHigh() && bi.getLow() < gao.getLow()){
                    rightDi = bi;
                    println("第二类上涨线段找到 顶分型的右边" + rightDi);
                    break;
                }
            }
            printSplit("**");
            if (leftDi != null && rightDi != null){ //形成了顶分型
                xd.setBis(unhandled_BI.subList(0,index + 1));
                return xd;//在递归中调用时，并不会直接返回，而只是返回到调用出
            }else{
                //为了防止出现下面一笔向上突破后上涨线段结束，所以不能从目前的低点开始分析，因为这样会丢失了特征线段。
                //所以对unhandled_BI进行处理，把下跌部分合并成一笔。
                handleFakeXD(index, interval);
                println("第二类上涨线段未形成，对unhandled_BI进行处理后为：");
                println(unhandled_BI);
                xd = handleDownXD(unhandled_BI);
                return xd;
            }
        }
    }
    /**
     * 上涨中，左边的特征笔，是依次上涨的，注重考查高点，所以把后包含直接去掉，前包含无关紧要
     * 右边的特征笔是依次下跌的，按说是应该将包含关系进行合并
     * 但是右边特征笔的合并，要考虑是第一类线段，还是第二类线段
     */
    private static void handleUpContains(){
        LinkedList<BI> beforeBI = new LinkedList<>(before_aspect_BI);
        LinkedList<BI> afterBI = new LinkedList<>(after_aspect_BI);
        handleUpAspect(beforeBI, before_aspect_BI,UP);
        handlerDownAspect(afterBI, after_aspect_BI,UP);
        printSplit("++");
    }

    //仍是处理后包含，不处理前包含
    private static void handleDownContains() {
        LinkedList<BI> beforeBI = new LinkedList<>(before_aspect_BI);
        LinkedList<BI> afterBI = new LinkedList<>(after_aspect_BI);
        handlerDownAspect(beforeBI, before_aspect_BI,DOWN);
        handleUpAspect(afterBI,  after_aspect_BI,DOWN);
        printSplit("--");
        println("经过处理后，分界点前的特征笔为：");
        println(before_aspect_BI);
        println("经过处理有，分界点后的特征笔为：");
        println(after_aspect_BI);
        printSplit("--");
    }


    /**
     * 根据找到的分拆点，拆分前后特征笔
     * 把特征线段拆分成前后两段
     * @param split 拆分的分割点
     */
    private static void splitAspectBI(int split) {
        Direction direction = isDirectionUP ? UP : DOWN;
        for (int i=0;i<unhandled_BI.size();i++){
            if (i < split && unhandled_BI.get(i).getDirection() != direction){
                before_aspect_BI.add(unhandled_BI.get(i));
                after_aspect_BI.remove(unhandled_BI.get(i));
            }
        }
   }
    private static void handleUpAspect(LinkedList<BI> beforeBI,LinkedList<BI> before_aspect_bi,Direction direction) {
        BI interval = beforeBI.get(0);
        for (BI value : beforeBI) {
            IntervalRelationship ship = IntervalRelationship.getRelationship(interval, value);
            if (ship == IntervalRelationship.INCLUDEAFTER) {
                int index = before_aspect_bi.indexOf(interval);
                if (direction == UP){
                    interval.setLow(value.getLow());
                }else{//如果主方向向下，那么包含关系处理为，当前interval设置为取value的低点
                    interval.setHigh(value.getHigh());
                }
                before_aspect_bi.set(index,interval);
                before_aspect_bi.remove(value);
            } else if (ship == IntervalRelationship.UP || ship == IntervalRelationship.UNCONTAIN_UP ||
                ship == IntervalRelationship.ISCONTAINED) {
                interval = value;
            }
        }
    }

    private static void handlerDownAspect(LinkedList<BI> beforeBI,LinkedList<BI> before_aspect_bi,Direction direction) {
        BI interval = beforeBI.get(0);
        for (BI value : beforeBI) {
            IntervalRelationship ship = IntervalRelationship.getRelationship(interval, value);
            if (ship == IntervalRelationship.INCLUDEAFTER) {
                int index = before_aspect_bi.indexOf(interval);
                if (direction == UP){
                    interval.setLow(value.getLow());
                }else{//如果主方向向下，那么包含关系处理为，当前interval设置为取value的低点
                    interval.setHigh(value.getHigh());
                }
                before_aspect_bi.set(index,interval);
                before_aspect_bi.remove(value);
            } else if (ship == IntervalRelationship.DOWN || ship == IntervalRelationship.UNCONTAIN_DOWN ||
                ship == IntervalRelationship.ISCONTAINED) {
                interval = value;
            }
        }
        println("经过处理后，前特征笔为：");
        println(before_aspect_bi);
        println("--------------------");
    }


    /**
     * step 1:找到破坏线段前，最高点的上涨笔,最低点的下跌笔
     * @param bis ,从0开始判断，这里的bis是一个 unhandled_BI的子集
     * @return 返回分割点向上笔的位置
     */
    private static int foundSplitXDIndex(List<BI> bis) {
        if (bis.size() < 4){
            return -1;
        }else{
            BI interval = bis.get(3);//第一条特征笔
            for (int i=3;i < bis.size();i= i +2){
                IntervalRelationship ship = IntervalRelationship.getRelationship(interval,bis.get(i));
                if(isDirectionUP){
                    if(ship == IntervalRelationship.UP || ship == IntervalRelationship.UNCONTAIN_UP
                            || ship == IntervalRelationship.ISCONTAINED){//向下突破，重新定义区间
                        interval = bis.get(i);
                    }else if(ship == IntervalRelationship.DOWN || ship == IntervalRelationship.UNCONTAIN_DOWN
                            ){//向下突破，这样就确定形成了向下的暂定线段。
                        //向下突破，可能该笔完全包含前一笔，这种情况，是破坏笔，在前一笔的判断中
                        return unhandled_BI.indexOf(interval) - 1;
                    }
                }else{
                    //这里可能出问题，
                    if(ship == IntervalRelationship.DOWN || ship == IntervalRelationship.UNCONTAIN_DOWN
                            || ship == IntervalRelationship.ISCONTAINED){//向上突破，重新定义区间
                        interval = bis.get(i);
                    }else if(ship == IntervalRelationship.UP || ship == IntervalRelationship.UNCONTAIN_UP){//向下突破，这样就确定形成了向下的暂定线段。
                        //向下突破，可能该笔完全包含前一笔，这种情况，是破坏笔，在前一笔的判断中
                        return unhandled_BI.indexOf(interval) - 1;
                    }
                }
            }
        }
        return -1;
    }
    private static XD resumeXD(XD currentXD) {
        println("在resumeXD中，打印xd = " + currentXD );
        LocalDateTime beginTime = currentXD.getBeginTime().minusSeconds(1);
        LocalDateTime endTime = currentXD.getEndTime().plusSeconds(2);
        XD newXD = new XD();
        for (BI bi :originalBI){
            if(bi.getBeginTime().isAfter(beginTime) && bi.getEndTime().isBefore(endTime)){
                newXD.addBI(bi);
            }
        }
        return newXD;
    }

    private static boolean isUsefulXD(XD currentXD) {
        return currentXD != null && currentXD.getBis().size() != 0;
    }

    //step 1:找到破坏线段,核心是找到split。
    //第一次查找，从位置0开始查找
    //同一次判断中，再次查找，记得要找到低点的位置，从低点开始
    //这种判断是有缺陷的，相连两个可能是包含关系，
    @Deprecated
    private static int findUpIndexOfBreak(List<BI> unhandled_bi,int fromIndex){
        int split = 0;
        for (int i= fromIndex;i<after_aspect_BI.size()-1;i++){
            IntervalRelationship ship = IntervalRelationship.getRelationship(after_aspect_BI.get(i),after_aspect_BI.get(i+1));
            if (ship==IntervalRelationship.DOWN || ship==IntervalRelationship.UNCONTAIN_DOWN){//特征线段出现下降型。可能需要进行判断
                split = unhandled_bi.indexOf(after_aspect_BI.get(i)) - 1;
                for (int j = 0;j<= i;j++){
                    before_aspect_BI.add(after_aspect_BI.removeFirst());
                }
                return split;
            }
        }
        return split;
    }
    private static void trimUnHandled_BI(XD xd){
        LocalDateTime time = xd.getEndTime();
        List<BI> beforeTime = new ArrayList<>();
        for (BI bi : unhandled_BI){
            if (bi.getEndTime().isBefore(time.plusSeconds(1))){
                beforeTime.add(bi);
            }
        }
        printSplit("`!");
        println("一个线段划分结束后，未处理的unhandled_BI为");

        unhandled_BI.removeAll(beforeTime);
        println(unhandled_BI);
        printSplit("`!");
    }
}
