package com.example.czsc.czscAnalyze.objects;

import com.example.czsc.common.Klines;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * @FileName: Frequency
 * @Author: Haifeng Tong
 * @Date: 2021/2/2611:12 下午
 * @Description:
 * @History:
 */
public enum Frequency {
    YEAR("年",525600),QUARTER("季",17280),MONTH("月",4320),WEEK("周",10080),DAY("日",1440),HOUR("时",60),
    MIN30("30分钟",30),MIN15("15分钟",15),MIN5("5分钟",5),MIN("1分钟",1);
    private String name;
    private int minutes;
    Frequency(String name,int minutes) {
        this.name = name;
        this.minutes = minutes;
    }

    public String getName() {
        return name;
    }

    public int getMinutes(){
        return minutes;
    }

    public static Frequency frequency(LocalDateTime localTime1, LocalDateTime localTime2){
        long minutes = Duration.between(localTime1,localTime2).toMinutes();
        return frequency(minutes);
    }
    public static Frequency frequency(long minutes){
        long days = Duration.ofMinutes(minutes).toDays();
        if(days >=255){
            return Frequency.YEAR;
        }else if (days >=60){
            return Frequency.QUARTER;
        }else if (days >= 22){
            return Frequency.MONTH;
        }
        else if (days >=5){
            return Frequency.WEEK;
        }else if (days >=1){
            return Frequency.DAY;
        }else{
            if(minutes >= 60){
                return Frequency.HOUR;
            }else if (minutes >= 30){
                return Frequency.MIN30;
            }else if (minutes >= 15){
                return Frequency.MIN15;
            }else if(minutes >= 5){
                return Frequency.MIN5;
            }else{
                return Frequency.MIN;
            }
        }
    }
    public static Frequency secondaryFrequency(Frequency freq){
        switch (freq){
            case YEAR:
                return QUARTER;
            case QUARTER:
                return MONTH;
            case MONTH:
                return WEEK;
            case WEEK:
                return DAY;
            case DAY:
                return MIN30;
            case HOUR:
                return MIN15;
            case MIN30:
            case MIN15:
                return MIN5;
            case MIN5:
                return MIN;
            default:
                return MIN30;
        }
    }
    public static Frequency highlyFrequency(Frequency freq){
        switch (freq){
            case YEAR:
                throw new IllegalArgumentException("年是最大的周期了");
            case QUARTER:
                return YEAR;
            case MONTH:
                return QUARTER;
            case WEEK:
                return MONTH;
            case DAY:
                return WEEK;
            case HOUR:
            case MIN30:
                return DAY;
            case MIN15:
                return HOUR;
            case MIN5:
                return MIN30;
            case MIN:
                return MIN5;
        }
        throw new IllegalArgumentException("周期不符合规定");
    }
    public static Frequency getFrequency(Klines klines){
        return klines.getFrequency();
    }
}
