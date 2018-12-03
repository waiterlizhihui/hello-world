package com.waiter.utils;


import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * @ClassName DateUtils
 * @Description 日期工具类，继承自org.apache.commons.lang3.time.DateUtils类，主要提供了以下功能
 * 1.将日期转换为指定格式的字符串
 * 2.将字符串转换成指定格式的Date类型变量
 * 3.获取系统当前日期的字符串
 * 4.获取两个日期之间的天数，不包括开始日期和结束日期
 * 5.获取与某个日期相隔多少月的日期
 * 6.获取与某个日期相隔多少天的日期
 * 7.获取某一天等于星期几
 * 8.对时间进行加减
 * 9.对时间进行时区转换
 * @Author lizhihui
 * @Date 2018/11/30 18:03
 * @Version 1.0
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    /**
     * 将日期转换为指定格式的字符串，pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"(返回星期几),如果不指定格式则返回“yyyy-MM-dd”格式的字符串
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String formatDate(Date date, Object... pattern) {
        String formatDate = null;
        if (pattern != null && pattern.length > 0) {
            formatDate = DateFormatUtils.format(date, pattern[0].toString());
        } else {
            formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
        }
        return formatDate;
    }

    /**
     * 将日期转换为yyyy-MM-dd HH:mm:ss格式的字符串
     *
     * @param date
     * @return
     */
    public static String formatDateTime(Date date) {
        return formatDate(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 将字符串转换为指定格式的日期
     *
     * @param dateStr
     * @param pattern
     * @return
     * @throws Exception
     */
    public static Date parseDate(Object dateStr, Object pattern) throws Exception {
        if (dateStr == null || pattern == null) {
            return null;
        }
        try {
            return parseDate(dateStr.toString(), pattern.toString());
        } catch (ParseException e) {
            e.printStackTrace();
            throw new Exception("字符串转换成日期时发生错误!");
        }
    }

    /**
     * 将字符串转换成日期类型的对象
     * 字符串的格式可以是parsePatterns中定义的任意一种
     *
     * @param dateStr
     * @return
     */
    public static Date parseDate(Object dateStr) throws Exception {
        if (dateStr == null) {
            return null;
        }
        try {
            return parseDate(dateStr.toString(), parsePatterns);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new Exception("字符串转换成日期时发生错误!");
        }
    }

    /**
     * 获取当前时间，根据传入的dateType获取格式
     *
     * @param dateType 可选值为：year,month,day,date,time,datetime,week
     * @return
     */
    public static String getCurrent(String dateType) {
        String current = null;
        Date date = new Date();
        switch (dateType) {
            case "year":
                current = formatDate(date, "yyyy");
                break;
            case "month":
                current = formatDate(date, "MM");
                break;
            case "day":
                current = formatDate(date, "dd");
                break;
            case "date":
                current = formatDate(date, "yyyy-MM-dd");
                break;
            case "time":
                current = formatDate(date, "HH:mm:ss");
                break;
            case "datetime":
                current = formatDate(date, "yyyy-MM-dd HH:mm:ss");
                break;
            case "week":
                current = formatDate(date, "E");
                break;
            default:
                current = formatDate(date);
        }
        return current;
    }

    /**
     * 获取两个日期之间的天数，不包括开始日期和结束日期
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static int getDaysBetweenTwoDate(Date startDate, Date endDate) {
        long startDateTimestamp = startDate.getTime();

        long endDateTimestamp = endDate.getTime();

        long betweenDays = (startDateTimestamp - endDateTimestamp) / (1000 * 3600 * 24);
        return NumberUtils.toInteger(betweenDays);
    }

    /**
     * 获取与某个日期相隔几个月之后的日期（比如2018-11-30相隔3个月的日期返回结果为2018-02-28,2018-11-15返回的结果为2019-02-15,它是以月为单位的）
     *
     * @param date
     * @param months
     * @return
     */
    public static Date getDateAfterMonths(Date date, int months) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, months);
        Date resultDate = calendar.getTime();
        return resultDate;
    }

    /**
     * 获取某一天等于星期几
     *
     * @param date
     * @return 返回结果对应关系是这样的：1代表星期日，2代表星期一，3代表星期二....以此类推
     */
    public static int getWeekDayOfDate(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
        return weekDay;
    }

    /**
     * 将日期从一个时区转换成另一个时区
     *
     * @param date
     * @param source 原来的时区
     * @param target 需要转换成的时区
     * @return
     */
    public static Date changeTimeZone(Date date, TimeZone source, TimeZone target) {
        Date dateTmp = null;
        if (date != null) {
            int timeOffset = source.getRawOffset() - target.getRawOffset();
            dateTmp = new Date(date.getTime() - timeOffset);
        }
        return dateTmp;
    }

    /**
     * 对时间进行加减
     * @param date 进行加减的时间
     * @param type 加减的字段，可以为year,month,day,hour,minute,second
     * @param value 加减的值，正数表示加，负数表示减
     * @return
     */
    public static Date addDate(Date date, String type, int value) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        switch (type) {
            case "year":
                calendar.add(Calendar.YEAR, value);
                break;
            case "month":
                calendar.add(Calendar.MONTH, value);
                break;
            case "day":
                calendar.add(Calendar.DAY_OF_YEAR, value);
                break;
            case "hour":
                calendar.add(Calendar.HOUR, value);
                break;
            case "minute":
                calendar.add(Calendar.MINUTE, value);
                break;
            case "second":
                calendar.add(Calendar.SECOND, value);
                break;
            default:
                return new Date();
        }
        return calendar.getTime();
    }

    public static void main(String[] args) throws Exception {
        Date date = new Date();
        String a = formatDate(date, "E");
        System.out.println(a);
    }
}
