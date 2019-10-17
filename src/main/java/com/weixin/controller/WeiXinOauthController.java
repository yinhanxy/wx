package com.weixin.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.core.JFinal;
import com.jfinal.kit.JsonKit;

import com.jfinal.log.Log;
import com.jfinal.weixin.sdk.api.*;
import com.jfinal.weixin.sdk.jfinal.ApiController;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by yinh on 2019/10/17
 */
public class WeiXinOauthController extends ApiController {
    private static Log log = Log.getLog(WeiXinOauthController.class);

    public void index(){
        String appId= ApiConfigKit.getApiConfig().getAppId();
        String redirect_uri = getBaseUrl(getRequest())
                + "/oauth/callback";
        try {
            redirect_uri = URLEncoder.encode(redirect_uri, JFinal.me().getConstants().getEncoding());
        } catch (UnsupportedEncodingException e) {
            log.error("urlDecode is error", e);
        }
        String authorizeURL = SnsAccessTokenApi.getAuthorizeURL(appId, redirect_uri, false);
        redirect(authorizeURL);
    }

    public void callback(){
        int  subscribe=0;
        //用户同意授权，获取code
        String code=getPara("code");
        String state=getPara("state");
        if (code!=null) {
            String appId=ApiConfigKit.getApiConfig().getAppId();
            String secret=ApiConfigKit.getApiConfig().getAppSecret();
            //通过code换取网页授权access_token
            SnsAccessToken snsAccessToken=SnsAccessTokenApi.getSnsAccessToken(appId,secret,code);
            String token=snsAccessToken.getAccessToken();
            String openId=snsAccessToken.getOpenid();
            //拉取用户信息(需scope为 snsapi_userinfo)
            ApiResult apiResult= SnsApi.getUserInfo(token, openId);

            log.warn("getUserInfo:"+apiResult.getJson());
            if (apiResult.isSucceed()) {
                JSONObject jsonObject= JSON.parseObject(apiResult.getJson());
                String nickName=jsonObject.getString("nickname");
                //用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
                int sex=jsonObject.getIntValue("sex");
                String city=jsonObject.getString("city");//城市
                String province=jsonObject.getString("province");//省份
                String country=jsonObject.getString("country");//国家
                String headimgurl=jsonObject.getString("headimgurl");
                String unionid=jsonObject.getString("unionid");
                //获取用户信息判断是否关注
                ApiResult userInfo = UserApi.getUserInfo(openId);
                log.warn(JsonKit.toJson("is subsribe>>"+userInfo));
                if (userInfo.isSucceed()) {
                    String userStr = userInfo.toString();
                    subscribe=JSON.parseObject(userStr).getIntValue("subscribe");
                }
                log.info("jsonObject: "+jsonObject);
            }

            setSessionAttr("openId", openId);
//            subscribe为1表示已经订阅
            if (subscribe==1) {
                render("index.html");
//                renderText("设置成功");
            }else {
                //根据state 跳转到不同的页面
                if (state.equals("2222")) {
                    redirect("http://www.cnblogs.com/zyw-205520/");
                }else {
                    redirect("/index");
                }
            }


        }else {
            renderText("code is  null");
        }
    }

    private static String getBaseUrl(HttpServletRequest request) {
        int port = request.getServerPort();
        StringBuilder defaultDomain = new StringBuilder(request.getScheme());
        defaultDomain.append("://")
                .append(request.getServerName())
                .append(port == 80 ? "" : ":" + port)
                .append(request.getContextPath());
        return defaultDomain.toString();
    }

}
