package com.waiter.utils;

import java.util.Date;

/**
 * @ClassName TimeUtils
 * @Description 时间工具类，提供的功能如下
 * 1.获取某个时间的时间戳
 * @Author lizhihui
 * @Date 2018/12/1 15:46
 * @Version 1.0
 */
public class TimeUtils {
    /**
     * 获取某个时间的时间戳
     * @param date
     * @return
     */
    public static long getTimeStamp(Date date){
        return date.getTime();
    }
}
