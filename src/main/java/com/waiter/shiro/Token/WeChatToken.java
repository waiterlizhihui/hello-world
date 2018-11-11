package com.waiter.shiro.Token;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * @author lizhihui
 * @version 2018/11/2
 * @Description
 */
public class WeChatToken extends UsernamePasswordToken {
    private String oauthCode;

    public WeChatToken(){
        super();
    }

    public WeChatToken(final String username, final String password, boolean rememberMe, final String host,
                       final String code, final String state){
        super(username, "", rememberMe, host);
        this.oauthCode = code;
    }

    public WeChatToken(String host, String code){
        super("","",false,host);
        this.oauthCode = code;
    }

    public String getOauthCode() {
        return oauthCode;
    }

    public void setOauthCode(String oauthCode) {
        this.oauthCode = oauthCode;
    }
}
