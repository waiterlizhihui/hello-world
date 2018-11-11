package com.waiter.shiro.session;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;

/**
 * @author lizhihui
 * @version 2018/11/10
 * @Description shiro的会话监听器
 */
public class SysSessionListener implements SessionListener{
    /**
     * 监听会话创建
     * @param session
     */
    @Override
    public void onStart(Session session) {
        System.out.println("创建会话x！");
    }

    /**
     * 监听会话退出
     * @param session
     */
    @Override
    public void onStop(Session session) {
        System.out.println("会话停止");
    }

    /**
     * 监听会话过期
     * @param session
     */
    @Override
    public void onExpiration(Session session) {
        System.out.println("会话已过期!");
    }
}
