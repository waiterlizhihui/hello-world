package com.waiter.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @ClassName ExceptionUtils
 * @Description 异常工具类，主要提供了以下功能
 * 1.将CheckedException转换为UncheckException
 * 2.将ErrorStack转换为字符串
 * @Author lizhihui
 * @Date 2019/2/14 16:05
 * @Version 1.0
 */
public class ExceptionUtils {
    /**
     * 将CheckedException转换为UncheckException
     * @param e
     * @return
     */
    public static RuntimeException tranToUnchecked(Exception e){
        if(e instanceof RuntimeException){
            return (RuntimeException) e;
        } else{
            return new RuntimeException(e);
        }
    }

    /**
     * 将ErrorStack转换为字符串
     * @param e
     * @return
     */
    public static String getStackTraceString(Throwable e){
        if(e == null){
            return "";
        }
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }
}
