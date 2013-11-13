package org.xhome.xauth;

import org.xhome.common.Base;

/**
 * @project xauth-bean
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Nov 14, 201312:21:01 AM
 * @describe 
 */
public class Config extends Base {

	private static final long serialVersionUID = -6912584504976391286L;

	private int	   category; // 配置分类
	private String item; // 配置项
	private String display; // 显示名称
	private String value; // 配置值
	
	public final static int CONFIG_COMMON = 0;
	public final static int CONFIG_XAUTH = 1;
	public final static int CONFIG_XBLOG = 2;
	public final static int CONFIG_XSMS = 3;
	public final static int CONFIG_XSTATISTICS = 4;
	
	public Config() {}
	
	public Config(String item, String value) {
		this(CONFIG_COMMON, item, item, value);
	}
	
	public Config(int category, String item, String value) {
		this(category, item, item, value);
	}
	
	public Config(String item, String display, String value) {
		this(CONFIG_COMMON, item, display, value);
	}
	
	public Config(int category, String item, String display, String value) {
		this.setCategory(category);
		this.setItem(item);
		this.setDisplay(display);
		this.setValue(value);
	}
	
	public int getCategory() {
		return category;
	}
	
	public void setCategory(int category) {
		this.category = category;
	}
	
	public String getItem() {
		return item;
	}
	
	public void setItem(String item) {
		this.item = item;
	}
	
	public String getDisplay() {
		return display;
	}
	
	public void setDisplay(String display) {
		this.display = display;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}

}
