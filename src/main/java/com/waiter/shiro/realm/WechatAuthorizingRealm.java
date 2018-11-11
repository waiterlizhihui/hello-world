package com.waiter.shiro.realm;

import com.waiter.shiro.Token.WeChatToken;
import com.waiter.shiro.principal.WeChatPrincipal;
import com.waiter.utils.wechat.WeChatLoginUtil;
import com.waiter.utils.wechat.WeChatUserInfo;
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
        WeChatToken weChatToken = (WeChatToken) authcToken;
        WeChatUserInfo weChatUserInfo = WeChatLoginUtil.getAccessToken(weChatToken.getOauthCode());
        WeChatPrincipal weChatPrincipal = new WeChatPrincipal();

        if(null != weChatUserInfo){
            System.out.println("用户信息获取成功!");
            weChatPrincipal.setWeChatUserInfo(weChatUserInfo);
            //设置登录用户在系统中的唯一标识
            weChatPrincipal.setUserId(weChatUserInfo.getUnionid());
        }else{
            throw new UnknownAccountException();
        }

        return new SimpleAuthenticationInfo(weChatPrincipal,weChatToken.getPassword(),getName());
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
