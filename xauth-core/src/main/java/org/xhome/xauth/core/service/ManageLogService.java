package org.xhome.xauth.core.service;

import java.util.List;

import org.springframework.stereotype.Service;
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
	
	public int logManage(ManageLog manageLog);
	
	public List<ManageLog> getManageLogs(User oper);
	
	public List<ManageLog> getManageLogs(User oper, QueryBase query);
	
	public long countManageLogs(User oper);
	
	public long countManageLogs(User oper, QueryBase query);
	
}
