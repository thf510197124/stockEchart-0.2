package com.example.demo.controller;


import com.alibaba.fastjson.JSONObject;
import com.example.czsc.common.Kline;
import com.example.czsc.common.Klines;

import com.example.czsc.czscAnalyze.objects.BI;
import com.example.czsc.czscAnalyze.objects.FX;
import com.example.czsc.czscAnalyze.objects.Frequency;
import com.example.czsc.dataSystem.UnifiedData;
import com.example.czsc.dataSystem.dataUtils.ReadExcel;
import com.example.demo.controller.JsonObject.ControllerUtils;
import com.example.demo.pojo.Exchange;
import com.example.demo.pojo.SymbolName;
import com.example.demo.service.ExchangeService;
import com.example.demo.service.SymbolNameService;
import com.example.simulateExchange.Position;
import com.example.simulateExchange.PositionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import static com.example.czsc.czscAnalyze.czscDefine.CzscUtils.getLast;
import static com.example.demo.controller.JsonObject.ControllerUtils.*;

/**
 * @FileName: SimulateK
 * @Author: Haifeng Tong
 * @Date: 2021/5/175:28 下午
 * @Description:
 * @History: 2021/5/17
 */
@Controller
@RequestMapping
public class SimulateK {
    @Autowired
    SymbolNameService symbolNameService;
    @Autowired
    ExchangeService exchangeService;
    private static Klines allKlines;
    private static Klines dayKlines;
    private static int currentIndex;
    private static SymbolName sn;
    private static Position position = null;

    @RequestMapping(path="allRSimulate")
    public ModelAndView getRandomStock() throws Exception {
        initKlines(SimulateType.AllRandom);
        ModelAndView mav = new ModelAndView("simulate");
        dayKlines = new Klines(allKlines.subList(0,currentIndex));
        fillModelAndView(dayKlines,mav);
        /*String path = "/Users/haifeng/Downloads/exchange.xlsx";
        ReadExcel reader = new ReadExcel();
        List<Exchange> exchanges = reader.getExcelInfo(path);
        exchangeService.saveAll(exchanges);*/
        return mav;
    }
    @RequestMapping("/isInPosition")
    @ResponseBody
    public boolean inPosition(){
        return position != null &&
                (position.getPosition() == PositionType.BUY_POSITION || position.getMoney() != 100000f);
    }
    @RequestMapping("/position")
    @ResponseBody
    public Position position(){
        position.setProfit(position.getProfit(allKlines.get(currentIndex).getClose()));
        return position;
    }

