package org.xhome.xauth.core.dao;

import java.util.List;
import java.util.Map;
import org.xhome.db.query.QueryBase;
import org.xhome.xauth.User;

/**
 * @project xauth-core
 * @author jhat
 * @email cpf624@126.com
 * @date Jun 14, 2013 10:12:50 PM
 * @description 用户DAO
 */
public interface UserDAO {
	
	/**
	 * 添加用户
	 * 
	 * @param user 待添加的用户
	 * @return 返回添加结果状态码，1表示操作成功
	 */
	public int addUser(User user);
	
	/**
	 * 更新用户
	 * 
	 * @param user 待更新的用户
	 * @return 返回添加结果状态码，1表示操作成功
	 */
	public int updateUser(User user);
	
	/**
	 * 锁定用户
	 * 
	 * @param user 待锁定的用户
	 * @return 返回添加结果状态码，1表示操作成功
	 */
	public int lockUser(User user);
	
	/**
	 * 解除用户锁定
	 * 
	 * @param user 待解除锁定的用户
	 * @return 返回添加结果状态码，1表示操作成功
	 */
	public int unlockUser(User user);
	
	/**
	 * 移除（标记删除）用户
	 * 
	 * @param user 待移除的用户
	 * @return 返回添加结果状态码，1表示操作成功
	 */
	public int removeUser(User user);
	
	/**
	 * 删除（物理删除）用户
	 * 
	 * @param user 待删除的用户
	 * @return 返回添加结果状态码，1表示操作成功
	 */
	public int deleteUser(User user);
	
	/**
	 * 判断用户是否存在
	 * 
	 * @param user 待判断的用户
	 * @return
	 */
	public boolean isUserExists(User user);
	
	/**
	 * 判断用户是否可以更新
	 * 
	 * @param user 待判断的用户
	 * @return
	 */
	public boolean isUserUpdateable(User user);
	
	/**
	 * 判断用户是否被锁定
	 * 
	 * @param user 待判断的用户
	 * @return
	 */
	public boolean isUserLocked(User user);
	
	/**
	 * 判断用户是否可被移除（标记删除）
	 * 
	 * @param user 待判断的用户
	 * @return
	 */
	public boolean isUserRemoveable(User user);
	
	/**
	 * 判断用户是否可被删除（物理删除）
	 * 
	 * @param user 待判断的用户
	 * @return
	 */
	public boolean isUserDeleteable(User user);
	
	/**
	 * 按ID查询用户
	 * 
	 * @param id 待查询的用户ID
	 * @return
	 */
	public User queryUserById(Long id);
	
	/**
	 * 按名称查询用户
	 * 
	 * @param name 待查询的用户名称
	 * @return
	 */
	public User queryUserByName(String name);
	
	/**
	 * 条件查询用户
	 * 
	 * @param query 查询条件
	 * @return
	 */
	public List<User> queryUsers(QueryBase query);
	
	/**
	 * 条件统计用户
	 * 
	 * @param query 统计条件
	 * @return
	 */
	public long countUsers(QueryBase query);
	
	/**
	 * 为用户添加角色
	 * 
	 * @param userRole user: 用户， role:角色
	 * @return 返回添加结果状态码，1表示操作成功
	 */
	public int addUserRole(Map<String, Object> userRole);

	/**
	 * 锁定用户角色
	 * 
	 * @param userRole user: 用户， role:角色
	 * @return 返回添加结果状态码，1表示操作成功
	 */
	public int lockUserRole(Map<String, Object> userRole);
	
	/**
	 * 解锁用户角色
	 * 
	 * @param userRole user: 用户， role:角色
	 * @return 返回添加结果状态码，1表示操作成功
	 */
	public int unlockUserRole(Map<String, Object> userRole);
	
	/**
	 * 移除用户角色
	 * 
	 * @param userRole user: 用户， role:角色
	 * @return 返回添加结果状态码，1表示操作成功
	 */
	public int removeUserRole(Map<String, Object> userRole);
	
	/**
	 * 删除用户角色
	 * 
	 * @param userRole user: 用户， role:角色
	 * @return 返回添加结果状态码，1表示操作成功
	 */
	public int deleteUserRole(Map<String, Object> userRole);
	
	/**
	 * 判断用户是否拥有指定角色
	 * 
	 * @param userRole user: 用户， role:角色
	 * @return
	 */
	public boolean hasUserRole(Map<String, Object> userRole);
	
	/**
	 * 判断用户角色是否可更新
	 * 
	 * @param userRole user: 用户， role:角色
	 * @return
	 */
	public boolean isUserRoleUpdateable(Map<String, Object> userRole);
	
	/**
	 * 判断用户角色是否被锁定
	 * 
	 * @param userRole user: 用户， role:角色
	 * @return
	 */
	public boolean isUserRoleLocked(Map<String, Object> userRole);

	/**
	 * 判断用户角色是否可移除
	 * 
	 * @param userRole user: 用户， role:角色
	 * @return
	 */
	public boolean isUserRoleRemoveable(Map<String, Object> userRole);
	
	/**
	 * 判断用户角色是否可删除
	 * 
	 * @param userRole user: 用户， role:角色
	 * @return
	 */
	public boolean isUserRoleDeleteable(Map<String, Object> userRole);

}
