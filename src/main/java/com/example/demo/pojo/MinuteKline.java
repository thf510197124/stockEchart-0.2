package com.example.demo.pojo;

import com.example.czsc.common.Kline;
import com.example.demo.utils.DateUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.GeneratorType;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @FileName: MinuteKlines
 * @Author: Haifeng Tong
 * @Date: 2021/5/156:44 下午
 * @Description:
 * @History: 2021/5/15
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "minute_klines")
@Data
public class MinuteKline extends Kline{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    private String symbol;
    @Temporal(TemporalType.TIMESTAMP)
    private Date minute;
    private float open;
    private float close;
    private float high;
    private float low;
    private float volume;
    private float exchangeMoney;

    public Date getMinute() {
        return minute;
    }
    @Override
    public LocalDateTime getTime() {
        if (super.getTime() != null){
            return super.getTime();
        }else
            return DateUtils.asLocalDateTime(getMinute());

    }
    @Override
    public void setTime(LocalDateTime time){
        super.setTime(time);
        this.minute = DateUtils.asDate(time);
    }
    public void setMinute(Date minute) {
        this.minute = minute;
        setTime(DateUtils.asLocalDateTime(minute));
    }
    public static MinuteKline kline(Kline k){
        MinuteKline mk = new MinuteKline();
        mk.setTime(k.getTime());
        mk.setSymbol(k.getSymbol());
        mk.setVolume(k.getVolume());
        mk.setClose(k.getClose());
        mk.setOpen(k.getOpen());
        mk.setExchangeMoney(k.getExchangePercent());
        mk.setHigh(k.getHigh());
        mk.setLow(k.getLow());
        mk.setMinute(DateUtils.asDate(k.getTime()));
        return mk;
    }
}
