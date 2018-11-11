package com.waiter.controller;

import com.waiter.utils.common.StringUtils;
import com.waiter.utils.wechat.WeChatLoginUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lizhihui
 * @version 2018/10/31
 * @Description
 */
@Controller
@RequestMapping("/login")
public class WeChatLoginController {
    @ResponseBody
    @RequestMapping(value="/wechat",method = RequestMethod.GET)
    public Object weChatQrCodeLogin(HttpServletRequest request, HttpServletResponse response){
        String code = request.getParameter("code");
        System.out.println("获取微信二维码");
        if(StringUtils.isEmpty(code)){
            System.out.println("没有code");
            String oauthUrl = WeChatLoginUtil.getWechatQrCodeUrl();
            System.out.println(oauthUrl);
            return new RedirectView(oauthUrl);
        }else{
            System.out.println("有code");
            ModelAndView mv = new ModelAndView();
            mv.addObject("code",code);
            mv.setViewName("login/authorize");
            return mv;
        }
    }

    @RequestMapping(value="/wechat",method = RequestMethod.POST)
    public Object loginFail(HttpServletRequest request, HttpServletResponse response){
        System.out.println("登录失败！");
        Object obj = request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
        if(obj != null){
            System.out.println("失败错误:"+obj.toString());
        }
        String oauthUrl = WeChatLoginUtil.getWechatQrCodeUrl();
        return new RedirectView(oauthUrl);
    }

    @RequestMapping(value="/success")
    public String loginSuccess(HttpServletRequest request,HttpServletResponse response){
        System.out.println("登录成功!");
        return "info2";
    }
}
