package com.waiter.controller.common;

import com.waiter.utils.common.LogicException;
import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.ValidationException;
import java.net.BindException;

/**
 * @author lizhihui
 * @version 2018/11/11
 * @Description 前端控制器的父类
 */
public abstract class BaseController{
    /**
     * 日志对象
     */
    protected Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 捕获参数绑定异常，并返回相应错误页面
     * @return
     */
    @ExceptionHandler({BindException.class, ValidationException.class})
    public String bindException(){
        return "error/400";
    }

    /**
     * 捕获登录授权异常，并返回相应页面
     * @return
     */
    @ExceptionHandler({AuthorizationException.class})
    public String authorizationException(){
        return "error/refuse";
    }

    /**
     * 捕获业务逻辑异常
     * @return
     */
    @ExceptionHandler({LogicException.class})
    public String logicException(){
        return "error/500";
    }

    /**
     * 捕获其它异常
     * @param e
     * @return
     */
    @ExceptionHandler({Exception.class})
    public String exception(Exception e){
        e.printStackTrace();
        return "error/500";
    }

}
