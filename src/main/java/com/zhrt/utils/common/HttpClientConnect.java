package com.zhrt.utils.common;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;

public class HttpClientConnect {

	//获得ConnectionManager，设置相关参数
	private static MultiThreadedHttpConnectionManager manager=new MultiThreadedHttpConnectionManager();
	private static int connectionTimeOut=30000;
	//掌阅家时间为12秒，因为网游设置的时间是10秒。默认为6秒
//	private static int socketTimeOut=12000;
	private static int socketTimeOut=120000;
	private static int maxConnectionPerHost=50;  //
	private static int maxTotalConnections=800;
	//标志初始化是否完成的flag 　 
	private static boolean initialed=false;
	//添加日志信息
	private final static Logger logger = LoggerFactory.getLogger(HttpClientConnect.class);
	//初始化ConnectionManger的方法
	public static void SetPara(){
		manager.getParams().setConnectionTimeout(connectionTimeOut);
		manager.getParams().setSoTimeout(socketTimeOut);
		manager.getParams().setDefaultMaxConnectionsPerHost(maxConnectionPerHost);
		manager.getParams().setMaxTotalConnections(maxTotalConnections);
		initialed=true;
	}
	
	/**通过get方法获取网页内容
	 * 
	 * @param url：链接的URL地址
	 * @param encode：编码
	 * @return 返回网页内容
	 */
	public static String getGetResponseWithHttpClient(String url,String encode){
		HttpClient client=new HttpClient(manager);
		client.getParams().setContentCharset(encode); 
		if(!initialed){
			HttpClientConnect.SetPara();
		}
		GetMethod get=new GetMethod(url); 
		get.setRequestHeader("Content-type","application/x-www-form-urlencoded");
		get.setFollowRedirects(true);
		String result=null;
		StringBuffer resultBuffer = new StringBuffer();
		try{
			//client.getParams().setContentCharset("GBK");
			client.executeMethod(get);
			//在目标页面情况未知的条件下，不推荐使用getResponseBodyAsString()方法
			//StringstrGetResponseBody=post.getResponseBodyAsString();
			BufferedReader in = new BufferedReader(new InputStreamReader(get.getResponseBodyAsStream(),get.getResponseCharSet()));
			String inputLine=null;
			while((inputLine=in.readLine())!=null){
				resultBuffer.append(inputLine);
				resultBuffer.append("\n");
			}
			in.close();
			result=resultBuffer.toString();
			//iso-8859-1 is the default reading encode
			result=HttpClientConnect.ConverterStringCode(resultBuffer.toString(),get.getResponseCharSet(),encode);
		}catch(SocketException e){		
//			Operlog.toError("com.zhrt.csvc.util.HttpClientConnect.getGetResponseWithHttpClient()", ErrorCode.errorDesc(ErrorCode.ERROR_NET_ERROR), ErrorCode.errorDesc(ErrorCode.ERROR_NET_ERROR), e.toString());
			result="";
		}catch(HttpException e){
//			Operlog.toError("com.zhrt.csvc.util.HttpClientConnect.getGetResponseWithHttpClient()", ErrorCode.errorDesc(ErrorCode.ERROR_HTTP_ERROR), ErrorCode.errorDesc(ErrorCode.ERROR_HTTP_ERROR), e.toString());
			result="";
		}catch(IOException e){
//			Operlog.toError("com.zhrt.csvc.util.HttpClientConnect.getGetResponseWithHttpClient()", ErrorCode.errorDesc(ErrorCode.ERROR_IO_ERROR), ErrorCode.errorDesc(ErrorCode.ERROR_IO_ERROR), e.toString());
			result="";
		}catch(Exception e){
//			Operlog.toError("com.zhrt.csvc.util.HttpClientConnect.getGetResponseWithHttpClient()", ErrorCode.errorDesc(ErrorCode.ERROR_OTHER_ERROR), ErrorCode.errorDesc(ErrorCode.ERROR_OTHER_ERROR), e.toString());
			result="";
		}finally{
			get.releaseConnection();
		}
		return result;
	}
	
