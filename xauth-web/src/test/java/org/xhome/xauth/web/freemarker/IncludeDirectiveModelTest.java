package org.xhome.xauth.web.freemarker;

import java.io.File;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * @project xauth-web
 * @author jhat
 * @email cpf624@126.com
 * @homepage http://pfchen.org
 * @date Feb 12, 2014
 * @describe
 */
public class IncludeDirectiveModelTest {

	@Test
	public void test() {
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("include", new IncludeDirectiveModel());
		File file = new File("src/test/resources");
		FreeMarkertTemplateUtils.processTemplate(file.getAbsolutePath(),
				"t/include.ftl", root, new OutputStreamWriter(System.out));
	}

}
