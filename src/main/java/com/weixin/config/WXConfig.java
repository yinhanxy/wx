package com.weixin.config;

import com.jfinal.config.*;
import com.jfinal.template.Engine;
import com.weixin.controller.IndexController;

/**
 * Created by yinh on 2019/10/16
 */
public class WXConfig extends JFinalConfig {
    @Override
    public void configConstant(Constants constants) {
        constants.setDevMode(true);
    }

    @Override
    public void configRoute(Routes routes) {
        routes.add("/index", IndexController.class);
    }

    @Override
    public void configEngine(Engine engine) {

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
