package com.waiter.utils;

import com.waiter.common.constant.EmailType;

import java.util.regex.Pattern;

/**
 * @ClassName RegexUtils
 * @Description 常用正则表达式工具类，主要提供的功能如下：
 * 1.检查字符串是否是邮箱格式
 * 2.校验身份证号码
 * 3.校验手机号码格式
 * 4.校验固定电话的号码格式
 * 5.校验字符串是否是汉字字符串
 * 6.
 * @Author lizhihui
 * @Date 2019/1/25 11:31
 * @Version 1.0
 */
public class RegexUtils {
    private RegexUtils(){}

    /**
     * 检查字符串是否是邮箱格式
     * @param email 待检查的字符串
     * @return 检查结果
     */
    public static boolean checkEmail(String email){
        String regex = "\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?";
        return Pattern.matches(regex,email);
    }

    /**
     * 检查字符串是否符合某种类型的邮箱格式
     * @param email 待检查的字符串
     * @param emailType 邮箱后缀，支持的邮箱后缀定义在EmailType这个枚举类型里面
     * @return
     */
    public static boolean checkEmail(String email, EmailType emailType){
        String regex = "\\w+@" + emailType.getName();
        return Pattern.matches(regex,email);
    }

    /**
     * 校验身份证号码
     * 身份证号码存在15位和18位的情况，并且最后一位校验码可能是数字，也有可能是X
     * @param idNumber
     * @return
     */
    public static boolean checkIdNumber(String idNumber){
        String regex = "[1-9]\\d{13,16}(\\d{1}|X)";
        return Pattern.matches(regex,idNumber);
    }

    /**
     * 校验手机号码格式，支持加前缀的手机号码（比如说+8617801010101）
     * @param phoneNumber
     * @return
     */
    public static boolean checkPhoneNumber(String phoneNumber){
        String regex = "(\\+\\d+)?1[345789]\\d{9}$";
        return Pattern.matches(regex,phoneNumber);
    }

    /**
     * 校验固定电话的号码格式
     * @param phone
     * @return
     */
    public static boolean checkTelePhone(String phone) {
        String regex = "(\\+\\d+)?(\\d{3,4}\\-?)?\\d{7,8}$";
        return Pattern.matches(regex, phone);
    }

    /**
     * 校验字符串是否是汉字字符串
     * @param chinese
     * @return
     */
    public static boolean checkChinese(String chinese){
        String regex = "^[\\u4E00-\\u9FA5]+$";
        return Pattern.matches(regex,chinese);
    }

//    public static boolean checkURL(String url){
//        String regex = "(https?://(w{3}\\.)?)?\\w+\\.\\w+(\\.[a-zA-Z]+)*(:\\d{1,5})?(/\\w*)*(\\??(.+=.*)?(&.+=.*)?)?";
//        return Pattern.matches(regex,url);
//    }

//    public static boolean checkPostCode(String postcode){
//        String regex = "[1-9]\\d{5}";
//        return Pattern.matches(regex,postcode);
//    }

    public static void main(String[] args){

    }
}
