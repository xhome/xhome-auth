package org.xhome.xauth.core.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.xhome.db.query.QueryBase;
import org.xhome.xauth.AuthLog;
import org.xhome.xauth.User;

/**
 * @project xauth-core
 * @author jhat
 * @email cpf624@126.com
 * @date Jan 3, 201312:54:01 PM
 */
@Service
public interface AuthLogService {
	
	public int logAuth(AuthLog authLog);
	
	public List<AuthLog> getAuthLogs(User oper);
	
	public List<AuthLog> getAuthLogs(User oper, QueryBase query);
	
	public long countAuthLogs(User oper);
	
	public long countAuthLogs(User oper, QueryBase query);
	
}
