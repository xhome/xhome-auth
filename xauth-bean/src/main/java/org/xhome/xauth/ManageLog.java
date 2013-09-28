package org.xhome.xauth;

import org.xhome.common.Base;

/**
 * @project xauth-bean
 * @author jhat
 * @email cpf624@126.com
 * @date Aug 9, 201311:12:25 PM
 * @description type 0 ~ 100 are reversed for xauth
 */
public class ManageLog extends Base {

	private static final long	serialVersionUID	= -1195041078734813285L;
	private String  content; // 内容描述
	private Short 	action;   // 动作 0:Add, 1:Update, 2: Remove, 3: Search...
	private Short 	type; 	  // 类型 0: 角色, 1: 用户, 2: 用户认证日志, 3: 管理日志
	private Long 	obj; 	  // 操作对象
	
	public final static short TYPE_ROLE = 1;
	public final static short TYPE_USER = 2;
	public final static short TYPE_USER_ROLE = 3;
	public final static short TYPE_AUTH_LOG = 4;
	public final static short TYPE_MANAGE_LOG = 5;
	
	public ManageLog () {}
	
	public ManageLog (Short action, Short type, Long obj, Long user) {
		this.setAction(action);
		this.setType(type);
		this.setObj(obj);
		this.setOwner(user);
	}
	
	public ManageLog (String content, Short action, Short type, Long obj, Long user) {
		this.setContent(content);
		this.setAction(action);
		this.setType(type);
		this.setObj(obj);
		this.setOwner(user);
	}
	
	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}
	
	public void setAction(Short action) {
		this.action = action;
	}
	
	public Short getAction() {
		return action;
	}
	
	public void setType(Short type) {
		this.type = type;
	}
	
	public Short getType() {
		return type;
	}
	
	public void setObj(Long obj) {
		this.obj = obj;
	}
	
	public Long getObj() {
		return obj;
	}

}
