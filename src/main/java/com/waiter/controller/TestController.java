package com.waiter.controller;

import com.waiter.controller.common.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lizhihui
 * @version 2018/11/10
 * @Description
 */
@Controller
@RequestMapping("test")
public class TestController extends BaseController{
    @RequestMapping(value="index")
    public String index(HttpServletRequest request, HttpServletResponse response){
        logger.info("进入拦截页面");
        return "index";
    }
}

