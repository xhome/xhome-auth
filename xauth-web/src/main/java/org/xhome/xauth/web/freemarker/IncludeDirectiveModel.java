package org.xhome.xauth.web.freemarker;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.xhome.util.StringUtils;

import freemarker.cache.TemplateLoader;
import freemarker.core.Environment;
import freemarker.core.ParseException;
import freemarker.core._DelayedGetMessage;
import freemarker.core._DelayedJQuote;
import freemarker.core._MiscTemplateException;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateScalarModel;

/**
 * @project xauth-web
 * @author jhat
 * @email cpf624@126.com
 * @homepage http://pfchen.org
 * @date Feb 12, 2014
 * @describe 模板文件引入扩展，与<code><#include></code>
 *           不同的是，该扩展优先使用上级目录的模板文件，如果上级目录的模板文件不存在才继续向下查找。 如在
 *           <code>xauth/user/login.ftl</code>中使用
 *           <code><@include file="copyright.ftl"/></code> ，则对
 *           <code>copyright.ftl</code>的查找顺序为：
 *           <ul>
 *           <li>copyright.ftl</li>
 *           <li>xauth/copyright.ftl</li>
 *           <li>xauth/user/copyright.ftl</li>
 *           </ul>
 *           这样的好处是第三方库可以在自己的目录下提供默认的模板文件，而使用第三方库时可以在上级目录重载对应的模板文件。
 */
@Component
public class IncludeDirectiveModel implements TemplateDirectiveModel {

	@SuppressWarnings("rawtypes")
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		String templateFile = null, encoding = null;
		boolean parse = true;
		Configuration config = env.getConfiguration();

		// 获取模板文件名称
		TemplateScalarModel model = (TemplateScalarModel) params.get("file");
		if (model != null) {
			templateFile = model.getAsString().trim();
		}
		if (templateFile == null || StringUtils.isEmpty(templateFile)) {
			throw new TemplateModelException("missing template file param");
		}

		// 获取字符集，默认与环境变量中得一致
		model = (TemplateScalarModel) params.get("encoding");
		if (model != null) {
			encoding = model.getAsString().trim();
		}
		if (encoding == null || StringUtils.isEmpty(encoding)) {
			encoding = config.getDefaultEncoding();
		}

		// 获取是否需要解析模块文件参数，默认解析
		model = (TemplateScalarModel) params.get("parse");
		if (model != null) {
			if ("false".equalsIgnoreCase(model.getAsString().trim())) {
				parse = false;
			}
		}

		// 根据当前模板文件路径从跟目录开始查找需要引入的模板文件
		String templatePath = env.getTemplate().getName();
		if (templatePath.contains("/")) {
			String[] paths = templatePath.split("/");
			TemplateLoader loader = config.getTemplateLoader();
			String dir = "", templatePathFile = templateFile;
			for (int i = 0; i < paths.length - 1; i++) {
				if (loader.findTemplateSource(templatePathFile) != null) {
					break;
				}
				dir += paths[i] + "/";
				templatePathFile = dir + templateFile;
			}
			templateFile = templatePathFile;
		}

		// 加载模板文件
		Template template = null;
		try {
			template = env.getTemplateForInclusion(templateFile, encoding,
					parse);
		} catch (ParseException pe) {
			throw new _MiscTemplateException(pe, env, new Object[] {
					"Error parsing included template ",
					new _DelayedJQuote(templateFile), ":\n",
					new _DelayedGetMessage(pe) });
		} catch (IOException ioe) {
			throw new _MiscTemplateException(ioe, env, new Object[] {
					"Error reading included file ",
					new _DelayedJQuote(templateFile), ":\n",
					new _DelayedGetMessage(ioe) });
		}

		env.include(template);
	}
}
