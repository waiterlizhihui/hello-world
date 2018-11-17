package com.waiter.utils;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Map;

/**
 * http请求的工具类
 */
public class HttpUtils {
    /**
     * 获得ConnectionManager，设置相关参数
     */
    private static MultiThreadedHttpConnectionManager manager = new MultiThreadedHttpConnectionManager();

    private static int connectionTimeOut = 30000;

    private static int socketTimeOut = 120000;

    private static int maxConnectionPerHost = 50;

    private static int maxTotalConnections = 800;

    /**
     * 标志初始化是否完成的flag
     */
    private static boolean initialed = false;

    /**
     * 添加日志信息
     */
    private final static Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    /**
     * 初始化ConnectionManger的方法
     */
    private static void SetPara() {
        manager.getParams().setConnectionTimeout(connectionTimeOut);
        manager.getParams().setSoTimeout(socketTimeOut);
        manager.getParams().setDefaultMaxConnectionsPerHost(maxConnectionPerHost);
        manager.getParams().setMaxTotalConnections(maxTotalConnections);
        initialed = true;
    }

    /**
     * 发送get请求（无参数）
     *
     * @param url    请求的URL地址
     * @param encode 返回内容的编码
     * @return
     */
    public static String sendGetRequest(String url, String encode) {
        //初始化一个get请求
        GetMethod get = new GetMethod(url);
        get.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        get.setFollowRedirects(true);

        String result = "";
        try {
            result = HttpUtils.sendRequest(encode, get, "get");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        return result;
    }

    /**
     * 发送post请求（无参数）
     *
     * @param url    请求的URL地址
     * @param encode 请求的内容编码
     * @return
     */
    public static String sendPostRequest(String url, String encode) {
        PostMethod post = new PostMethod(url);
        post.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        post.setFollowRedirects(false);
        StringBuffer resultBuffer = new StringBuffer();

        String result = "";
        try {
            result = HttpUtils.sendRequest(encode, post, "post");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        return result;
    }

    /**
     * 发送psot请求（有参数）
     *
     * @param url         请求的URL地址
     * @param encode      返回内容的编码
     * @param paramterMap 请求的参数Map
     * @return
     */
    public static String sendPostRequest(String url, String encode, Map<String, String> paramterMap) {
        String result = "";
        logger.info("向" + url + "发送post请求");

        NameValuePair[] nameValuePair = new NameValuePair[paramterMap.size()];
        int i = 0;
        for (String key : paramterMap.keySet()) {
            nameValuePair[i].setName(key);
            nameValuePair[i].setValue(paramterMap.get(key));
            i++;
            logger.info("请求的参数为" + nameValuePair[i]);
        }

        PostMethod post = new PostMethod(url);
        post.setRequestHeader("Content-type", "application/x-www-form-urlencoded;charset=UTF-8");
        post.setRequestBody(nameValuePair);

        try {
            result = HttpUtils.sendRequest(encode, post, encode);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        return result;
    }

    /**
     * 发送psot请求（有参数）
     *
     * @param url         请求的URL地址
     * @param encode      返回内容的编码
     * @param paramterStr 请求的参数参数Str
     * @return
     */
    public static String sendPostRequest(String url, String encode, String paramterStr) {
        String result = "";
        logger.info("向" + url + "发送post请求");

        PostMethod post = new PostMethod(url);
        RequestEntity entity;
        try {
            entity = new StringRequestEntity(paramterStr, "application/json", "UTF-8");
            post.setRequestEntity(entity);
            result = HttpUtils.sendRequest(encode, post, "post");
        } catch (UnsupportedEncodingException e) {
            logger.error("解析post字符串参数" + paramterStr + "的时候出错", e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        return result;
    }

    /**
     * 发送psot请求（有参数）
     *
     * @param url         请求的URL地址
     * @param encode      返回内容的编码
     * @param paramterStr 请求的参数参数Str
     * @param contentType 请求的conentType
     * @return
     */
    public static String sendPostRequest(String url, String encode, String paramterStr, String contentType) {
        String result = "";
        logger.info("向" + url + "发送post请求");


        PostMethod post = new PostMethod(url);
        RequestEntity entity;
        try {
            entity = new StringRequestEntity(paramterStr, contentType, "UTF-8");
            post.setRequestEntity(entity);
            result = HttpUtils.sendRequest(encode, post, "post");
        } catch (UnsupportedEncodingException e) {
            logger.error("解析post字符串参数" + paramterStr + "的时候出错", e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        return result;
    }

    /**
     * 发送psot请求（有参数）
     *
     * @param url         请求的URL地址
     * @param encode      返回内容的编码
     * @param paramterStr 请求的参数参数Str
     * @param contentType 请求的conentType
     * @return InputStream
     */
    public static InputStream sendPostRequestAndReturnStream(String url, String encode, String paramterStr, String contentType) {
        InputStream in = null;

        PostMethod post = new PostMethod(url);
        RequestEntity entity;
        try {
            entity = new StringRequestEntity(paramterStr, contentType, "UTF-8");
            post.setRequestEntity(entity);
            in = HttpUtils.sendRequestAndReturnStream(post, "post");
        } catch (UnsupportedEncodingException e) {
            logger.error("解析post字符串参数" + paramterStr + "的时候出错", e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        return in;
    }

    /**
     * 发送http请求的公用方法
     *
     * @param encode         返回数据的编码格式
     * @param httpMethodBase GET与POST请求的父类
     * @param requestType    请求类型（get代表发送get请求，post代表发送post请求）
     * @return 编码之后的字符串
     * @throws IOException
     */
    private static String sendRequest(String encode, HttpMethodBase httpMethodBase, String requestType) throws IOException {
        String result = "";

        //初始化一个http连接
        HttpClient client = new HttpClient(manager);
        client.getParams().setContentCharset(encode);
        if (!initialed) {
            HttpUtils.SetPara();
        }
        GetMethod getMethod;
        PostMethod postMethod;


        //向下转型
        if ("get".equals(requestType)) {
            getMethod = (GetMethod) httpMethodBase;
            client.executeMethod(getMethod);
        } else if ("post".equals(requestType)) {
            postMethod = (PostMethod) httpMethodBase;
            System.out.println(postMethod);
            client.executeMethod(postMethod);
        } else {
            logger.error("传入的请求类型既不是get也不是post!");
            return "wrong requestType";
        }

        //读取返回的数据
        StringBuffer resultBuffer = new StringBuffer();
        InputStreamReader inputStreamReader = null;
        BufferedReader in = null;
        try {
            inputStreamReader = new InputStreamReader(httpMethodBase.getResponseBodyAsStream(), httpMethodBase.getRequestCharSet());
            in = new BufferedReader(inputStreamReader);
            String line = null;
            while ((line = in.readLine()) != null) {
                resultBuffer.append(line);
                resultBuffer.append("\n");
            }
            in.close();
            inputStreamReader.close();
            //对返回的内容进行编码转换（默认编码格式为iso-8859-1）
            result = StringUtils.ConverterStringCode(resultBuffer.toString(), httpMethodBase.getRequestCharSet(), encode);
        } catch (HttpException e) {
            logger.error("发送http请求时出错!", e.getMessage());
        } catch (IOException e) {
            logger.error("发送http请求时出错!", e.getMessage());
        } finally {
            if (in != null) {
                in.close();
            }

            if (inputStreamReader != null) {
                inputStreamReader.close();
            }
        }
        return result;
    }

    /**
     * 发送http请求的公用方法
     *
     * @param httpMethodBase GET与POST请求的父类
     * @param requestType    请求类型（get代表发送get请求，post代表发送post请求）
     * @return 二进制流
     * @throws IOException
     */
    private static InputStream sendRequestAndReturnStream(HttpMethodBase httpMethodBase, String requestType) throws IOException {
        InputStream in = null;

        //初始化一个http连接
        HttpClient client = new HttpClient(manager);
        if (!initialed) {
            HttpUtils.SetPara();
        }
        GetMethod getMethod;
        PostMethod postMethod;

        //向下转型
        if ("get".equals(requestType)) {
            getMethod = (GetMethod) httpMethodBase;
            client.executeMethod(getMethod);
        } else if ("post".equals(requestType)) {
            postMethod = (PostMethod) httpMethodBase;
            System.out.println(postMethod);
            client.executeMethod(postMethod);
        } else {
            logger.error("传入的请求类型既不是get也不是post!");
            return null;
        }

        in = httpMethodBase.getResponseBodyAsStream();

        return in;
    }
}
