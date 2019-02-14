package com.waiter.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName ClientIPUtils
 * @Description 获取客户端真实访问IP的方法，注意：这个是通过http协议获取到的IP,这个IP其实是可以伪造的，
 * 如果要想获取绝对真实的IP，就别用http协议的IP,而去获取tcp/ip协议层的IP，这个IP是绝对不能被伪造的，但是由于操作系统的底层封装抽象，目前我还不知道该怎么获取这个IP
 * @Author lizhihui
 * @Date 2019/2/3 21:44
 * @Version 1.0
 */
public class ClientIPUtils {
    //这是一些目前为止代理服务器自己制定的标准，将真实的代理IP写在这些http头中（这个头可能随着代理服务器种类的增多而不断增加）
    private static final String[] IP_HEADERS = {
            "X-Real-IP",//nginx添加的代理IP字段
            "X-Forwarded-For",//Squid添加的代理IP字段
            "Proxy-Client-IP",//apche添加的代理IP字段
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"
    };

    /**
     * 获取真实访问IP（不一定是真的真实的）
     * @param request
     * @return
     */
    public static String getRealIp(HttpServletRequest request){
        String ip = null;
        for(String header:IP_HEADERS){
            ip = request.getHeader(header);
            if(!StringUtils.isEmpty(ip) && !"unknown".equalsIgnoreCase(ip)){
                break;
            }
        }

        if(StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)){
            ip = request.getRemoteAddr();
        }

        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ip != null && ip.length() > 15) {
            if (ip.indexOf(",") > 0) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        }
        return ip;
    }
}
