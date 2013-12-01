package org.xhome.xauth.web.freemarker;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xhome.xauth.Config;
import org.xhome.xauth.User;
import org.xhome.xauth.core.service.ConfigService;
import org.xhome.xauth.web.util.AuthUtils;

import freemarker.core.Environment;
import freemarker.ext.servlet.HttpRequestHashModel;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateScalarModel;

/**
 * @project xauth-web
 * @author jhat
 * @email cpf624@126.com
 * @date Nov 21, 20139:46:21 PM
 * @describe
 */
@Component
public class ConfigMethodModel implements TemplateMethodModelEx {

	@Autowired(required = false)
	private ConfigService configService;

	@SuppressWarnings("rawtypes")
	@Override
	public Object exec(List arguments) throws TemplateModelException {
		if (arguments.size() == 0) {
			throw new TemplateModelException("Missing parameters.");
		}
		String item = ((TemplateScalarModel) arguments.get(0)).getAsString();
		Environment env = Environment.getCurrentEnvironment();
		HttpRequestHashModel req = (HttpRequestHashModel) env
				.getVariable("Request");
		HttpServletRequest request = req.getRequest();
		User user = AuthUtils.getCurrentUser(request);
		Config config = configService.getConfig(user, item);
		return config != null ? config.getValue() : null;
	}

	public void setConfigService(ConfigService configService) {
		this.configService = configService;
	}

}
