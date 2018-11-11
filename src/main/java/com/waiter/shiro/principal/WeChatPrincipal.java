package com.waiter.shiro.principal;

import com.waiter.utils.wechat.WeChatUserInfo;

import java.io.Serializable;

/**
 * @author lizhihui
 * @version 2018/11/9
 * @Description 微信验证登录的登录用户信息
 */
public class WeChatPrincipal extends BasePrincipal implements Serializable{
    private static final long serialVersionUID = 1L;

    private WeChatUserInfo weChatUserInfo;

    public WeChatPrincipal(){}

    public WeChatPrincipal(WeChatUserInfo weChatUserInfo) {
        this.weChatUserInfo = weChatUserInfo;
    }

    public WeChatUserInfo getWeChatUserInfo() {
        return weChatUserInfo;
    }

    public void setWeChatUserInfo(WeChatUserInfo weChatUserInfo) {
        this.weChatUserInfo = weChatUserInfo;
    }
}
