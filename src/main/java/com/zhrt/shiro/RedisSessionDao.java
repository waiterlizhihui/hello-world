package com.zhrt.shiro;

import com.zhrt.utils.common.RedisService;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

/**
 * @author lizhihui
 * @version 2018/11/1
 * @Description shiro利用redis实现会话管理
 */
public class RedisSessionDao extends EnterpriseCacheSessionDAO{
    private static final String PRINCIPAL_KEY = DefaultSubjectContext.class.getName() + "_PRINCIPALS_SESSION_KEY";

    @Autowired
    RedisService redisService;

    @Override
    protected Serializable doCreate(Session session){
        // 获取session中的用户凭证
        Serializable sessionid=super.doCreate(session);
        return sessionid;
    }

    @Override
    protected void doUpdate(Session session) {
        //什么也不用做，父父类中都已做了
        super.doUpdate(session);
    }

    @Override
    protected void doDelete(Session session) {
        //什么也不用做，父父类中都已做了
        super.doDelete(session);
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        //什么也不用做，父父类中都已做了
        return super.doReadSession(sessionId);
    }


    /**
     * 读取缓存
     * 在request生命周期内，把session缓存在request中
     */
//    @Override
//    public Session readSession(Serializable sessionId) throws UnknownSessionException {
//        try{
//            Session s = null;
//            HttpServletRequest request = Servlets.getRequest();
//            if (request != null){
//                s = (Session)request.getAttribute("session_"+sessionId);
//            }
//            if (s != null){
//                return s;
//            }
//
//            Session session = super.readSession(sessionId);
//
//            if (request != null && session != null){
//                request.setAttribute("session_"+sessionId, session);
//            }
//
//            return session;
//        }catch (UnknownSessionException e) {
//            return null;
//        }
//    }
}
