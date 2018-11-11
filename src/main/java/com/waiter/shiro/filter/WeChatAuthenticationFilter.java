package com.waiter.shiro.filter;


import com.waiter.shiro.Token.WeChatToken;
import com.waiter.utils.common.R;
import com.waiter.utils.common.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;


/**
 * @author lizhihui
 * @version 2018/11/2
 * @Description 微信登录的认证拦截器
 */
public class WeChatAuthenticationFilter extends FormAuthenticationFilter {
    public static final String DEFAULT_MESSAGE_PARAM = "message";
    private String messageParam = DEFAULT_MESSAGE_PARAM;

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
//        System.out.println("执行验证登录!");
//        String code = R.get("code");
//        String state = R.get("state");
//        String host = R.getRealIp();
//        Subject currentUser = SecurityUtils.getSubject();
//        WeChatToken weChatToken = new WeChatToken();
//        if(!currentUser.isAuthenticated()){
//            weChatToken.setCode(code);
//            weChatToken.setState(state);
//            weChatToken.setHost(host);
//            //密码字段不能为空，所以伪造一个密码
//            char[] chars = "code".toCharArray();
//            weChatToken.setPassword(chars);
//            weChatToken.setRememberMe(false);
//            try {
//                currentUser.login(weChatToken);
//            }catch (UnknownAccountException e){
//                return onLoginFailure(weChatToken,new UnknownAccountException("微信授权获取失败"),request,response);
//            }
//        }
//        return onLoginSuccess(weChatToken, currentUser, request, response);

        System.out.println("进入微信登录拦截器!");
        WeChatToken weChatToken = createToken(request,response);
        try {
            Subject subject = getSubject(request,response);
            subject.login(weChatToken);
            return onLoginSuccess(weChatToken, subject, request, response);
        }catch (UnknownAccountException e){
            return onLoginFailure(weChatToken,new UnknownAccountException("微信授权获取失败"),request,response);
        }

    }


    @Override
    protected WeChatToken createToken(ServletRequest request, ServletResponse response) {
        System.out.println("执行验证登录!");

        String code = request.getParameter("oauthCode");
        String host = "127.0.0.1";

        return new WeChatToken(host,code);
    }

    /**
     * 登录失败调用事件
     */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token,
                                     AuthenticationException e, ServletRequest request, ServletResponse response) {
        String className = e.getClass().getName(), message = "";
        if (IncorrectCredentialsException.class.getName().equals(className)
                || UnknownAccountException.class.getName().equals(className)){
            message = "账号或密码错误, 请重试.";
        }
        else if (e.getMessage() != null && StringUtils.startsWith(e.getMessage(), "msg:")){
            message = StringUtils.replace(e.getMessage(), "msg:", "");
        }
        else{
            message = "系统出现点问题，请稍后再试！";
            // 输出到控制台
            //e.printStackTrace();
        }
        request.setAttribute(getFailureKeyAttribute(), className);
        request.setAttribute(getMessageParam(), message);
        return true;
    }

    public String getMessageParam() {
        return messageParam;
    }

    @Override
    public String getSuccessUrl() {
        return super.getSuccessUrl();
    }

    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject,
                                     ServletRequest request, ServletResponse response) throws Exception {
        WebUtils.issueRedirect(request, response, getSuccessUrl(), null, true);
        return false;
    }

    /**
     * 重写这个方法的意义是什么我也不清楚，下面写的“用户重复登录引起无法访问的bug”我也不知道是什么情况
     * 重写是否允许访问页面，先判断是否是登录操作，是登录操作，直接登录 用于用户重复登录引起无法访问的bug
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
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(isLogin) && isLogin.equals("1")) {
            return true;
        } else {
            return false;
        }
    }
}
