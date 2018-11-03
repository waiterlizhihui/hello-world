package com.zhrt.shiro;

import com.zhrt.utils.wechat.MyToken;
import com.zhrt.utils.wechat.UserInfo;
import com.zhrt.utils.wechat.WechatLoginUtil;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * @author lizhihui
 * @version 2018/11/1
 * @Description
 */
public class WechatAuthorizingRealm extends AuthorizingRealm {
    /**
     * 认证回调函数, 登录时调用
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) {
        MyToken myToken = (MyToken) authcToken;
        UserInfo userInfo = WechatLoginUtil.getAccessToken(myToken.getCode());
        if(null != userInfo){
            System.out.println("用户信息获取成功!");
        }else{
            throw new UnknownAccountException();
        }
        return new SimpleAuthenticationInfo(userInfo,myToken.getPassword(),getName());
    }

    /**
     * 获取权限授权信息,如果缓存中存在，则直接从缓存中获取，否则就重新获取， 登录成功后调用
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo getAuthorizationInfo(PrincipalCollection principals) {
        return doGetAuthorizationInfo(principals);
    }

    /**
     * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        return info;
    }
}
