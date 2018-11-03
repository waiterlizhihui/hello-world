package com.zhrt.utils.wechat;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * @author lizhihui
 * @version 2018/11/1
 * @Description
 */
public class MyToken extends UsernamePasswordToken{
    private String code;
    private String state;

    public MyToken(){
        super();
    }

    public MyToken(final String username, final String password, boolean rememberMe, final String host,
                   final String code,final String state){
        super(username, password, rememberMe, host);
        this.code = code;
        this.state = state;
    }

    public String getCode() {
        return code;
    }

    public String getState() {
        return state;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setState(String state) {
        this.state = state;
    }
}