    @RequestMapping("/buy")
    @ResponseBody
    public Position buy(){
        float price = getLast(dayKlines).getClose();
        LocalDateTime time = getLast(dayKlines).getTime();
        Position ps = new Position(PositionType.BUY_POSITION, price, time);
        if (position  == null) {
            position = new Position(PositionType.BUY_POSITION, price,time);
        }else{
            position.addPosition(ps);
        }
        return position;
    }
    @RequestMapping("/sell")
    @ResponseBody
    public Position sell(){
        float price = getLast(dayKlines).getClose();
        position.clear(price);
        return position;
    }
    @RequestMapping(path="solid",method= RequestMethod.POST)
    public ModelAndView getSolidStock(String symbol,String date) throws Exception {
        sn = symbolNameService.findSymbolName(symbol);
        LocalDateTime begin = LocalDateTime.of(1990,1,1,9,0,0);
        allKlines = UnifiedData.getKlines(symbol,begin,LocalDateTime.now().minusDays(1), Frequency.DAY);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime beginTime = LocalDate.parse(date,dtf).atTime(1,1,1);
        Collections.sort(allKlines);
        List<Float> factors = UnifiedData.factor(allKlines);
        allKlines = UnifiedData.fuQuan(allKlines,factors);
        if (beginTime.isBefore(allKlines.get(0).getTime())){
            currentIndex = 1;
        }else if(beginTime.isAfter(getLast(allKlines).getTime())){
            currentIndex = allKlines.size() -1;
        }
        else {
            int year = beginTime.getYear();
            int month = beginTime.getMonthValue();
            int day = beginTime.getDayOfMonth();
            for (int i = 0; i < allKlines.size(); i++) {
                Kline k = allKlines.get(i);
                int thisDay = k.getTime().getDayOfMonth();
                if (k.getTime().getYear() == year && k.getTime().getMonthValue() == month &&
                        (thisDay > day || thisDay == day)) {
                    currentIndex = allKlines.indexOf(k);
                    break;
                }
            }
        }
        if (currentIndex ==0){
            currentIndex =1;
        }
        ModelAndView mav = new ModelAndView("simulate");
        dayKlines = new Klines(allKlines.subList(0,currentIndex));
        fillModelAndView(dayKlines,mav);
        return mav;
    }
    @RequestMapping(path="rYSimulate")
    public ModelAndView getRecentStock() throws Exception {
        initKlines(SimulateType.RecentYear);
        ModelAndView mav = new ModelAndView("simulate");
        dayKlines = new Klines(allKlines.subList(0,currentIndex));
        fillModelAndView(dayKlines,mav);
        return mav;
    }
    @RequestMapping(path="rMSimulate")
    public ModelAndView getRecentMonthStock() throws Exception {
        initKlines(SimulateType.RecentMonth);
        ModelAndView mav = new ModelAndView("simulate");
        dayKlines = new Klines(allKlines.subList(0,currentIndex));
        fillModelAndView(dayKlines,mav);
        return mav;
    }
    @RequestMapping(path="bSimulate")
    public ModelAndView getBeginStock() throws Exception {
        initKlines(SimulateType.FromBegin);
        ModelAndView mav = new ModelAndView("simulate");
        dayKlines = new Klines(allKlines.subList(0,currentIndex));
        fillModelAndView(dayKlines,mav);
        return mav;
    }
    @RequestMapping(path = "nextDay")
    public ModelAndView nextDay() throws Exception {
        currentIndex +=1;
        if(currentIndex == allKlines.size()){
            currentIndex = currentIndex -1;
        }
        if(currentIndex > 1000){
            allKlines.remove(0);
            dayKlines.remove(0);
            currentIndex -=1;
        }
        dayKlines.add(allKlines.get(currentIndex));
        ModelAndView mav = new ModelAndView("simulate");
        fillModelAndView(dayKlines,mav);
        if (position != null){
            mav.addObject("profit", position.getMoney());
        }
        return mav;
    }
    private ModelAndView fillModelAndView(Klines dayKlines,ModelAndView mav) throws Exception {
        mav.addObject("title",sn.getName());
        mav.addObject("symbol",sn.getSymbol());
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        mav.addObject("date1",dtf.format(dayKlines.get(0).getTime()));
        mav.addObject("date2",dtf.format(getLast(dayKlines).getTime()));
        Klines weekKlines = UnifiedData.getWeekKlines(dayKlines);
        Klines monthKlines = UnifiedData.getMonthKlines(dayKlines);
        frequencyAnalyze(dayKlines,Frequency.DAY,ControllerUtils.determineDatePattern(Frequency.DAY),"",mav);
        frequencyAnalyze(weekKlines,Frequency.WEEK,ControllerUtils.determineDatePattern(Frequency.WEEK),"_w",mav);
        frequencyAnalyze(monthKlines,Frequency.MONTH,ControllerUtils.determineDatePattern(Frequency.MONTH),"_m",mav);
        return mav;
    }
    private void initKlines(SimulateType type){
        List<SymbolName> sns = symbolNameService.findAll();
        sn = sns.get((int)Math.round(Math.random() * sns.size()));
        String symbol = sn.getSymbol();
        LocalDateTime begin = randomDate(type);
        allKlines = UnifiedData.getKlines(symbol,begin,LocalDateTime.now().minusDays(1), Frequency.DAY);
        List<Float> factors = UnifiedData.factor(allKlines);
        allKlines = UnifiedData.fuQuan(allKlines,factors);
        Collections.sort(allKlines);
        position = null;
        if(allKlines.size() > 100) {
            currentIndex = 100;
        }else{
            currentIndex = allKlines.size() * 2 / 3;
        }

    }
    private LocalDateTime randomDate(SimulateType type){
        int year = 1990;
        int month = (int)Math.ceil(Math.random() * 12);;
        if(type == SimulateType.AllRandom){
            year = 1990 + (int)Math.round(Math.random() * (LocalDateTime.now().getYear() - 1990));
            month = (int)Math.ceil(Math.random() * 12);
        }else if(type == SimulateType.RecentYear){
            year = 2010 + (int)Math.round(Math.random() * (LocalDateTime.now().getYear() - 2010));
        }else if(type == SimulateType.RecentMonth){
            month = LocalDateTime.now().getMonthValue();
            if (month <=6){
                year = LocalDateTime.now().getYear() - 1;
                month = 6 + LocalDateTime.now().getMonthValue();
            }else {
                year = LocalDateTime.now().getYear();
                month = LocalDateTime.now().getMonthValue()-6;
            }
        }else if(type == SimulateType.FromBegin){
            month = 1;
        }
        return LocalDateTime.of(year,month,1,9,30,0);
    }
}