	/**通过post方法获取网页内容
	 * 
	 * @param url：链接的URL地址
	 * @param encode：编码
	 * @return 返回网页内容
	 */
	public static String getPostResponseWithHttpClient(String url,String encode){
	HttpClient client = new HttpClient(manager);
		if(!initialed){
			HttpClientConnect.SetPara();
		}
		PostMethod post = new PostMethod(url);
		post.setRequestHeader("Content-type","application/x-www-form-urlencoded");
		post.setFollowRedirects(false);
		StringBuffer resultBuffer = new StringBuffer();
		String result=null;
		try{
			//client.getParams().setContentCharset("GBK");
			client.executeMethod(post);
			BufferedReader in = new BufferedReader(new InputStreamReader(post.getResponseBodyAsStream(),post.getResponseCharSet()));
			String inputLine=null;
			while((inputLine=in.readLine())!=null){
				resultBuffer.append(inputLine);
				resultBuffer.append("\n");
			}
			in.close();
			//iso-8859-1 is the default reading encode
			result=HttpClientConnect.ConverterStringCode(resultBuffer.toString(),post.getResponseCharSet(),encode);
		}catch(SocketException e){
//			Operlog.toError("com.zhrt.csvc.util.HttpClientConnect.getPostResponseWithHttpClient()", ErrorCode.errorDesc(ErrorCode.ERROR_NET_ERROR), ErrorCode.errorDesc(ErrorCode.ERROR_NET_ERROR), e.toString());
			result="";
		}catch(HttpException e){			
//			Operlog.toError("com.zhrt.csvc.util.HttpClientConnect.getPostResponseWithHttpClient()", ErrorCode.errorDesc(ErrorCode.ERROR_HTTP_ERROR), ErrorCode.errorDesc(ErrorCode.ERROR_HTTP_ERROR), e.toString());
			result="";
		}catch(IOException e){
//			Operlog.toError("com.zhrt.csvc.util.HttpClientConnect.getPostResponseWithHttpClient()", ErrorCode.errorDesc(ErrorCode.ERROR_IO_ERROR), ErrorCode.errorDesc(ErrorCode.ERROR_IO_ERROR), e.toString());
			result="";
		}catch(Exception e){
//			Operlog.toError("com.zhrt.csvc.util.HttpClientConnect.getPostResponseWithHttpClient()", ErrorCode.errorDesc(ErrorCode.ERROR_OTHER_ERROR), ErrorCode.errorDesc(ErrorCode.ERROR_OTHER_ERROR), e.toString());
			result="";
		}finally{
			post.releaseConnection();
		}
		return result;
	}
	
