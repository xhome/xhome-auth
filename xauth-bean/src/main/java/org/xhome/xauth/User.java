package org.xhome.xauth;

import java.util.ArrayList;
import java.util.List;

import org.xhome.common.Base;

/**
 * @project xauth-bean
 * @author jhat
 * @email cpf624@126.com
 * @date Jun 14, 2013 9:29:41 PM
 * @description 用户信息
 */
public class User extends Base {

	private static final long serialVersionUID = -5873055257824169330L;

	private String name; // 用户名
	private String nick; // 昵称
	private String email; // 邮箱
	private String password; // 密码
	private String method = "DEFAULT"; // 认证方式
	private List<Role> roles; // 用户角色

	public User() {
	}

	public User(String name) {
		this.setName(name);
	}

	public User(String name, String password) {
		this(name);
		this.setPassword(password);
	}

	public void setName(String name) {
		this.name = name;
		if (this.nick == null) {
			this.setNick(name);
		}
	}

	public String getName() {
		return name;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getNick() {
		return nick;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getMethod() {
		return method;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void addRole(Role role) {
		if (roles == null) {
			roles = new ArrayList<Role>();
		}
		roles.add(role);
	}

	public void removeRole(Role role) {
		if (roles != null) {
			roles.remove(role);
		}
	}

	public boolean hasRole(Role role) {
		return roles != null ? roles.contains(role) : false;
	}

	// @Override
	// public boolean equals(Object obj) {
	// if (obj != null && obj instanceof User) {
	// User user = (User) obj;
	// return name.equals(user.name);
	// }
	// return false;
	// }

}
