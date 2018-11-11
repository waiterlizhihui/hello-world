package com.waiter.shiro.principal;

import java.io.Serializable;

/**
 * @author lizhihui
 * @version 2018/11/9
 * @Description
 */
public class BasePrincipal implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 登录账号
     */
    private String account;

    /**
     * 登录名称（有些系统有可能支持使用名称登录）
     */
    private String loginName;

    /**
     * 用户名
     */
    private String name;

    /**
     * 数据库中用户的ID
     */
    private String userId;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
