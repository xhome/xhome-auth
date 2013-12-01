package org.xhome.xauth.core.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.xhome.common.constant.Status;
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
	
	/**
	 * 记录认证日志
	 * @param authLog 认证日志
	 * @return 返回记录结果，记录成功：{@link Status}.SUCCESS， 记录失败{@link Status}.ERROR
	 */
	public int logAuth(AuthLog authLog);
	
	/**
	 * 查询认证日志
	 * @param oper 执行该操作的用户信息
	 * @return
	 */
	public List<AuthLog> getAuthLogs(User oper);
	
	/**
	 * 查询认证日志
	 * @param oper 执行该操作的用户信息
	 * @param query 查询条件
	 * @return
	 */
	public List<AuthLog> getAuthLogs(User oper, QueryBase query);
	
	/**
	 * 统计认证日志
	 * @param oper 执行该操作的用户信息
	 * @return
	 */
	public long countAuthLogs(User oper);
	
	/**
	 * 统计认证日志
	 * @param oper 执行该操作的用户信息
	 * @param query 统计条件
	 * @return
	 */
	public long countAuthLogs(User oper, QueryBase query);
	
	/**
	 * 统计用户从指定时间到现在的认证失败次数
	 * @param authLog.user 待查询的用户信息
	 * @param authLog.created 指定的查询时间
	 * @return 返回认证失败次数
	 */
	public long countFailureAuth(AuthLog authLog);
	
}
