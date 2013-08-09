package org.xhome.xauth.core.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.xhome.common.query.QueryBase;
import org.xhome.xauth.ManageLog;

/**
 * @project xauth-core
 * @author jhat
 * @email cpf624@126.com
 * @date Jan 3, 201312:54:01 PM
 */
@Service
public interface ManageLogService {
	
	public int logManage(ManageLog manageLog);
	
	public List<ManageLog> getManageLogs();
	
	public List<ManageLog> getManageLogs(QueryBase query);
	
	public long countManageLogs();
	
	public long countManageLogs(QueryBase query);
	
}
