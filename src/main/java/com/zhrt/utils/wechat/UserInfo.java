package com.zhrt.utils.wechat;

import java.util.List;

/**
 * 通过网页授权获取用户信息
 * @author Administrator
 *
 */
public class UserInfo {
	
	//用户标识
	private String openid;
	
	//用户昵称
	private String nick_name;
	
	//性别(1 是男性 2 是女性  0 是未知)
	private int sex;
	
	//国家
	private String country;
	
	//省份
	private String province;
	
	//城市
	private String city;
	
	//头像
	private String head_img_url;
	
	//用户特权信息
	private List<String> privilege_list;

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getNick_name() {
		return nick_name;
	}

	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getHead_img_url() {
		return head_img_url;
	}

	public void setHead_img_url(String head_img_url) {
		this.head_img_url = head_img_url;
	}

	public List<String> getPrivilege_list() {
		return privilege_list;
	}

	public void setPrivilege_list(List<String> privilege_list) {
		this.privilege_list = privilege_list;
	}
}