	/**通过post方法获取网页内容
	 * 
	 * @param url：链接的URL地址
	 * @param encode：编码
	 * @param nameValuePair:传给URL的参数数组
	 * @return 返回网页内容
	 * @return
	 */
	public static String getPostResponseWithHttpClient(String url,String encode,NameValuePair[] nameValuePair){
		HttpClient client = new HttpClient(manager);
		if(!initialed){
			HttpClientConnect.SetPara();
		}
		//UTF8PostMethod post = new UTF8PostMethod(url);
		PostMethod post = new PostMethod(url);
		//post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,"UTF-8");   
		post.setRequestHeader("Content-type","application/x-www-form-urlencoded;charset=UTF-8");
		post.setRequestBody(nameValuePair);
		
		//System.out.println("发送的数据 is ===============00000000000000000 "+nameValuePair[0]);
		for (int i = 0; i < nameValuePair.length; i++) {
			logger.info("发送的数据 is ===============00000000000000000 "+nameValuePair[i]);
		}
		//post.setFollowRedirects(false);
		String result=null;
		StringBuffer resultBuffer = new StringBuffer();
		try{
			//client.getParams().setContentCharset("GBK");

			client.executeMethod(post);
			
			BufferedReader in = new BufferedReader(new InputStreamReader(post.getResponseBodyAsStream(),post.getResponseCharSet()));
			//System.out.println("post.getResponseCharSet() is0000000000000000 "+post.getResponseCharSet());
			String inputLine = null;
			while((inputLine=in.readLine())!=null){
				resultBuffer.append(inputLine);
//				resultBuffer.append("\n");
			}
			in.close();
			//iso-8859-1 is the default reading encode
			result=HttpClientConnect.ConverterStringCode(resultBuffer.toString(),post.getResponseCharSet(),encode);
			//System.out.println("result is ===============$$$$$$$$$$$$$$$$$$$$$ "+result);
			logger.info("result is ===============$$$$$$$$$$$$$$$$$$$$$ "+result);
		}catch(SocketException e){
			e.printStackTrace();
//			Operlog.toError("com.zhrt.csvc.util.HttpClientConnect.getPostResponseWithHttpClient()", ErrorCode.errorDesc(ErrorCode.ERROR_NET_ERROR), ErrorCode.errorDesc(ErrorCode.ERROR_NET_ERROR), e.toString());
			result="";
		}catch(HttpException e){
			e.printStackTrace();
//			Operlog.toError("com.zhrt.csvc.util.HttpClientConnect.getPostResponseWithHttpClient()", ErrorCode.errorDesc(ErrorCode.ERROR_HTTP_ERROR), ErrorCode.errorDesc(ErrorCode.ERROR_HTTP_ERROR), e.toString());
			result="";
		}catch(IOException e){
			e.printStackTrace();
//			Operlog.toError("com.zhrt.csvc.util.HttpClientConnect.getPostResponseWithHttpClient()", ErrorCode.errorDesc(ErrorCode.ERROR_IO_ERROR), ErrorCode.errorDesc(ErrorCode.ERROR_IO_ERROR), e.toString());
			result="";
		}catch(Exception e){
			e.printStackTrace();
//			Operlog.toError("com.zhrt.csvc.util.HttpClientConnect.getPostResponseWithHttpClient()", ErrorCode.errorDesc(ErrorCode.ERROR_OTHER_ERROR), ErrorCode.errorDesc(ErrorCode.ERROR_OTHER_ERROR), e.toString());
			result="";
		}finally{
			post.releaseConnection();
		}
		//System.out.println("result is ===============@@@@@@@@@@@ "+result);
		logger.info("result is ===============@@@@@@@@@@@ "+result);
		return result;
	}
	
