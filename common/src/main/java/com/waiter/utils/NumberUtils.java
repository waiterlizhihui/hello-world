package com.waiter.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * @ClassName NumberUtils
 * @Description 数字处理工具类, 提供的功能有：
 * 1.将对象转换成Double类型
 * 2.将对象转换成Float类型
 * 3.将对象转换成Long类型
 * 4.将对象转换成Integer类型
 * 5.判断一个字符串是不是为数字（整数或小数）
 * 6.精确计算两个数相除之后的值
 * 7.格式化数字
 * @Author lizhihui
 * @Date 2018/11/30 17:44
 * @Version 1.0
 */
public class NumberUtils {
    /**
     * 将对象转换成Double类型
     *
     * @param val
     * @return
     */
    public static Double toDouble(Object val) {
        if (val == null) {
            return 0D;
        }
        try {
            return Double.valueOf(StringUtils.trim(val.toString()));
        } catch (Exception e) {
            return 0D;
        }
    }

    /**
     * 将对象转换成Float类型
     *
     * @param val
     * @return
     */
    public static Float toFloat(Object val) {
        return toDouble(val).floatValue();
    }

    /**
     * 将对象转换成Long类型
     *
     * @param val
     * @return
     */
    public static Long toLong(Object val) {
        return toDouble(val).longValue();
    }

    /**
     * 将对象转换成Integers类型
     *
     * @param val
     * @return
     */
    public static Integer toInteger(Object val) {
        return toLong(val).intValue();
    }

    /**
     * 判断一个字符串是否数字（整数与小数点）
     *
     * @param number
     * @return
     */
    public static boolean isNumber(String number) {
        if (number == null || "".equals(number)) {
            return false;
        }

        int index = number.indexOf(".");
        if (index < 0) {
            return StringUtils.isNumeric(number);
        } else {
            String num1 = number.substring(0, index);
            String num2 = number.substring(index + 1);

            return StringUtils.isNumeric(num1) && StringUtils.isNumeric(num2);
        }
    }

    /**
     * 精确计算两个数字相除之后的值，并指定保留几位小数
     * @param fenmu 分母
     * @param fenzi 分子
     * @param scale 保留的小数位
     * @return
     */
    public static BigDecimal divide(BigDecimal fenmu,BigDecimal fenzi,int scale){
        if (fenmu.compareTo(new BigDecimal(0)) == 0) {
            return new BigDecimal(0);
        }
        return fenzi.divide(fenmu, scale, BigDecimal.ROUND_HALF_EVEN);
    }

    /**
     * 将一个数字格式的对象转换成指定格式的字符串
     * @param val
     * @param numerFormat 符合DecimalFormat格式的表达式
     * @return
     */
    public static String formatNumber(Object val, String numerFormat){
        DecimalFormat decimalFormat = new DecimalFormat(numerFormat);
        Double num = toDouble(val);
        String formatNumber = decimalFormat.format(num);
        return formatNumber;
    }

    public static void main(String[] args) throws Exception {

    }
}
