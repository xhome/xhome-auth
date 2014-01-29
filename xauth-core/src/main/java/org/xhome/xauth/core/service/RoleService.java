package org.xhome.xauth.core.service;

import java.util.List;

import org.xhome.db.query.QueryBase;
import org.xhome.xauth.Role;
import org.xhome.xauth.User;

/**
 * @project xauth-core
 * @author jhat
 * @email cpf624@126.com
 * @date Jun 14, 2013 10:07:18 PM
 */
public interface RoleService {

	public int addRole(User oper, Role role);

	public int updateRole(User oper, Role role);

	public int lockRole(User oper, Role role);

	public int unlockRole(User oper, Role role);

	public int removeRole(User oper, Role role);

	public int removeRoles(User oper, List<Role> roles);

	public int deleteRole(User oper, Role role);

	public int deleteRoles(User oper, List<Role> roles);

	public boolean isRoleExists(User oper, Role role);

	public boolean isRoleUpdateable(User oper, Role role);

	public boolean isRoleLocked(User oper, Role role);

	public boolean isRoleRemoveable(User oper, Role role);

	public boolean isRoleDeleteable(User oper, Role role);

	public Role getRole(User oper, long id);

	public Role getRole(User oper, String name);

	public List<Role> getRoles(User oper, QueryBase query);

	public long countRoles(User oper, QueryBase query);

}
