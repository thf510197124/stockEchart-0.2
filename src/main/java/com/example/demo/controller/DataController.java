package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.czsc.common.Kline;
import com.example.czsc.common.Klines;
import com.example.czsc.czscAnalyze.czscDefine.Analyze;
import com.example.czsc.czscAnalyze.czscDefine.TaUtils;
import com.example.czsc.czscAnalyze.objects.BI;
import com.example.czsc.czscAnalyze.objects.FX;
import com.example.czsc.czscAnalyze.objects.Frequency;
import com.example.czsc.czscAnalyze.objects.XD;
import com.example.demo.controller.JsonObject.*;
import com.example.demo.pojo.MinuteKline;
import com.example.demo.pojo.SymbolName;
import com.example.demo.service.MinuteKlinesService;
import com.example.demo.service.SymbolNameService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.czsc.dataSystem.UnifiedData.getKlines;
import static com.example.czsc.utils.Utils.println;
import static com.example.demo.controller.JsonObject.ControllerUtils.*;

/**
 * 准备ajax读取的数据
 * 包括 1、K线信息
 * 2、均线信息
 * 3、成交量信息
 * @FileName: DataController
 * @Author: Haifeng Tong
 * @Date: 2021/4/2410:29 下午
 * @Description:
 * @History: 2021/4/24
 */
@Controller
@RequestMapping
public class DataController {
    private String datePattern = "";//首先是确定需要使用哪个pattern
    private Frequency frequency;
    private LocalDateTime dateBegin;
    private LocalDateTime dateEnd;
    @Autowired
    private SymbolNameService symbolNameService;
    @Autowired
    private MinuteKlinesService minuteKlinesService;
    @RequestMapping("index")
    public String index(){
        return "index";
    }
    //设置成提交的方法。这样自然会调用
    @RequestMapping(path="getStock",method= RequestMethod.POST)
    public ModelAndView init(String symbol, String date1, String date2, String freq) throws Exception {
        if (freq == null || freq.length() == 0){
            frequency = Frequency.DAY;
        }else{
            frequency = Frequency.valueOf(freq);
        }
        initDatePattern(date1,date2);
        Klines klines = getKlines(symbol,dateBegin,dateEnd,frequency);

        if (frequency == Frequency.MIN){
            minuteKlinesService.saveKlines(klines);
            List<MinuteKline> min = minuteKlinesService.findBySymbolAndTime(symbol,dateBegin,dateEnd);
            klines.clear();
            klines.addAll(min);
        }
        //显示经过包含关系处理的K线
        String name = null;
        //从后往前找，因为最后一天的K线是用分钟线合成的，可能不含有name
        //而从前往后找，则可能会有股票名称变更问题。

        if(frequency.getMinutes() >= 1440){
            for (int i = klines.size()-1;i>=0;i--){
                if (klines.get(i).getName() != null){
                    name = klines.get(i).getName();
                    break;
                }
            }
            if (name != null) {
                SymbolName symbolName = symbolNameService.updateOrAdd(symbol, name, "");
                name = symbolName.getName();
            }
        }else{
            name = symbol;
        }
        //存入数据库，用来给后面查询

        ModelAndView mav = new ModelAndView("stockWithVolume");
        mav.addObject("title",name);
        frequencyAnalyze(klines,frequency,datePattern,"",mav);

        mav.addObject("symbol",symbol);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        mav.addObject("date1",dtf.format(dateBegin));
        mav.addObject("date2",dtf.format(dateEnd));
        mav.addObject("frequency",Frequency.values()[frequency.ordinal()]);
        return mav;

    }


    private void initDatePattern(String date1, String date2) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        datePattern ="";
        //首先对起始日期和终止日期进行从处理
        LocalDateTime dateFrom;
        LocalDateTime dateTo;
        if (date1 == null || date1.length() < 10){
            dateFrom = LocalDate.of(1990,1,1).atStartOfDay();
        }else{
            dateFrom = LocalDate.parse(date1, dtf).atStartOfDay();
        }

        if (date2 == null || date2.length() < 8){
            dateTo = LocalDateTime.now();
        }else{
            dateTo = LocalDate.parse(date2, dtf).atTime(23,59,59);
        }
        dateBegin = dateFrom.isBefore(dateTo) ? dateFrom : dateTo;
        dateEnd = dateFrom.isAfter(dateTo) ? dateFrom : dateTo;

        Duration duration = Duration.between(dateBegin, dateEnd);
        //初始化日期格式,并根据需要，并不取太多的K线，对dateBegin进行限制
        if (frequency != null){
            datePattern = ControllerUtils.determineDatePattern(frequency);
        }else {
            if (duration.toDays() >= 364) {
                datePattern = "yy-MM-dd";
            }else{
                datePattern = "MM-dd HH:mm";
            }
        }
    }
}
