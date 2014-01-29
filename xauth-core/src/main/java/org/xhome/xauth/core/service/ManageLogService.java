package org.xhome.xauth.core.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.xhome.common.constant.Status;
import org.xhome.db.query.QueryBase;
import org.xhome.xauth.ManageLog;
import org.xhome.xauth.User;

/**
 * @project xauth-core
 * @author jhat
 * @email cpf624@126.com
 * @date Jan 3, 201312:54:01 PM
 */
@Service
public interface ManageLogService {

	/**
	 * 记录管理日志
	 * 
	 * @param manageLog
	 *            管理日志
	 * @return 返回记录结果，记录成功：{@link Status}.SUCCESS， 记录失败{@link Status}.ERROR
	 */
	public int logManage(ManageLog manageLog);

	/**
	 * 查询管理日志
	 * 
	 * @param oper
	 *            当前操作用户
	 * @param query
	 *            查询条件
	 * @return
	 */
	public List<ManageLog> getManageLogs(User oper, QueryBase query);

	/**
	 * 统计管理日志
	 * 
	 * @param oper
	 *            当前操作用户
	 * @param query
	 *            统计条件
	 * @return
	 */
	public long countManageLogs(User oper, QueryBase query);

}
