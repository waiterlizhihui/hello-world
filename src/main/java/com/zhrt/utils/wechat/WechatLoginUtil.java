package com.zhrt.utils.wechat;

import com.google.gson.Gson;
import com.zhrt.utils.common.HttpClientConnect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


/**
 * @author lizhihui
 * @version 2018/11/1
 * @Description 微信扫码登录的工具类
 */
public class WechatLoginUtil {
//    @Value("${wechat.qrcode.url}")
    private static String wechatQrCodeUrl = "https://open.weixin.qq.com/connect/qrconnect?appid=wx5d905856e107260a&redirect_uri=http://mrw.so/login/getAccessToken&response_type=code&scope=snsapi_login&state=1#wechat_redirect";

//    @Value("${wechat.accesstoken.url}")
    private static String wechatAccesstokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=wx5d905856e107260a&secret=051f78dea23ad012f11be26da1f96ccf&code=CODE&grant_type=authorization_code";

//    @Value("${wechat.getuserinfo.url}")
    private static String wechatGetUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESSTOKEN&openid=OPENID";

    private static Gson gson = new Gson();

    /**
     * 返回微信登录二维码地址
     * @return
     */
    public static String getWechatQrCodeUrl(){
        return wechatQrCodeUrl;
    }

    /**
     * 通过获取AccessToken然后获取用户信息
     * @param code
     * @return
     */
    public static UserInfo getAccessToken(String code){
        String requestUrl = wechatAccesstokenUrl.replace("CODE",code);

        //发送请求获取网页授权凭证
        String result = HttpClientConnect.getGetResponseWithHttpClient(requestUrl,"UTF-8");
        WeixinOauth2Token weixinOauth2Token = gson.fromJson(result,WeixinOauth2Token.class);

        return getUserInfo(weixinOauth2Token.getAccess_token(),weixinOauth2Token.getOpenid());
    }

    /**
     * 获取用户信息
     * @param accessToken
     * @param openId
     * @return
     */
    public static UserInfo getUserInfo(String accessToken,String openId){
        String requestUrl = wechatGetUserInfoUrl.replace("ACCESSTOKEN",accessToken).replace("OPENID",openId);

        //通过网页授权获取用户信息
		String result = HttpClientConnect.getGetResponseWithHttpClient(requestUrl,"UTF-8");
		UserInfo userInfo = gson.fromJson(result,UserInfo.class);

		return userInfo;
    }
}
