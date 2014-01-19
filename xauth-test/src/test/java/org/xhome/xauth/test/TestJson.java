package org.xhome.xauth.test;

import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.xhome.xauth.User;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @project xauth-test
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Dec 30, 201312:08:45 AM
 * @describe 
 */
public class TestJson {
	
	@Test
	public void testParse() {
		ObjectMapper mapper = new ObjectMapper();
		try {
			User user = mapper.readValue("{\"name\":\"jhat\"}", User.class);
			System.out.println(user.getName());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
