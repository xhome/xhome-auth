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
import org.xhome.common.query.QueryBase;
import org.xhome.xauth.ManageLog;
import org.xhome.xauth.User;
import org.xhome.xauth.core.dao.ManageLogDAO;
import org.xhome.xauth.core.listener.ManageLogManageListener;

/**
 * @project xauth-core
 * @author jhat
 * @email cpf624@126.com
 * @date Jan 3, 201312:55:05 PM
 */
@Service
public class ManageLogServiceImpl implements ManageLogService {
	
	@Autowired
	private ManageLogDAO	manageLogDAO;
	private List<ManageLogManageListener> manageLogManageListeners;
	
	private Logger		logger;
	
	public ManageLogServiceImpl() {
		logger = LoggerFactory.getLogger(ManageLogService.class);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int logManage(ManageLog manageLog) {
		manageLog.setCreated(new Timestamp(System.currentTimeMillis()));
		int r = manageLogDAO.addManageLog(manageLog) == 1 ? Status.SUCCESS : Status.ERROR;
		
		if (logger.isDebugEnabled()) {
			if (r == Status.SUCCESS) {
				logger.debug("success to add manage log for user {} of {} {} {}",
						manageLog.getOwner(), manageLog.getAction(), manageLog.getType(), manageLog.getObj());
			} else {
				logger.debug("fail to add manage log for user {} of {} {} {}",
						manageLog.getOwner(), manageLog.getAction(), manageLog.getType(), manageLog.getObj());
			}
		}
		
		return r;
	}
	
	@Override
	public List<ManageLog> getManageLogs(User oper) {
		return getManageLogs(oper, null);
	}
	
	@Override
	public List<ManageLog> getManageLogs(User oper, QueryBase query) {
		if (!this.beforeManageLogManage(oper, Action.QUERY, null, query)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to query manageLogs, but it's blocked");
			}
			
			this.logManage(null, Action.QUERY, null, Status.BLOCKED, oper);
			this.afterManageLogManage(oper, Action.QUERY, Status.BLOCKED, null, query);
			return null;
		}
		
		List<ManageLog> manageLogs = manageLogDAO.queryManageLogs(query);
		if (query != null) {
			query.setResults(manageLogs);
			long total = manageLogDAO.countManageLogs(query);
			query.setTotalRow(total);
		}
		
		if (logger.isDebugEnabled()) {
			if (query != null) {
				logger.debug("query user manage logs with parameters {}", query.getParameters());
			} else {
				logger.debug("query user manage logs");
			}
		}

		this.logManage(null, Action.QUERY, null, Status.SUCCESS, oper);
		this.afterManageLogManage(oper, Action.QUERY, Status.SUCCESS, null, query);
		return manageLogs;
	}
	
	@Override
	public long countManageLogs(User oper) {
		return countManageLogs(oper, null);
	}
	
	@Override
	public long countManageLogs(User oper, QueryBase query) {
		if (!this.beforeManageLogManage(oper, Action.COUNT, null, query)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to count manageLogs, but it's blocked");
			}
			
			this.logManage(null, Action.COUNT, null, Status.BLOCKED, oper);
			this.afterManageLogManage(oper, Action.COUNT, Status.BLOCKED, null, query);
			return -1;
		}
		
		long c = manageLogDAO.countManageLogs(query);
		if (logger.isDebugEnabled()) {
			if (query != null) {
				logger.debug("count user manage logs with parameters {} of {}", query.getParameters(), c);
			} else {
				logger.debug("count user manage logs of {}", c);
			}
		}
		
		this.logManage(null, Action.COUNT, null, Status.SUCCESS, oper);
		this.afterManageLogManage(oper, Action.COUNT, Status.SUCCESS, null, query);
		return c;
	}
	
	private void logManage(String content, Short action, Long obj, Short status, User oper) {
		ManageLog manageLog = new ManageLog(content, action, ManageLog.TYPE_MANAGE_LOG, obj, oper == null ? null : oper.getId());
		manageLog.setStatus(status);
		this.logManage(manageLog);
	}
	
	private boolean beforeManageLogManage(User oper, short action, ManageLog manageLog, Object ...args) {
		if (manageLogManageListeners != null) {
			for (ManageLogManageListener listener : manageLogManageListeners) {
				if (!listener.beforeManageLogManage(oper, action, manageLog, args)) {
					return false;
				}
			}
		}
		return true;
	}
	
	private void afterManageLogManage(User oper, short action, short result, ManageLog manageLog, Object ...args) {
		if (manageLogManageListeners != null) {
			for (ManageLogManageListener listener : manageLogManageListeners) {
				listener.afterManageLogManage(oper, action, result, manageLog, args);
			}
		}
	}
	
	public void setManageLogDAO(ManageLogDAO manageLogDAO) {
		this.manageLogDAO = manageLogDAO;
	}
	
	public ManageLogDAO getManageLogDAO() {
		return this.manageLogDAO;
	}
	
	public void setManageLogManageListeners(List<ManageLogManageListener> manageLogManageListeners) {
		this.manageLogManageListeners = manageLogManageListeners;
	}

	public List<ManageLogManageListener> getManageLogManageListeners() {
		return manageLogManageListeners;
	}
	
	public void registerManageLogManageListener(ManageLogManageListener manageLogManageListener) {
		if (manageLogManageListeners == null) {
			manageLogManageListeners = new ArrayList<ManageLogManageListener>();
		}
		manageLogManageListeners.add(manageLogManageListener);
	}
	
}
