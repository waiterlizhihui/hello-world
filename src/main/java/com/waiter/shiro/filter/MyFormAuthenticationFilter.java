package com.waiter.shiro.filter;

import com.waiter.utils.common.GatewayHttpUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author lizhihui
 * @version 2018/11/10
 * @Description
 */
public class MyFormAuthenticationFilter extends FormAuthenticationFilter {
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        System.out.println("进入登录表单拦截器!");
        //解析表单中的数据，用于构造令牌(注意，如果想用FormAuthenticationFilter中的默认方法获取表单中的数据的话，表单中的数据字段必须按照规定命名)
        String userName = getUsername(request);
        String password = getPassword(request);
        boolean remeberMe = isRememberMe(request);
        String host = getHost(request);
        //构造令牌
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(userName,password,remeberMe,host);
        try {
            //构造一个登录对象
            Subject subject = getSubject(request,response);
            //登录对象拿着令牌去执行登录请求（具体认证规则在realm里面）
            subject.login(usernamePasswordToken);
            //如果登录认证没有发生错误的话则回调登录成功的方法（这个方法已经被我改写了），往登录成功页面跳转
            return onLoginSuccess(usernamePasswordToken,subject,request,response);
        } catch (UnknownAccountException uae) {//这些错误都是shiro定义好的，我们在realm中执行登录验证的时候根据情况抛出错误就可以
            System.out.println("用户不存在");
            //在这里调用FormAuthenticationFilter登录失败回调方法，前端控制器能够捕捉到错误
            return onLoginFailure(usernamePasswordToken,new UnknownAccountException("用户不存在"),request,response);
        } catch (IncorrectCredentialsException ice){
            System.out.println("用户名或密码错误");
            return onLoginFailure(usernamePasswordToken,new IncorrectCredentialsException("用户名或密码错误"),request,response);
        } catch (LockedAccountException lae) {
            System.out.println("用户已禁用");
            return onLoginFailure(usernamePasswordToken,new LockedAccountException("用户已被禁用"),request,response);
        } catch (AuthenticationException ae) {
            ae.printStackTrace();
            System.out.println("用户登录验证发生未知错误");
            return onLoginFailure(usernamePasswordToken,new AuthenticationException("发生未知错误"),request,response);
        }
    }

    /**
     * 重写获取访问IP的方法
     * @param request
     * @return
     */
    @Override
    protected String getHost(ServletRequest request){
        return GatewayHttpUtils.getRemoteHost((HttpServletRequest)request);
    }

    /**
     * 重写FormAuthenticationFilter登录成功回调方法
     * 如果不重写的话则登录成功之后shiro默认跳转往登录请求来自的页面（在这里是登录页面），为了在登录成功之后往配置页面中的成功页面中跳转，必须重写该方法
     * @param token
     * @param subject
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject,
                                     ServletRequest request, ServletResponse response) throws IOException {
        WebUtils.getAndClearSavedRequest(request);
        WebUtils.redirectToSavedRequest(request,response,super.getSuccessUrl());
        return false;
    }

    /**
     * 重写是否允许访问页面，先判断是否是登录操作，是登录操作，直接登录 用于用户重复登录引起无法访问的bug
     *
     * @param request
     * @param response
     * @param mappedValue
     * @return
     * @see org.apache.shiro.web.filter.authc.AuthenticatingFilter#isAccessAllowed(javax.servlet.ServletRequest,
     * javax.servlet.ServletResponse, java.lang.Object)
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        try {
            // 先判断是否是登录操作
            if (isLoginSubmission(request, response)) {
                // 如果是登录操作，登出当前用户并直接返回false允许登录
                Subject subject = SecurityUtils.getSubject();
                if (subject != null && subject.isAuthenticated()) {
                    subject.logout();
                }
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.isAccessAllowed(request, response, mappedValue);
    }

    /**
     * 重写判断是否为登录访问
     */
    @Override
    protected boolean isLoginSubmission(ServletRequest request, ServletResponse response) {
        String isLogin = request.getParameter("isLogin");
        //isLogin参数为1时，为登录操作
        if (StringUtils.isNotEmpty(isLogin) && isLogin.equals("1")) {
            return true;
        } else {
            return false;
        }
    }
}
