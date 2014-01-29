USE xauth;

DELETE FROM xhome_xauth_role;
INSERT INTO xhome_xauth_role VALUES(1, "ADMIN", 1, 1, NOW(), NOW(), 0, 2);
INSERT INTO xhome_xauth_role VALUES(2, "MEMBER", 1, 1, NOW(), NOW(), 0, 2);

DELETE FROM xhome_xauth_user;
/* 密码为：xhome_admin */
INSERT INTO xhome_xauth_user VALUES(1, "xhome", "xhome", "jLh6xp+PhMvcu3SNdO6ssaPfosKap6O+", "DEFAULT", "cpf624@126.com", 1, 1, NOW(), NOW(), 0, 2);

DELETE FROM xhome_xauth_user_role;
INSERT INTO xhome_xauth_user_role VALUES(1, 1, 1, 1, 1, NOW(), NOW(), 0, 2);

DELETE FROM xhome_xauth_config;
/* 系统配置项 */
INSERT INTO xhome_xauth_config VALUES(1, 0, 'base_url', '系统基地址', 'http://127.0.0.1:8081/xauth-test/', 1, 1, NOW(), NOW(), 0, 2);

/* 认证管理配置项 */
INSERT INTO xhome_xauth_config VALUES(2, 1, 'auth_next_page', '登录跳转地址', 'http://127.0.0.1:8081/xauth-test/xauth/dashboard.htm', 1, 1, NOW(), NOW(), 0, 2);
INSERT INTO xhome_xauth_config VALUES(3, 1, 'auth_try_limit', '登录尝试次数', '3', 1, 1, NOW(), NOW(), 0, 2);
/* 密码错误锁定时间， 单位为秒 */
INSERT INTO xhome_xauth_config VALUES(4, 1, 'auth_lock_time', '登录锁定时间', '180000', 1, 1, NOW(), NOW(), 0, 2);
/* 1 开启， 0 关闭 */
INSERT INTO xhome_xauth_config VALUES(5, 1, 'allow_auth_log', '认证日志', '1', 1, 1, NOW(), NOW(), 0, 2);
/* 1 开启， 0 关闭 */
INSERT INTO xhome_xauth_config VALUES(6, 1, 'allow_manage_log', '管理日志', '0', 1, 1, NOW(), NOW(), 0, 2);
