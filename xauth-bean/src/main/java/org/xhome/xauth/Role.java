package org.xhome.xauth;

import org.xhome.common.Base;

/**
 * @project xauth-bean
 * @author jhat
 * @email cpf624@126.com
 * @date Jun 14, 2013 9:29:48 PM
 * @description 角色信息
 */
public class Role extends Base {

	private static final long serialVersionUID = 1426771516880127921L;
	
	private String name = ""; // 角色名称
	
	public Role() {}
	
	public Role(String name) {
		this.setName(name);
	}
	
	public void setName(String name) {
		this.name = name != null ? name : "";
	}
	
	public String getName() {
		return name;
	}
	
//	@Override
//	public boolean equals(Object obj) {
//		if (obj != null && obj instanceof Role) {
//			Role role = (Role) obj;
//			return name.equals(role.name);
//		}
//		return false;
//	}

}
