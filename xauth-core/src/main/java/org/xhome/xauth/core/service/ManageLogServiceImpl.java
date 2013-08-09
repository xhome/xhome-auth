package org.xhome.xauth.core.service;

import java.sql.Timestamp;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.xhome.common.constant.Status;
import org.xhome.common.query.QueryBase;
import org.xhome.xauth.ManageLog;
import org.xhome.xauth.core.dao.ManageLogDAO;

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
	public List<ManageLog> getManageLogs() {
		return getManageLogs(null);
	}
	
	@Override
	public List<ManageLog> getManageLogs(QueryBase query) {
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
		
		return manageLogs;
	}
	
	@Override
	public long countManageLogs() {
		return countManageLogs(null);
	}
	
	@Override
	public long countManageLogs(QueryBase query) {
		long c = manageLogDAO.countManageLogs(query);
		if (logger.isDebugEnabled()) {
			if (query != null) {
				logger.debug("count user manage logs with parameters {} of {}", query.getParameters(), c);
			} else {
				logger.debug("count user manage logs of {}", c);
			}
		}
		return c;
	}
	
	public void setManageLogDAO(ManageLogDAO manageLogDAO) {
		this.manageLogDAO = manageLogDAO;
	}
	
}
