package org.xhome.xauth.core.dao;

import java.util.List;

import org.xhome.db.query.QueryBase;
import org.xhome.xauth.Role;

/**
 * @project xauth-core
 * @author jhat
 * @email cpf624@126.com
 * @date Jun 14, 2013 10:12:50 PM
 * @description 角色DAO
 */
public interface RoleDAO {

	/**
	 * 添加角色
	 * 
	 * @param role
	 *            待添加的角色
	 * @return 返回添加结果状态码，1表示操作成功
	 */
	public int addRole(Role role);

	/**
	 * 更新角色
	 * 
	 * @param role
	 *            待更新的角色
	 * @return 返回添加结果状态码，1表示操作成功
	 */
	public int updateRole(Role role);

	/**
	 * 锁定角色
	 * 
	 * @param role
	 *            待锁定的角色
	 * @return 返回添加结果状态码，1表示操作成功
	 */
	public int lockRole(Role role);

	/**
	 * 解除角色锁定
	 * 
	 * @param role
	 *            待解除锁定的角色
	 * @return 返回添加结果状态码，1表示操作成功
	 */
	public int unlockRole(Role role);

	/**
	 * 删除（物理删除）角色
	 * 
	 * @param role
	 *            待删除的角色
	 * @return 返回添加结果状态码，1表示操作成功
	 */
	public int deleteRole(Role role);

	/**
	 * 判断角色是否存在
	 * 
	 * @param role
	 *            待判断的角色
	 * @return
	 */
	public boolean isRoleExists(Role role);

	/**
	 * 判断角色是否可以更新
	 * 
	 * @param role
	 *            待判断的角色
	 * @return
	 */
	public boolean isRoleUpdateable(Role role);

	/**
	 * 判断角色是否被锁定
	 * 
	 * @param role
	 *            待判断的角色
	 * @return
	 */
	public boolean isRoleLocked(Role role);

	/**
	 * 判断角色是否可被删除（物理删除）
	 * 
	 * @param role
	 *            待判断的角色
	 * @return
	 */
	public boolean isRoleDeleteable(Role role);

	/**
	 * 按ID查询角色
	 * 
	 * @param id
	 *            待查询的角色ID
	 * @return
	 */
	public Role queryRoleById(Long id);

	/**
	 * 按名称查询角色
	 * 
	 * @param name
	 *            待查询的角色名称
	 * @return
	 */
	public Role queryRoleByName(String name);

	/**
	 * 条件查询角色
	 * 
	 * @param query
	 *            查询条件
	 * @return
	 */
	public List<Role> queryRoles(QueryBase query);

	/**
	 * 条件统计角色
	 * 
	 * @param query
	 *            统计条件
	 * @return
	 */
	public long countRoles(QueryBase query);

}
