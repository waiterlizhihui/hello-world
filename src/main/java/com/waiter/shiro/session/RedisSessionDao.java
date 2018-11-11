package com.waiter.shiro.session;

import com.waiter.shiro.principal.BasePrincipal;
import com.waiter.service.common.RedisService;
import com.waiter.utils.common.StringUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.Serializable;

/**
 * @author lizhihui
 * @version 2018/11/1
 * @Description shiro利用redis实现会话管理
 */
public class RedisSessionDao extends EnterpriseCacheSessionDAO{
    private static final String PRINCIPAL_KEY = DefaultSubjectContext.class.getName() + "_PRINCIPALS_SESSION_KEY";

    @Autowired
    RedisService redisService;

    /**
     * DefaultSessionManager在创建完session后会调用该方法，此时可以将会话保持进redis
     * @param session
     * @return
     */
    @Override
    protected Serializable doCreate(Session session){
        //尝试获取用户凭证
        PrincipalCollection principalCollection = (PrincipalCollection) session.getAttribute(PRINCIPAL_KEY);
        // 获取session中的sessionId，如果没有的话则新建一个
        Serializable sessionId = super.doCreate(session);

        System.out.println("创建会话!");

        //如果成功获取了用凭证的话，代表用户已经通过登录验证了
        if(principalCollection != null && principalCollection.getPrimaryPrincipal() != null){
            String userId = ((BasePrincipal)principalCollection.getPrimaryPrincipal()).getUserId();
            if(StringUtils.isNotEmpty(userId)){
                //将在线的用户的session存入redis,方便在线的用户进行踢出或者其他管理，过期时间为会话保持时间（因为超过会话保持时间会话就会被销毁，用户一定不在线了）
                redisService.setStringRedis("wechat:user:inline"+userId,sessionId.toString(),1800);
            }
            //将会话保存到redis中
            redisService.setByteInRedis("wechat:user:session"+sessionId,session,1800);
        }else{
            //不太理解为什么要对这种临时会话进行管理，有什么意义？
            //临时会话保存时间为3分钟
            session.setTimeout(3*60*1000);
            redisService.setByteInRedis("wechat:user:session"+sessionId,session,180);
        }

        return sessionId;
    }

    /**
     * 获取session
     * @param sessionId
     * @return
     */
    @Override
    protected Session doReadSession(Serializable sessionId) {
        System.out.println("尝试从本地获取session");
        //先从本地缓存(我们用的是ehcache)中获取session，如果本地缓存中不存在则去redis里取
        Session session = super.doReadSession(sessionId);
        if(session == null){
            System.out.println("从本地获取缓存失败，尝试从redis中session");
            session = redisService.getByteInRedis("wechat:user:session"+sessionId.toString());
        }

        if(session == null){
            System.out.println("从redis里面获取session失败");
        }

        return session;
    }


    /**
     * 将session已经保持的时间归零
     * @param session
     */
    @Override
    protected void doUpdate(Session session) {
        //更新本地缓存的session
        super.doUpdate(session);

        String sessionId = session.getId().toString();
        //当用户在会话保持时间内进行了操作了的话，则将会话已经保持的时间归零，重新设置会话保持时间
        redisService.setByteInRedis("wechat:user:session"+sessionId,session,1800);

        //尝试获取用户凭证
        PrincipalCollection principalCollection = (PrincipalCollection) session.getAttribute(PRINCIPAL_KEY);
        //如果成功获取了用凭证的话，代表用户已经通过登录验证了
        if(principalCollection != null && principalCollection.getPrimaryPrincipal() != null){
            String userId = ((BasePrincipal)principalCollection.getPrimaryPrincipal()).getUserId();
            if(StringUtils.isNotEmpty(userId)){
                //将在线的用户的session存入redis,方便在线的用户进行踢出或者其他管理，过期时间为会话保持时间（因为超过会话保持时间会话就会被销毁，用户一定不在线了）
                redisService.setStringRedis("wechat:user:inline"+userId,sessionId.toString(),1800);
            }
            //将会话保存到redis中
            redisService.setByteInRedis("wechat:user:session"+sessionId,session,1800);
        }
    }

    /**
     * 删除session
     * @param session
     */
    @Override
    protected void doDelete(Session session) {
        //删除本地缓存session
        super.doDelete(session);
        //删除redis里面的session
        redisService.deleteRedis("wechat:user:session"+session.getId());
    }

    /**
     * 根据用户ID踢出登录用户
     * @param userId
     */
    public void kickOutUserSession(String userId){
        String sessionId = redisService.getStringInRedis("wechat:user:inline"+userId);
        //this.readSession这个方法会自动调用doReadSession方法
        Session session = this.readSession(sessionId);
        if(session != null){
            System.out.println("踢出登录的用户:"+userId);
            //this.delete方法会自动调用doDelete方法
            this.delete(session);
        }
    }

    /**
     * 根据用户ID获取session
     * @param userId
     * @return
     */
    public Session getSessionByUserId(String userId){
        String sessionId = redisService.getStringInRedis("wechat:user:inline"+userId);
        return this.doReadSession(sessionId);
    }
}
