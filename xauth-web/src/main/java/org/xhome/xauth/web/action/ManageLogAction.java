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
import org.xhome.xauth.core.service.ManageLogService;
import org.xhome.xauth.web.util.AuthUtils;

/**
 * @project xauth-web
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Oct 11, 20139:51:31 PM
 * @describe 
 */
@Controller
public class ManageLogAction {

	@Autowired(required = false)
	private ManageLogService manageLogService;
	private Logger logger = LoggerFactory.getLogger(ManageLogAction.class);
	private CommonValidator		commonValidator 	= new CommonValidator();
	private	ValidatorMapping	validatorMapping	= ValidatorMapping.getInstance();
	
	public final static String	RM_MANAGE_LOG_QUERY	= "xauth/manage_log/query.do";
	public final static String	RM_MANAGE_LOG_COUNT	= "xauth/manage_log/count.do";
	
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
	@RequestMapping(value = RM_MANAGE_LOG_QUERY, method = RequestMethod.GET)
	public Object getManageLogs(QueryBase query, HttpServletRequest request) {
		User user = AuthUtils.getCurrentUser(request);
		String uname = user.getName();
		
		if (logger.isInfoEnabled()) {
			if (query != null) {
				logger.info("用户" + uname + "按条件" + query.getParameters() + "查询管理日志");
			} else {
				query = new QueryBase();
				logger.info("用户" + uname + "查询管理日志");
			}
		}
		manageLogService.getManageLogs(user, query);
		
		String msg = "条件查询管理日志";
		short status = Status.SUCCESS;
		
		Result r = new Result(status, msg, query);

		if (logger.isInfoEnabled()) {
			logger.info("[" + status + "]" + uname + msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_MANAGE_LOG_COUNT, method = RequestMethod.GET)
	public Object countManageLogs(QueryBase query, HttpServletRequest request) {
		User user = AuthUtils.getCurrentUser(request);
		String uname = user.getName();
		
		if (logger.isInfoEnabled()) {
			if (query != null) {
				logger.info("用户" + uname + "按条件" + query.getParameters() + "统计管理日志");
			} else {
				logger.info("用户" + uname + "统计管理日志");
			}
		}
		long count = manageLogService.countManageLogs(user, query);
		
		String msg = "条件统计管理日志，共" + count;
		short status = Status.SUCCESS;
		
		Result r = new Result(status, msg, count);

		if (logger.isInfoEnabled()) {
			logger.info("[" + status + "]" + uname + msg);
		}
		
		return r;
	}

	public void setManageLogService(ManageLogService manageLogService) {
		this.manageLogService = manageLogService;
	}

	public ManageLogService getManageLogService() {
		return manageLogService;
	}
	
}