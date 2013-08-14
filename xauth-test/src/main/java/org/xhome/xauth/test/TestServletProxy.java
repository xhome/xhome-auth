package org.xhome.xauth.test;

import java.io.IOException;

import javax.servlet.GenericServlet;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @project xauth-test
 * @author jhat
 * @email cpf624@126.com
 * @date Aug 13, 201310:32:28 PM
 * @description 
 */
public class TestServletProxy extends GenericServlet {
	
	private static final long	serialVersionUID	= 6688053945665991267L;

	private Servlet proxy;

	@Override
	public void init() throws ServletException {
		WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(this.getServletContext());
		this.proxy = (Servlet) wac.getBean(this.getServletName());
		proxy.init(this.getServletConfig());
	}
	
	@Override
	public void service(ServletRequest req, ServletResponse res)
			throws ServletException, IOException {
		this.proxy.service(req, res);
	}

}
