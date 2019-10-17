package com.weixin.controller;

import com.jfinal.aop.Before;
import com.jfinal.weixin.sdk.jfinal.MsgControllerAdapter;
import com.jfinal.weixin.sdk.jfinal.MsgInterceptor;
import com.jfinal.weixin.sdk.msg.in.InMsg;
import com.jfinal.weixin.sdk.msg.in.InTextMsg;
import com.jfinal.weixin.sdk.msg.in.event.InFollowEvent;
import com.jfinal.weixin.sdk.msg.in.event.InMenuEvent;
import com.jfinal.weixin.sdk.msg.out.OutMusicMsg;
import com.jfinal.weixin.sdk.msg.out.OutNewsMsg;
import com.jfinal.weixin.sdk.msg.out.OutTextMsg;

/**
 * Created by yinh on 2019/10/17
 */
public class WeixinMsgController extends MsgControllerAdapter {

    @Override
    @Before(MsgInterceptor.class)
    public void index() {
        super.index();
        renderNull();
        return;
    }

    /**
     * 新用户关注了公众号
     *
     * @param inFollowEvent
     */
    @Override
    protected void processInFollowEvent(InFollowEvent inFollowEvent) {
        if (InFollowEvent.EVENT_INFOLLOW_SUBSCRIBE.equals(inFollowEvent.getEvent())) {
            InMsg msg = getInMsg();
            OutTextMsg outMsg = new OutTextMsg(msg);
            outMsg.setContent("感谢关注");

            render(outMsg);
        } else {
            renderNull();
        }
    }

    /**
     * 用户发送了文字消息
     *
     * @param inTextMsg
     */
    @Override
    protected void processInTextMsg(InTextMsg inTextMsg) {

        String msgContent = inTextMsg.getContent().trim();
        // 帮助提示
        if ("help".equalsIgnoreCase(msgContent)) {
            OutTextMsg outMsg = new OutTextMsg(inTextMsg);
            outMsg.setContent("帮助");
            render(outMsg);
        }
        // 图文消息测试
        else if ("news".equalsIgnoreCase(msgContent)) {
            OutNewsMsg outMsg = new OutNewsMsg(inTextMsg);
            outMsg.addNews("图文消息title", "图文消息description", "图文消息片 url", "图文消息 url");
            render(outMsg);
        }
        // 音乐消息测试
        else if ("music".equalsIgnoreCase(msgContent)) {
            OutMusicMsg outMsg = new OutMusicMsg(inTextMsg);
            outMsg.setTitle("Day By Day");
            outMsg.setDescription("建议在 WIFI 环境下流畅欣赏此音乐");
            outMsg.setMusicUrl("http://www.jfinal.com/DayByDay-T-ara.mp3");
            outMsg.setHqMusicUrl("http://www.jfinal.com/DayByDay-T-ara.mp3");
            outMsg.setFuncFlag(true);
            render(outMsg);
        }
        else if ("美女".equalsIgnoreCase(msgContent)) {
            OutNewsMsg outMsg = new OutNewsMsg(inTextMsg);
            outMsg.addNews("秀色可餐", "JFinal Weixin 极速开发就是这么爽，有木有 ^_^", "http://mmbiz.qpic.cn/mmbiz/zz3Q6WSrzq2GJLC60ECD7rE7n1cvKWRNFvOyib4KGdic3N5APUWf4ia3LLPxJrtyIYRx93aPNkDtib3ADvdaBXmZJg/0", "http://mp.weixin.qq.com/s?__biz=MjM5ODAwOTU3Mg==&mid=200987822&idx=1&sn=7eb2918275fb0fa7b520768854fb7b80#rd");
            render(outMsg);
        }
        // 其它文本消息直接返回原值 + 帮助提示
        else {
            OutTextMsg outMsg = new OutTextMsg(inTextMsg);
            outMsg.setContent("\t文本消息已成功接收，内容为： " + inTextMsg.getContent() + "\n\n" );
            render(outMsg);
        }
    }

    private void renderTextMsg(InMsg inMsg, String key) {
        OutTextMsg outTextMsg = new OutTextMsg(inMsg);
        outTextMsg.setContent("呵呵呵，你好！");
        render(outTextMsg);


    }

    /**
     * 用户点击了 事件菜单（备注：微信菜单分为多种类型，其实一种是事件类型）
     *
     * @param inMenuEvent
     */
    @Override
    protected void processInMenuEvent(InMenuEvent inMenuEvent) {

    }
}
