package com.waiter.shiro.matcher;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

/**
 * @author lizhihui
 * @version 2018/11/10
 * @Description 自定义的密码认证规则
 */
public class MyCredentialsMatcher extends SimpleCredentialsMatcher{
    //重写密码认证方法，如果认证成功返回true，失败返回false
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        return true;
    }
}
