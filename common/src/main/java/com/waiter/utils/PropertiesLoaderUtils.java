package com.waiter.utils;

import org.apache.commons.io.IOUtils;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @ClassName PropertiesLoaderUtils
 * @Description 读取properties文件内容属性的工具类
 * @Author lizhihui
 * @Date 2019/2/4 11:03
 * @Version 1.0
 */
public class PropertiesLoaderUtils {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(PropertiesLoaderUtils.class);

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
     * 获取propertise属性值
     * @param key
     * @return
     */
    public String getProperties(String key){
        if(properties.contains(key)){
            return properties.getProperty(key);
        }
        return "";
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
}
