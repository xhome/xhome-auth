USE xauth;

DELETE FROM xhome_xauth_role;
INSERT INTO xhome_xauth_role VALUES(1, "ADMIN", 1, 1, NOW(), NOW(), 0, 2);
INSERT INTO xhome_xauth_role VALUES(2, "MEMBER", 1, 1, NOW(), NOW(), 0, 2);

DELETE FROM xhome_xauth_user;
/* 密码为：xhome_admin */
INSERT INTO xhome_xauth_user VALUES(1, "xhome", "xhome", "jLh6xp+PhMvcu3SNdO6ssaPfosKap6O+", "DEFAULT", "xhome@xhomestudio.org", 1, 1, NOW(), NOW(), 0, 2);

DELETE FROM xhome_xauth_user_role;
INSERT INTO xhome_xauth_user_role VALUES(1, 1, 1, 1, 1, NOW(), NOW(), 0, 2);

DELETE FROM xhome_xauth_config;

INSERT INTO xhome_xauth_config (category, item, display, value, owner, modifier, created, modified, version, status) VALUES
    /* 系统配置项 */
    (0, 'base_url', '系统基地址', 'http://127.0.0.1/xauth-test', 1, 1, NOW(), NOW(), 0, 2),
    
    /* 认证管理配置项 */
    (1, 'xauth_next_page', '登录跳转地址', 'http://127.0.0.1/xauth-test/dashboard.htm', 1, 1, NOW(), NOW(), 0, 2),
    (1, 'xauth_try_limit', '登录尝试次数', '3', 1, 1, NOW(), NOW(), 0, 2),
    /* 密码错误锁定时间， 单位为秒 */
    (1, 'xauth_lock_time', '登录锁定时间', '180000', 1, 1, NOW(), NOW(), 0, 2),
    /* 1 开启， 0 关闭 */
    (1, 'xauth_allow_auth_log', '认证日志', '1', 1, 1, NOW(), NOW(), 0, 2),
    /* 1 开启， 0 关闭 */
    (1, 'xauth_allow_manage_log', '管理日志', '0', 1, 1, NOW(), NOW(), 0, 2);
