package org.xhome.xauth.core.dao;

import java.util.List;

import org.xhome.common.query.QueryBase;
import org.xhome.xauth.AuthLog;

/**
 * @project xauth-core
 * @author jhat
 * @email cpf624@126.com
 * @date Jan 3, 201312:56:08 PM
 */
public interface AuthLogDAO {
	
	public int addAuthLog(AuthLog authLog);
	
	public List<AuthLog> queryAuthLogs(QueryBase query);
	
	public long countAuthLogs(QueryBase query);
	
}
