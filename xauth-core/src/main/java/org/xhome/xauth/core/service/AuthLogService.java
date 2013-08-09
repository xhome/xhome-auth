package org.xhome.xauth.core.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.xhome.common.query.QueryBase;
import org.xhome.xauth.AuthLog;

/**
 * @project xauth-core
 * @author jhat
 * @email cpf624@126.com
 * @date Jan 3, 201312:54:01 PM
 */
@Service
public interface AuthLogService {
	
	public int logAuth(AuthLog authLog);
	
	public List<AuthLog> getAuthLogs();
	
	public List<AuthLog> getAuthLogs(QueryBase query);
	
	public long countAuthLogs();
	
	public long countAuthLogs(QueryBase query);
	
}
