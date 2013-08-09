package org.xhome.xauth;

import org.xhome.common.Base;

/**
 * @project xauth-bean
 * @author jhat
 * @email cpf624@126.com
 * @date Aug 9, 201311:12:32 PM
 * @description 用户认证信息
 */
public class AuthLog extends Base {

	private static final long	serialVersionUID	= 3418695337307713490L;
	private User 	user; 	 // 用户名
	private String 	method;  // 认证方式
	private String 	address; // 访问地址(IPv4/IPv6)
	private Short 	agent;   // 0:Other,1:Chrome, 2:Firefox, 3:IE, 4:Android
	private String 	number;  // 设备编号
	
	public AuthLog () {}
	
	public AuthLog (User user, String method, String address, Short agent, String number) {
		this.setUser(user);
		this.setMethod(method);
		this.setAddress(address);
		this.setAgent(agent);
		this.setNumber(number);
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Short getAgent() {
		return agent;
	}

	public void setAgent(Short agent) {
		this.agent = agent;
	}
	
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
