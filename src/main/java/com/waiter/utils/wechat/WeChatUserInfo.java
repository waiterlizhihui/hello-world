package com.waiter.utils.wechat;

import java.io.Serializable;
import java.util.List;

/**
 * 通过网页授权获取用户信息
 * @author Administrator
 *
 */
public class WeChatUserInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	//用户标识
	private String openid;
	
	//用户昵称
	private String nickname;
	
	//性别(1 是男性 2 是女性  0 是未知)
	private int sex;

	//语言
	private String language;

	//城市
	private String city;

	//省份
	private String province;
	
	//国家
	private String country;

	//头像
	private String headimgurl;
	
	//用户特权信息
	private List<String> privilege;

	private String unionid;

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getHeadimgurl() {
		return headimgurl;
	}

	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}

	public List<String> getPrivilege() {
		return privilege;
	}

	public void setPrivilege(List<String> privilege) {
		this.privilege = privilege;
	}

	public String getUnionid() {
		return unionid;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

	@Override
	public String toString() {
		return "UserInfo{" +
				"openid='" + openid + '\'' +
				", nickname='" + nickname + '\'' +
				", sex=" + sex +
				", language='" + language + '\'' +
				", city='" + city + '\'' +
				", province='" + province + '\'' +
				", country='" + country + '\'' +
				", headimgurl='" + headimgurl + '\'' +
				", privilege=" + privilege +
				", unionid='" + unionid + '\'' +
				'}';
	}
}
