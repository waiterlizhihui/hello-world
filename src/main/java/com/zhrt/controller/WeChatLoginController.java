package com.zhrt.controller;

import com.zhrt.utils.wechat.MyToken;
import com.zhrt.utils.wechat.UserInfo;
import com.zhrt.utils.wechat.WechatLoginUtil;
import jdk.nashorn.internal.parser.Token;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.sound.midi.Soundbank;

/**
 * @author lizhihui
 * @version 2018/10/31
 * @Description
 */
@Controller
@RequestMapping("/login")
public class WeChatLoginController {
    @RequestMapping(value="getQrCode")
    public String getgetQrCode(){
        String url = WechatLoginUtil.getWechatQrCodeUrl();
        System.out.println("二维码地址:"+url);
        return "redirect:"+ WechatLoginUtil.getWechatQrCodeUrl();
    }

    @RequestMapping(value="getAccessToken")
    public String getAccessToken(HttpServletRequest request){
        //获取微信授权之后返回的code
        String code = request.getParameter("code");
        String state = request.getParameter("state");

        Subject currentUser = SecurityUtils.getSubject();
        if(!currentUser.isAuthenticated()){
            MyToken myToken = new MyToken();
            myToken.setState(state);
            myToken.setCode(code);
            //密码字段不能为空，所以伪造一个密码
            char[] chars = "code".toCharArray();
            myToken.setPassword(chars);
            myToken.setRememberMe(false);
            try {
                currentUser.login(myToken);
            }catch (UnknownAccountException e){
                System.out.println("账号错误");
            }catch (IncorrectCredentialsException e){
                System.out.println("密码错误");
            }
            return "info2";
        }
        return "info1";
    }

    @RequestMapping(value = "index")
    public String getIndexView(){
        return "info2";
    }
}
