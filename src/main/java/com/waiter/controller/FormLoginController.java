package com.waiter.controller;

import com.sun.deploy.net.HttpResponse;
import com.waiter.controller.common.BaseController;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lizhihui
 * @version 2018/11/10
 * @Description
 */
@Controller
@RequestMapping("login")
public class FormLoginController extends BaseController{
    @RequestMapping(value="/form",method = RequestMethod.GET)
    public String login(HttpServletRequest request, HttpServletResponse response){
        logger.info("进入登录页面");
        return "login/formLogin";
    }

    /**
     * 如果登录失败的话就会进行这个监听器
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/form",method = RequestMethod.POST)
    public String loginFail(HttpServletRequest request, HttpServletResponse response){
        //获取错误对象
        Object exception = request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
        if(exception != null){
            logger.info("登录验证错误"+exception);
        }
        return "login/formLogin";
    }
}
