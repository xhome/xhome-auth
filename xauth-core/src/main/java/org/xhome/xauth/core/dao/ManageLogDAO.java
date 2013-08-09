package org.xhome.xauth.core.dao;

import java.util.List;

import org.xhome.common.query.QueryBase;
import org.xhome.xauth.ManageLog;

/**
 * @project xauth-core
 * @author jhat
 * @email cpf624@126.com
 * @date Jan 3, 201312:56:08 PM
 */
public interface ManageLogDAO {
	
	public int addManageLog(ManageLog manageLog);
	
	public List<ManageLog> queryManageLogs(QueryBase query);
	
	public long countManageLogs(QueryBase query);
	
}
