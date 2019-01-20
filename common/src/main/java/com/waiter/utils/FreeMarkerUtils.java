package com.waiter.utils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName FreeMarkerUtils
 * @Description FreeMarker模板生成类，提供的功能如下：
 * 1.将数据填充到各种文件模板中，生成指定格式的文本文件
 * @Author lizhihui
 * @Date 2019/1/20 16:52
 * @Version 1.0
 */
public class FreeMarkerUtils {
    private static final String TEMPLATE_PATH = "/template/freemarker/";

    /**
     * 根据模板生成指定目录下的文件
     * @param dataMap 要渲染的数据
     * @param templateName 要渲染的freemarker模板（默认存储在resource下的/template/freemarker/路径下面）
     * @param outputFile 生成的文件的路径
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static void generateFile(Map<String,Object> dataMap, String templateName, String outputFile) throws IOException, ClassNotFoundException {
        OutputStream outputStream = new FileOutputStream(outputFile);
        generateFile(dataMap,templateName,outputStream);
    }

    /**
     * 根据模板输出流
     * @param dataMap 要渲染的数据
     * @param templateName 要渲染的freemarker模板（默认存储在resource下的/template/freemarker/路径下面）
     * @param out 渲染完之后的文件的输出流
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static void generateFile(Map<String,Object> dataMap,String templateName,OutputStream out) throws IOException, ClassNotFoundException {
        //获取模板所在目录的绝对路径
        String templateAbsolutePath = Class.forName(FreeMarkerUtils.class.getName()).getResource(TEMPLATE_PATH).getPath();
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_23);
        //指定模板所在目录
        configuration.setDirectoryForTemplateLoading(new File(templateAbsolutePath));
        //获取模板
        Template template = configuration.getTemplate(templateName);
        //对模板进行数据渲染
        try(Writer writer = new BufferedWriter(new OutputStreamWriter(out))){
            template.process(dataMap,writer);
            System.out.println("模板文件创建成功!");
        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("name","李志慧");
        String templateAbsolutePath = Class.forName(FreeMarkerUtils.class.getName()).getResource(TEMPLATE_PATH).getPath();
        File outPutFile = new File(templateAbsolutePath+"test.html");
        FileOutputStream out = new FileOutputStream(outPutFile);
        FreeMarkerUtils.generateFile(dataMap,"htmlTemp.ftl",out);
    }
}
