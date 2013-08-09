package org.xhome.xauth.core.service;

import java.util.List;

import org.xhome.common.query.QueryBase;
import org.xhome.xauth.Role;

/**
 * @project xauth-core
 * @author jhat
 * @email cpf624@126.com
 * @date Jun 14, 2013 10:07:18 PM
 */
public interface RoleService {

	public int addRole(Role role);
	
	public int updateRole(Role role);
	
	public int lockRole(Role role);
	
	public int unlockRole(Role role);

	public int removeRole(Role role);
	
	public int deleteRole(Role role);
	
	public boolean isRoleExists(Role role);
	
	public boolean isRoleUpdateable(Role role);

	public boolean isRoleLocked(Role role);
	
	public boolean isRoleRemoveable(Role role);
	
	public boolean isRoleDeleteable(Role role);
	
	public Role getRole(long id);
	
	public Role getRole(String name);
	
	public List<Role> getRoles();
	
	public List<Role> getRoles(QueryBase query);
	
	public long countRoles();
	
	public long countRoles(QueryBase query);
	
}
