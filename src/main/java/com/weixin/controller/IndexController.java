package com.weixin.controller;

import com.jfinal.core.Controller;

/**
 * Created by yinh on 2019/10/16
 */
public class IndexController extends Controller {

    public void index() {
        render("login.html");
    }


    public void login() {
        render("login.html");
    }
}
