package org.xhome.xauth.core.dao;

import java.util.List;

import org.xhome.db.query.QueryBase;
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
	
	/**
	 * 统计用户从指定时间到现在的认证失败次数
	 * @param authLog.user 待查询的用户信息
	 * @param authLog.created 指定的查询时间
	 * @return 返回认证失败次数
	 */
	public long countFailureAuth(AuthLog authLog);
	
}
