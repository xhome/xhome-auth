package org.xhome.xauth.core.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.xhome.common.constant.Action;
import org.xhome.common.constant.Status;
import org.xhome.db.query.QueryBase;
import org.xhome.xauth.AuthLog;
import org.xhome.xauth.ManageLog;
import org.xhome.xauth.User;
import org.xhome.xauth.core.dao.AuthLogDAO;
import org.xhome.xauth.core.listener.AuthLogManageListener;

/**
 * @project xauth-core
 * @author jhat
 * @email cpf624@126.com
 * @date Jan 3, 201312:55:05 PM
 */
@Service
public class AuthLogServiceImpl implements AuthLogService {
	
	@Autowired(required = false)
	private AuthLogDAO	authLogDAO;
	@Autowired(required = false)
	private ManageLogService manageLogService;
	@Autowired(required = false)
	private List<AuthLogManageListener> authLogManageListeners;
	
	private Logger		logger;
	
	public AuthLogServiceImpl() {
		logger = LoggerFactory.getLogger(AuthLogService.class);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int logAuth(AuthLog authLog) {
		authLog.setCreated(new Timestamp(System.currentTimeMillis()));
		int r = authLogDAO.addAuthLog(authLog) == 1 ? Status.SUCCESS : Status.ERROR;
		
		if (logger.isDebugEnabled()) {
			if (r == Status.SUCCESS) {
				logger.debug("success to add auth log for {}", authLog.getUser().getName());
			} else {
				logger.debug("fail to add auth log for {}", authLog.getUser().getName());
			}
		}
		
		return r;
	}
	
	@Override
	public List<AuthLog> getAuthLogs(User oper) {
		return getAuthLogs(oper, null);
	}
	
	@Override
	public List<AuthLog> getAuthLogs(User oper, QueryBase query) {
		if (!this.beforeAuthLogManage(oper, Action.QUERY, null, query)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to query authLogs, but it's blocked");
			}
			
			this.logManage(null, Action.QUERY, null, Status.BLOCKED, oper);
			this.afterAuthLogManage(oper, Action.QUERY, Status.BLOCKED, null, query);
			return null;
		}
		
		List<AuthLog> authLogs = authLogDAO.queryAuthLogs(query);
		if (query != null) {
			query.setResults(authLogs);
			long total = authLogDAO.countAuthLogs(query);
			query.setTotalRow(total);
		}
		
		if (logger.isDebugEnabled()) {
			if (query != null) {
				logger.debug("query user auth logs with parameters {}", query.getParameters());
			} else {
				logger.debug("query user auth logs");
			}
		}

		this.logManage(null, Action.QUERY, null, Status.SUCCESS, oper);
		this.afterAuthLogManage(oper, Action.QUERY, Status.SUCCESS, null, query);
		return authLogs;
	}
	
	@Override
	public long countAuthLogs(User oper) {
		return countAuthLogs(oper, null);
	}
	
	@Override
	public long countAuthLogs(User oper, QueryBase query) {
		if (!this.beforeAuthLogManage(oper, Action.COUNT, null, query)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to count authLogs, but it's blocked");
			}
			
			this.logManage(null, Action.COUNT, null, Status.BLOCKED, oper);
			this.afterAuthLogManage(oper, Action.COUNT, Status.BLOCKED, null, query);
			return -1;
		}
		
		long c = authLogDAO.countAuthLogs(query);
		if (logger.isDebugEnabled()) {
			if (query != null) {
				logger.debug("count user auth logs with parameters {} of {}", query.getParameters(), c);
			} else {
				logger.debug("count user auth logs of {}", c);
			}
		}

		this.logManage(null, Action.COUNT, null, Status.SUCCESS, oper);
		this.afterAuthLogManage(oper, Action.COUNT, Status.SUCCESS, null, query);
		return c;
	}
	
	private void logManage(String content, Short action, Long obj, Short status, User oper) {
		ManageLog manageLog = new ManageLog(ManageLog.MANAGE_LOG_XAUTH, content, action, ManageLog.TYPE_AUTH_LOG, obj, oper == null ? null : oper.getId());
		manageLog.setStatus(status);
		manageLogService.logManage(manageLog);
	}
	
	private boolean beforeAuthLogManage(User oper, short action, AuthLog authLog, Object ...args) {
		if (authLogManageListeners != null) {
			for (AuthLogManageListener listener : authLogManageListeners) {
				if (!listener.beforeAuthLogManage(oper, action, authLog, args)) {
					return false;
				}
			}
		}
		return true;
	}
	
	private void afterAuthLogManage(User oper, short action, short result, AuthLog authLog, Object ...args) {
		if (authLogManageListeners != null) {
			for (AuthLogManageListener listener : authLogManageListeners) {
				listener.afterAuthLogManage(oper, action, result, authLog, args);
			}
		}
	}
	
	public void setAuthLogDAO(AuthLogDAO authLogDAO) {
		this.authLogDAO = authLogDAO;
	}
	
	public AuthLogDAO getAuthLogDAO() {
		return this.authLogDAO;
	}

	public void setManageLogService(ManageLogService manageLogService) {
		this.manageLogService = manageLogService;
	}
	
	public ManageLogService getManageLogService() {
		return this.manageLogService;
	}
	
	public void setAuthLogManageListeners(List<AuthLogManageListener> authLogManageListeners) {
		this.authLogManageListeners = authLogManageListeners;
	}

	public List<AuthLogManageListener> getAuthLogManageListeners() {
		return authLogManageListeners;
	}
	
	public void registerAuthLogManageListener(AuthLogManageListener authLogManageListener) {
		if (authLogManageListeners == null) {
			authLogManageListeners = new ArrayList<AuthLogManageListener>();
		}
		authLogManageListeners.add(authLogManageListener);
	}
	
}
