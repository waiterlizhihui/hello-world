package com.waiter.shiro.realm;

import com.waiter.shiro.matcher.MyCredentialsMatcher;
import com.waiter.shiro.principal.BasePrincipal;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.annotation.PostConstruct;
import java.util.Arrays;

/**
 * @author lizhihui
 * @version 2018/11/10
 * @Description 表单登录时的登录验证规则
 */
public class FormAuthorizingRealm extends AuthorizingRealm{
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken){
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken)authcToken;
        BasePrincipal formUserPrincipal = new BasePrincipal();
        char[] pwd = "123".toCharArray();
        if(Arrays.equals(pwd,usernamePasswordToken.getPassword())){
            formUserPrincipal.setUserId("11111");
        }else{
            throw new IncorrectCredentialsException("密码不正确");
        }
        return new SimpleAuthenticationInfo(formUserPrincipal,usernamePasswordToken.getPassword(),getName());
    }

    /**
     * 获取权限授权信息,如果缓存中存在，则直接从缓存中获取，否则就重新获取， 登录成功后调用(目前还没做权限控制，所以只是简单返回)
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo getAuthorizationInfo(PrincipalCollection principals) {
        return doGetAuthorizationInfo(principals);
    }

    /**
     * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用(目前还没做权限控制，所以只是简单返回)
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        return info;
    }

    /**
     * 可以自己在这个方法中指定用何种方式对密码进行验证
     */
    @PostConstruct
    public void initCredentialsMatcher() {
        MyCredentialsMatcher myCredentialsMatcher = new MyCredentialsMatcher();
        setCredentialsMatcher(myCredentialsMatcher);
    }
}
