/** ======================================
 * Copyright © 2017 北京展鸿软通科技股份有限公司. All rights reserved.
 * Date：2017年8月15日 上午11:53:03
 * Author：lichuanyou
 * Version：1.0
 * =========Modification History==========
 * Date          Name        Description
 * 2017年8月15日       lichuanyou     创建LogicException类
 */
package com.waiter.utils.common;

/**
 * @Title 业务逻辑异常
 * @Description 业务逻辑处理环节出现校验异常时，抛出
 * @date 2017年8月15日 上午11:53:03
 * @author lichuanyou
 * @version 1.0
 */
public class LogicException extends Exception {
	/**服务名*/
	private String serviceCode;
	/**错误码*/
	private String errorCode;
	private static final long serialVersionUID = 1L;

	public LogicException(){
		super();
	}
	
	public LogicException(String logicError){
		super(logicError);
	}
	
	/**
	 * 
	 * @param serviceCode---服务码
	 * @param errorCode 错误码
	 * @param logicError----异常信息
	 */
	public LogicException(String serviceCode, String errorCode, String logicError){
		super(logicError);
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
}
