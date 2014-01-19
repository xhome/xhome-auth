package org.xhome.xauth.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xhome.spring.mvc.extend.bind.annotation.RequestAttribute;
import org.xhome.spring.mvc.extend.bind.annotation.RequestJsonParam;
import org.xhome.xauth.User;

/**
 * @project xauth-test
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Dec 29, 201311:45:02 PM
 * @describe 
 */
@Controller
public class TestSpringExtend {
	
	@ResponseBody
	@RequestMapping(value = "testp", method = RequestMethod.GET)
	public Object testP(@RequestAttribute("user") User user) {
		return user;
	}
	
	@ResponseBody
	@RequestMapping(value = "testj", method = RequestMethod.GET)
	public Object testJ(@RequestJsonParam("user") User user) {
		return user;
	}

}
