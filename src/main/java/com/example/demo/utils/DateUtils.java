package com.example.demo.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @FileName: DateUtils
 * @Author: Haifeng Tong
 * @Date: 2021/5/1511:33 下午
 * @Description:
 * @History: 2021/5/15
 */
public class DateUtils {
    private static String zoneId = ZoneId.SHORT_IDS.get("CTT");
    //LocalDate -> Date
    public static Date asDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.of(zoneId)).toInstant());
    }

    //LocalDateTime -> Date
    public static Date asDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.of(zoneId)).toInstant());
    }

    //Date -> LocalDate
    public static LocalDate asLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.of(zoneId)).toLocalDate();
    }

        //Date -> LocalDateTime
    public static LocalDateTime asLocalDateTime(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.of(zoneId)).toLocalDateTime();
    }

    public static void main(String[] args) {
        LocalDateTime time = LocalDateTime.now();
        System.out.println(asDate(time));
    }
}