	public static String getPostResponseWithHttpClient(String url,String encode,String paraStr){
		System.out.println("访问地址："+url);
		HttpClient client = new HttpClient(manager);
		if(!initialed){
			HttpClientConnect.SetPara();
		}
		//UTF8PostMethod post = new UTF8PostMethod(url);
		PostMethod post = new PostMethod(url);
		RequestEntity entity;
		try {
			entity = new StringRequestEntity(paraStr, "application/json", "UTF-8");
			post.setRequestEntity(entity);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		
		//System.out.println("发送的数据 is ===============00000000000000000 "+paraStr);
		
		//post.setFollowRedirects(false);
		String result=null;
		StringBuffer resultBuffer = new StringBuffer();
		try{
			//client.getParams().setContentCharset("GBK");

			client.executeMethod(post);
			
			BufferedReader in = new BufferedReader(new InputStreamReader(post.getResponseBodyAsStream(),post.getResponseCharSet()));
			//System.out.println("post.getResponseCharSet() is0000000000000000 "+post.getResponseCharSet());
			String inputLine = null;
			while((inputLine=in.readLine())!=null){
				resultBuffer.append(inputLine);
				resultBuffer.append("\n");
			}
			in.close();
			//iso-8859-1 is the default reading encode
			result=HttpClientConnect.ConverterStringCode(resultBuffer.toString(),post.getResponseCharSet(),encode);
			//System.out.println("result is ===============$$$$$$$$$$$$$$$$$$$$$ "+result);
		}catch(Exception e){
			result="";
			//System.out.println("httpclient error : "+url);
			logger.info("httpclient error : "+url);
			e.printStackTrace();
		}finally{
			post.releaseConnection();
		}
		//System.out.println("result is ===============@@@@@@@@@@@ "+result);
		return result;
	}
	
	/**
	 * 
	 * http请求发包，并接收返回内容
	 * 
	 * */
	public static String getPostResponseWithHttpClient(String url,String encode,String paraStr,String contentType){
		System.out.println("访问地址："+url);
		HttpClient client = new HttpClient(manager);
		if(!initialed){
			HttpClientConnect.SetPara();
		}
		PostMethod post = new PostMethod(url);
		RequestEntity entity;
		try {
			entity = new StringRequestEntity(paraStr, contentType, "UTF-8");
			post.setRequestEntity(entity);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		
		//System.out.println("发送的数据 is ===============00000000000000000 "+paraStr);
		
		//post.setFollowRedirects(false);
		String result=null;
		StringBuffer resultBuffer = new StringBuffer();
		try{
			//client.getParams().setContentCharset("GBK");

			client.executeMethod(post);
			
			BufferedReader in = new BufferedReader(new InputStreamReader(post.getResponseBodyAsStream(),post.getResponseCharSet()));
			//System.out.println("post.getResponseCharSet() is0000000000000000 "+post.getResponseCharSet());
			String inputLine = null;
			while((inputLine=in.readLine())!=null){
				resultBuffer.append(inputLine);
				resultBuffer.append("\n");
			}
			in.close();
			//iso-8859-1 is the default reading encode
			result=HttpClientConnect.ConverterStringCode(resultBuffer.toString(),post.getResponseCharSet(),encode);
			//System.out.println("result is ===============$$$$$$$$$$$$$$$$$$$$$ "+result);
		}catch(Exception e){
			result="";
			//System.out.println("httpclient error : "+url);
			logger.info("httpclient error : "+url);
			e.printStackTrace();
		}finally{
			post.releaseConnection();
		}
		//System.out.println("result is ===============@@@@@@@@@@@ "+result);
		return result;
	}
	
	//编码转换
	public static String ConverterStringCode(String source,String srcEncode,String destEncode){
		if(source!=null){
			try{
				if(!srcEncode.equals("")&&!destEncode.equals("")){
					return new String(source.getBytes(srcEncode),destEncode);
				}else{
					return source.toString();
				}
			
			}catch(UnsupportedEncodingException e){
				//TODOAuto-generatedcatchblock
				//e.printStackTrace();
//				Operlog.toError("com.zhrt.csvc.util.HttpClientConnect.ConverterStringCode()", ErrorCode.errorDesc(ErrorCode.ERROR_COVER_CODE_ERROR), ErrorCode.errorDesc(ErrorCode.ERROR_COVER_CODE_ERROR), e.toString());
				return "";
			}
		}else{
				return"";
		} 
	}
	
	public static BufferedReader getResponseStreamWithHttpClient(String url,String encode,String paraStr){
		HttpClient client = new HttpClient(manager);
		BufferedReader in = null;
		if(!initialed){
			HttpClientConnect.SetPara();
		}
		PostMethod post = new PostMethod(url);
		RequestEntity entity;
		try {
			entity = new StringRequestEntity(paraStr, "text/xml", "UTF-8");
			post.setRequestEntity(entity);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try{
			client.executeMethod(post);
			in = new BufferedReader(new InputStreamReader(post.getResponseBodyAsStream(),"UTF-8"));
		}catch(Exception e){
			logger.info("httpclient error : "+url);
			e.printStackTrace();
		}finally{
			//post.releaseConnection();
		}
		return in;
	}
	
	
	public static class UTF8PostMethod extends PostMethod{ 
		public UTF8PostMethod(String url){ 
			super(url); 
		}  
		public String getRequestCharSet() { 
			return "UTF-8"; 
		} 
	} 
	/**
	 * 获取请求来源ip
	 * @param request
	 * @return
	 */
	public static String getRemoteHost(HttpServletRequest request){
		String ip = request.getHeader("X-Real-IP");
		System.out.println("新网关：X-Real-IP 获取IP："+ip);
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
			ip = request.getHeader("X-Forwarded-For");
	        System.out.println("新网关：X-Forwarded-For 获取IP："+ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("Proxy-Client-IP");
            System.out.println("新网关：Proxy-Client-IP 获取IP："+ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("WL-Proxy-Client-IP");  
            System.out.println("新网关：WL-Proxy-Client-IP 获取IP："+ip);
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_CLIENT_IP");  
            System.out.println("新网关：HTTP_CLIENT_IP 获取IP："+ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
            System.out.println("新网关：HTTP_X_FORWARDED_FOR 获取IP："+ip);
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getRemoteAddr();  
            System.out.println("新网关：getRemoteAddr 获取IP："+ip);
        }
        if(ip==null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip) || "0:0:0:0:0:0:0:1".equals(ip)){
        	ip = "127.0.0.1";
        }
	    return ip;
	}
	
}
