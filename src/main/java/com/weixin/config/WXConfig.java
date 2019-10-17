package com.weixin.config;

import com.jfinal.config.*;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.template.Engine;
import com.jfinal.weixin.sdk.api.ApiConfig;
import com.jfinal.weixin.sdk.api.ApiConfigKit;
import com.weixin.controller.IndexController;
import com.weixin.controller.WXApiController;
import com.weixin.controller.WeiXinOauthController;
import com.weixin.controller.WeixinMsgController;

/**
 * Created by yinh on 2019/10/16
 */
public class WXConfig extends JFinalConfig {

    static Prop p;

    @Override
    public void onStart() {
        loadConfig();
        ApiConfig ac = new ApiConfig();
        // 配置微信 API 相关参数
        ac.setToken(p.get("token"));
        ac.setAppId(p.get("appId"));
        ac.setAppSecret(p.get("appSecret"));
        ApiConfigKit.putApiConfig(ac);
    }
    /**
     * PropKit.useFirstFound(...) 使用参数中从左到右最先被找到的配置文件
     * 从左到右依次去找配置，找到则立即加载并立即返回，后续配置将被忽略
     */
    static void loadConfig() {
        if (p == null) {
            p = PropKit.useFirstFound("config.properties", "demo-config-dev.txt");
        }
    }
    @Override
    public void configConstant(Constants me) {
        loadConfig();
        me.setDevMode(p.getBoolean("devMode", false));
        // ApiConfigKit 设为开发模式可以在开发阶段输出请求交互的 xml 与 json 数据
        ApiConfigKit.setDevMode(me.getDevMode());
    }

    @Override
    public void configRoute(Routes routes) {
        routes.setMappingSuperClass(false);
        routes.add("/index", IndexController.class);
        routes.add("/msg", WeixinMsgController.class);
        routes.add("/api", WXApiController.class);
        routes.add("/oauth", WeiXinOauthController.class);
    }

    @Override
    public void configEngine(Engine me) {
        me.addSharedFunction("/common/_layout.html");
        me.addSharedFunction("/common/_paginate.html");
    }

    @Override
    public void configPlugin(Plugins plugins) {

    }

    @Override
    public void configInterceptor(Interceptors interceptors) {

    }

    @Override
    public void configHandler(Handlers handlers) {

    }
}
