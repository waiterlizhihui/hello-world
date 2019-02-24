package com.waiter.utils;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Properties;

/**
 * @ClassName PropertiesLoaderUtils
 * @Description 读取properties文件内容属性的工具类
 * @Author lizhihui
 * @Date 2019/2/4 11:03
 * @Version 1.0
 */
public class PropertiesLoaderUtils {
    private static final Logger logger = LoggerFactory.getLogger(PropertiesLoaderUtils.class);

    private static ResourceLoader resourceLoader = new DefaultResourceLoader();

    private final Properties properties;

    /**
     * 构造函数
     * @param resourcePaths propertise文件的路径，可以有多个
     */
    public PropertiesLoaderUtils(String... resourcePaths){
        properties = loadProperties(resourcePaths);
    }

    /**
     * 获取int类型的属性
     * @param key
     * @return
     */
    public Integer getInteger(String key){
        String value = getProperties(key);
        if (value == null) {
            throw new NoSuchElementException();
        }
        return Integer.valueOf(value);
    }

    /**
     * 获取int类型的属性，没有属性的话则返回默认值
     * @param key
     * @param defalut
     * @return
     */
    public Integer getInteger(String key,String defalut){
        String value = getProperties(key,defalut);
        if (value == null) {
            throw new NoSuchElementException();
        }
        return Integer.valueOf(value);
    }

    /**
     * 获取double类型的属性
     * @param key
     * @return
     */
    public Double getDouble(String key){
        String value = getProperties(key);
        if (value == null) {
            throw new NoSuchElementException();
        }
        return Double.valueOf(value);
    }

    /**
     * 获取double类型的属性，没有属性的话则返回默认值
     * @param key
     * @param defalut
     * @return
     */
    public Double getDouble(String key,String defalut){
        String value = getProperties(key,defalut);
        if (value == null) {
            throw new NoSuchElementException();
        }
        return Double.valueOf(value);
    }

    /**
     * 获取propertise属性值
     * @param key
     * @return
     */
    public String getProperties(String key){
        if(properties.containsKey(key)){
            return properties.getProperty(key);
        }
        return "";
    }

    /**
     * 获取propertise属性值,如果没有值的话则返回默认值
     * @param key
     * @param defalut 默认值
     * @return
     */
    public String getProperties(String key,String defalut){
        if(properties.containsKey(key)){
            return properties.getProperty(key);
        }
        return defalut;
    }

    /**
     * 载入propertise文件
     * @param resourcePaths
     * @return
     */
    private Properties loadProperties(String... resourcePaths){
        Properties props = new Properties();
        for(String location:resourcePaths){
            InputStream inputStream = null;
            Resource resource = resourceLoader.getResource(location);
            try {
                inputStream = resource.getInputStream();
                props.load(inputStream);
            } catch (IOException e) {
                logger.error("Could not load properties from path:"+e.getMessage());
                e.printStackTrace();
            } finally {
                IOUtils.closeQuietly(inputStream);
            }
        }
        return props;
    }

    public static void main(String[] args){
        PropertiesLoaderUtils loaderUtils = new PropertiesLoaderUtils("config.properties");
        String x = loaderUtils.getProperties("x");
        System.out.println(x);
    }
}
