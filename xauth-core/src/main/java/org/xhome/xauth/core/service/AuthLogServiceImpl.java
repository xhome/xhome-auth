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
import org.xhome.xauth.AuthLog;
import org.xhome.xauth.core.dao.AuthLogDAO;

/**
 * @project xauth-core
 * @author jhat
 * @email cpf624@126.com
 * @date Jan 3, 201312:55:05 PM
 */
@Service
public class AuthLogServiceImpl implements AuthLogService {
	
	@Autowired
	private AuthLogDAO	authLogDAO;
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
	public List<AuthLog> getAuthLogs() {
		return getAuthLogs(null);
	}
	
	@Override
	public List<AuthLog> getAuthLogs(QueryBase query) {
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
		
		return authLogs;
	}
	
	@Override
	public long countAuthLogs() {
		return countAuthLogs(null);
	}
	
	@Override
	public long countAuthLogs(QueryBase query) {
		long c = authLogDAO.countAuthLogs(query);
		if (logger.isDebugEnabled()) {
			if (query != null) {
				logger.debug("count user auth logs with parameters {} of {}", query.getParameters(), c);
			} else {
				logger.debug("count user auth logs of {}", c);
			}
		}
		return c;
	}
	
	public void setAuthLogDAO(AuthLogDAO authLogDAO) {
		this.authLogDAO = authLogDAO;
	}
	
}
