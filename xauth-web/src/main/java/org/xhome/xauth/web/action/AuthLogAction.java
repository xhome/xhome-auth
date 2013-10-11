package org.xhome.xauth.web.action;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xhome.common.constant.Status;
import org.xhome.db.query.QueryBase;
import org.xhome.http.response.Result;
import org.xhome.validator.CommonValidator;
import org.xhome.validator.ValidatorMapping;
import org.xhome.xauth.User;
import org.xhome.xauth.core.service.AuthLogService;
import org.xhome.xauth.web.util.AuthUtils;

/**
 * @project xauth-web
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Oct 11, 20139:51:31 PM
 * @describe 
 */
@Controller
public class AuthLogAction {

	@Autowired(required = false)
	private AuthLogService authLogService;
	private Logger logger = LoggerFactory.getLogger(AuthLogAction.class);
	private CommonValidator		commonValidator 	= new CommonValidator();
	private	ValidatorMapping	validatorMapping	= ValidatorMapping.getInstance();
	
	public final static String	RM_AUTH_LOG_QUERY	= "xauth/auth_log/query.do";
	public final static String	RM_AUTH_LOG_COUNT	= "xauth/auth_log/count.do";
	
	@InitBinder
	public void initBinder(HttpServletRequest request, WebDataBinder binder) {
		String uri = request.getRequestURI();
		commonValidator.setValidators(validatorMapping.getValidatorByUri(uri));
		binder.setValidator(commonValidator);
		if (logger.isDebugEnabled()) {
			logger.debug("init binder for " + uri);
		}
	}
	
	@ResponseBody
	@RequestMapping(value = RM_AUTH_LOG_QUERY, method = RequestMethod.GET)
	public Object getAuthLogs(QueryBase query, HttpServletRequest request) {
		User user = AuthUtils.getCurrentUser(request);
		String uname = user.getName();
		
		if (logger.isInfoEnabled()) {
			if (query != null) {
				logger.info("用户" + uname + "按条件" + query.getParameters() + "查询认证日志");
			} else {
				query = new QueryBase();
				logger.info("用户" + uname + "查询认证日志");
			}
		}
		authLogService.getAuthLogs(user, query);
		
		String msg = "条件查询认证日志";
		short status = Status.SUCCESS;
		
		Result r = new Result(status, msg, query);

		if (logger.isInfoEnabled()) {
			logger.info("[" + status + "]" + uname + msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_AUTH_LOG_COUNT, method = RequestMethod.GET)
	public Object countAuthLogs(QueryBase query, HttpServletRequest request) {
		User user = AuthUtils.getCurrentUser(request);
		String uname = user.getName();
		
		if (logger.isInfoEnabled()) {
			if (query != null) {
				logger.info("用户" + uname + "按条件" + query.getParameters() + "统计认证日志");
			} else {
				logger.info("用户" + uname + "统计认证日志");
			}
		}
		long count = authLogService.countAuthLogs(user, query);
		
		String msg = "条件统计认证日志，共" + count;
		short status = Status.SUCCESS;
		
		Result r = new Result(status, msg, count);

		if (logger.isInfoEnabled()) {
			logger.info("[" + status + "]" + uname + msg);
		}
		
		return r;
	}

	public void setAuthLogService(AuthLogService authLogService) {
		this.authLogService = authLogService;
	}

	public AuthLogService getAuthLogService() {
		return authLogService;
	}
	
}
