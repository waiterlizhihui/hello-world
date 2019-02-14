package com.waiter.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;

/**
 * @ClassName CookieUtils
 * @Description Cookie工具类，主要提供的功能如下：
 * 1.设置cookie
 * 2.获取cookie
 * 3.移除cookie
 * @Author lizhihui
 * @Date 2019/2/4 9:44
 * @Version 1.0
 */
public class CookieUtils {
    private static final Logger logger = LoggerFactory.getLogger(CookieUtils.class);

    /**
     * 在当前域名的根路径下设置一个cookie，过期时间为一天
     * @param response
     * @param name
     * @param value
     */
    public static void setCookie(HttpServletResponse response,String name,String value){
        setCookie(response,true,"","/",name,value,86400);
    }

    /**
     * 在当前域名的根路径下设置一个cookie并指定过期时间
     * @param response
     * @param name
     * @param value
     * @param expireTime
     */
    public static void setCookie(HttpServletResponse response,String name,String value,int expireTime){
        setCookie(response,true,"","/",name,value,expireTime);
    }
    /**
     * 设置cookie
     * @param response
     * @param isHttpOnly 是否设置为httponly
     * @param domain cookie所属的域名
     * @param path cookie的路径
     * @param name cookie的名称
     * @param value cookie的值
     * @param expireTime cookie的过期时间（单位是秒）
     */
    public static void setCookie(HttpServletResponse response, boolean isHttpOnly,String domain, String path, String name, String value, int expireTime) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath(path);
        cookie.setMaxAge(expireTime);
        cookie.setHttpOnly(isHttpOnly);
        if(!StringUtils.isEmpty(domain)){
            cookie.setDomain(domain);
        }
        response.addCookie(cookie);
    }

    /**
     * 获取cookie对象
     * @param request
     * @param name cookie的名称
     * @return
     */
    public static Cookie getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie;
                }
            }
        }
        return null;
    }

    /**
     * 获取cookie的值
     * @param request
     * @param name
     * @return
     */
    public static String getCookieValue(HttpServletRequest request, String name) {
        String value = null;
        Cookie cookie = getCookie(request, name);
        try {
            value = URLDecoder.decode(cookie.getValue(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            logger.error("获取cookie时出错:" + e.getMessage());
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 移除cookie
     * @param request
     * @param response
     * @param name
     */
    public static void removeCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie cookie = getCookie(request,name);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
