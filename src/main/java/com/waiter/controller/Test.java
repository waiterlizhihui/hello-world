package com.zhrt.controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lizhihui
 * @version 2018/8/22
 * @Description
 */
@Controller
public class Test {
    @RequestMapping(value="/test",method = {RequestMethod.GET, RequestMethod.POST})
    public void testRequest(HttpServletRequest request, HttpServletResponse response){
        try {
            PrintWriter out = response.getWriter();
            out.println("hello world");
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
