package com.waiter.utils.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * http协议处理类 如接受requestbody 接收requestparam
 * 
 */
public class GatewayHttpUtils {

	private static Logger logger = LoggerFactory.getLogger(GatewayHttpUtils.class);

	/**
	 * 
	 * 获取请求参数中所有的信息
	 * 
	 * @param request
	 * @return
	 */
	public static Map<String, String> getAllRequestParam(final HttpServletRequest request, StringBuffer logStr) {
		Map<String, String> res = new HashMap<String, String>();
		Enumeration<?> temp = request.getParameterNames();
		if (null != temp) {
			while (temp.hasMoreElements()) {
				String en = (String) temp.nextElement();
				String value = request.getParameter(en);
				res.put(en, value);
				// 在报文上送时，如果字段的值为空，则不上送<下面的处理为在获取所有参数数据时，判断若值为空，则删除这个字段>
				if (res.get(en) == null || "".equals(res.get(en))) {
					// System.out.println("======为空的字段名===="+en);
					res.remove(en);
				}
			}
		}
		return res;
	}

	/**
	 * 
	 * 方法描述: 获取请求参数信息
	 * 
	 * @param request
	 * @return String
	 */
	public static String getByReqStreamParam(HttpServletRequest request, StringBuffer logStr) {
		StringBuffer reqInput = new StringBuffer();
		try {
			// 接收数据流
			BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
			String data = null;
			while ((data = br.readLine()) != null) {
				reqInput.append(data);
			}
			br.close();
			br = null;
			logStr.append(" 请求参数：" + reqInput);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("接口接受数据异常：err=" + e);
			return "";
		}
		return reqInput.toString();
	}

	/**
	 * 字符转化为对应map
	 * 
	 * @param digest
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static Map<String, String> stringToMap(String digest) throws UnsupportedEncodingException {
		Map<String, String> resultMap = new HashMap<String, String>();
		if (digest == null) {
			return null;
		}
		String[] split = digest.split("&");
		for (String aSplit : split) {
			String[] temp = aSplit.split("=");
			if (temp.length == 1) {
				resultMap.put(temp[0], "");
			}
			if (temp.length > 1) {
				String paramValue = URLDecoder.decode(temp[1], "UTF-8");
				resultMap.put(temp[0], paramValue);
			}
		}
		return resultMap;
	}

	/**
	 * 检测是否为移动端设备访问
	 * 
	 * @author : Cuichenglong
	 * @group : tgb8
	 * @Version : 1.00
	 * @Date : 2014-7-7 下午01:34:31
	 */

	// \b 是单词边界(连着的两个(字母字符 与 非字母字符) 之间的逻辑上的间隔),
	// 字符串在编译时会被转码一次,所以是 "\\b"
	// \B 是单词内部逻辑间隔(连着的两个字母字符之间的逻辑上的间隔)
	static String phoneReg = "\\b(ip(hone|od)|android|opera m(ob|in)i" + "|windows (phone|ce)|blackberry"
			+ "|s(ymbian|eries60|amsung)|p(laybook|alm|rofile/midp" + "|laystation portable)|nokia|fennec|htc[-_]"
			+ "|mobile|up.browser|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";
	static String tableReg = "\\b(ipad|tablet|(Nexus 7)|up.browser" + "|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";

	// 移动设备正则匹配：手机端、平板
	static Pattern phonePat = Pattern.compile(phoneReg, Pattern.CASE_INSENSITIVE);
	static Pattern tablePat = Pattern.compile(tableReg, Pattern.CASE_INSENSITIVE);

	/**
	 * 检测是否是移动设备访问
	 * 
	 * @Title: check
	 * @Date : 2014-7-7 下午01:29:07
	 * @param userAgent
	 *            浏览器标识
	 * @return true:移动设备接入，false:pc端接入
	 */
	public static boolean check(String userAgent) {
		if (null == userAgent) {
			userAgent = "";
		}
		userAgent = userAgent.toLowerCase();
		// 匹配
		Matcher matcherPhone = phonePat.matcher(userAgent);
		Matcher matcherTable = tablePat.matcher(userAgent);
		if (matcherPhone.find() || matcherTable.find()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取请求来源ip
	 * 
	 * @param request
	 * @return
	 */
	public static String getRemoteHost(HttpServletRequest request) {
		String ip = request.getHeader("X-Real-IP");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Forwarded-For");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip) || "0:0:0:0:0:0:0:1".equals(ip)) {
			ip = "127.0.0.1";
		}
		return ip;
	}

	public static boolean isWeChat(HttpServletRequest request) {
		String userAgent = request.getHeader("user-agent").toLowerCase();
		return userAgent != null && userAgent.indexOf("micromessenger") == -1 ? false : true;
	}

	/**
	 * 根据UA判断设备类型（待完成）
	 * @param request
	 * @return
	 */
	public static String deviceType(HttpServletRequest request) {
		return "wrong";
	}
}
