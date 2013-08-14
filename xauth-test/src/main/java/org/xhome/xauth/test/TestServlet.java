package org.xhome.xauth.test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.xhome.http.util.ResponseUtils;
import org.xhome.xauth.Role;
import org.xhome.xauth.User;
import org.xhome.xauth.core.service.RoleService;

/**
 * @project xauth-test
 * @author jhat
 * @email cpf624@126.com
 * @date Aug 13, 201310:24:47 PM
 * @description 
 */
public class TestServlet extends HttpServlet {

	private static final long	serialVersionUID	= 5235204662964310457L;

	@Autowired
	private RoleService roleService;
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User oper = new User("jhat");
		oper.setId(12L);
		final Role role = roleService.getRole(oper, 1);
		Map<String, Role> data = new HashMap<String, Role>();
		data.put("role", role);
		ResponseUtils.responseJSON(response, "Test Servlet", data);
	}
}
