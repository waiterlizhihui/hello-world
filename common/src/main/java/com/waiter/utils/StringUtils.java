/**
 * Copyright (C) 2016-Now 展鸿软通 All rights reserved.
 * 展鸿软通(北京)有限公司对本内容拥有著作权，禁止外泄，禁止未经授权使用。
 */
package com.waiter.utils;

import org.apache.commons.text.StringEscapeUtils;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName StringUtils
 * @Description String处理的工具类，通过继承org.apache.commons.lang3.StringUtils类实现，提供的功能有：
 * 1.将字符串转换为字节数组
 * 2.将字节数组转换成字符串
 * 3.替换掉字符串中的html标签
 * 4.缩略字符串
 * 5.驼峰字符串转换方法（有三种转换）
 * 6.给空的字符串设置默认值
 * 7.将对象转换成字符串
 * 8.对字符串进行编码转换
 * @Author lizhihui
 * @Date 2018/11/28 9:40
 * @Version 1.0
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {
    private static final char SEPARATOR = '_';

    //字符编码格式
    private static final String CHARSET_NAME = "UTF-8";

    /**
     * 将字符串转换为字节数组
     *
     * @param str
     * @return
     */
    public static byte[] stringToByte(String str) {
        if (str != null) {
            try {
                return str.getBytes(CHARSET_NAME);
            } catch (UnsupportedEncodingException e) {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * 将字节数组转换成字符串
     *
     * @param bytes
     * @return
     */
    public static String byteToString(byte[] bytes) {
        try {
            return new String(bytes, CHARSET_NAME);
        } catch (UnsupportedEncodingException e) {
            return EMPTY;
        }
    }

    /**
     * 替换掉字符串中的html标签
     *
     * @param html
     * @return
     */
    public static String replaceHtml(String html) {
        if (StringUtils.isBlank(html)) {
            return "";
        }
        String regEx = "<.+?>";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(html);
        String s = m.replaceAll("");
        return s;
    }

    /**
     * 缩略字符串（区别汉字和英文）
     * 截取字符串，若超过最大宽度，在文本后追回“...”
     * 如果字符串中含有html标签，会被替掉
     * 一个字母算1个长度，一个汉字算2个长度
     * <p>
     * 应用场景：
     * 在网页上有一个文章列表，固定宽度是200px，里面要显示很多行的文章标题。文章标题有中文的、有英文的、有中英混合的。
     * 如果文章的标题超长就要被截掉后面的部分，不能超出列表的200px最大宽度。
     * 当文章的标题同时有中英文时，保留多少个字符合适呢？如何在固定的宽度内保留更多的字符呢？
     * 例1："北京发生严重的雾霾"有9个字符，但宽度是200px。
     * 例2："Beijing is Smog"有15个字符，但宽度是200px。
     * 为在固定的宽度内保留更多的字符，要这样计算：一个字母占1个长度，一个汉字占2个长度。
     *
     * @param str    目标字符串
     * @param length 保留长度（一个文字要按2个长度算）
     * @return 返回被缩略后的字符串
     */
    public static String abbr(String str, int length) {
        if (str == null) {
            return "";
        }
        try {
            StringBuilder sb = new StringBuilder();
            int currentLength = 0;
            //StringEscapeUtils.unescapeHtml4()方法时将字符串中的html转义之后的字符翻译回来，比如“&amp;哈哈哈”会被翻译成"&哈哈哈"
            for (char c : replaceHtml(StringEscapeUtils.unescapeHtml4(str)).toCharArray()) {
                currentLength += String.valueOf(c).getBytes("GBK").length;
                if (currentLength <= length - 3) {
                    sb.append(c);
                } else {
                    sb.append("...");
                    break;
                }
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 驼峰命名法工具
     *
     * @return toCamelCase(" hello_world ") == "helloWorld"
     * toCapitalizeCamelCase("hello_world") == "HelloWorld"
     * toUnderScoreCase("helloWorld") = "hello_world"
     */
    public static String toCamelCase(String s) {
        if (s == null) {
            return null;
        }

        s = s.toLowerCase();

        StringBuilder sb = new StringBuilder(s.length());
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (c == SEPARATOR) {
                upperCase = true;
            } else if (upperCase) {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            } else {
                sb.append(c);
            }
        }

        return sb.toString();
    }

    /**
     * 驼峰命名法工具
     *
     * @return toCamelCase(" hello_world ") == "helloWorld"
     * toCapitalizeCamelCase("hello_world") == "HelloWorld"
     * toUnderScoreCase("helloWorld") = "hello_world"
     */
    public static String toCapitalizeCamelCase(String s) {
        if (s == null) {
            return null;
        }
        s = toCamelCase(s);
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    /**
     * 驼峰命名法工具
     *
     * @return toCamelCase(" hello_world ") == "helloWorld"
     * toCapitalizeCamelCase("hello_world") == "HelloWorld"
     * toUnderScoreCase("helloWorld") = "hello_world"
     */
    public static String toUnderScoreCase(String s) {
        if (s == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            boolean nextUpperCase = true;

            if (i < (s.length() - 1)) {
                nextUpperCase = Character.isUpperCase(s.charAt(i + 1));
            }

            if ((i > 0) && Character.isUpperCase(c)) {
                if (!upperCase || !nextUpperCase) {
                    sb.append(SEPARATOR);
                }
                upperCase = true;
            } else {
                upperCase = false;
            }

            sb.append(Character.toLowerCase(c));
        }

        return sb.toString();
    }

    /**
     * 如果不为空，则设置值
     *
     * @param target
     * @param source
     */
    public static void setValueIfNotBlank(String target, String source) {
        if (isNotBlank(source)) {
            target = source;
        }
    }


    /**
     * object转String
     */
    public static String objToString(Object obj) {
        String result = null;
        if (obj != null) {
            result = String.valueOf(obj);
        }
        return result;
    }

    /**
     * 对字符串进行编码转换
     *
     * @param source     要编码转换的字符串
     * @param srcEncode  原有编码
     * @param destEncode 目标编码
     * @return
     */
    public static String ConverterStringCode(String source, String srcEncode, String destEncode) {
        if (source != null) {
            try {
                if (!srcEncode.equals("") && !destEncode.equals("")) {
                    return new String(source.getBytes(srcEncode), destEncode);
                } else {
                    return source.toString();
                }

            } catch (UnsupportedEncodingException e) {
                return "";
            }
        } else {
            return "";
        }
    }
}
