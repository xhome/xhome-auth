USE xauth;

DELETE FROM xhome_xauth_role;
INSERT INTO xhome_xauth_role VALUES(1, "ADMIN", 1, 1, NOW(), NOW(), 0, 15);
INSERT INTO xhome_xauth_role VALUES(2, "MEMBER", 1, 1, NOW(), NOW(), 0, 15);

DELETE FROM xhome_xauth_user;
/* 密码为：xhome_admin */
INSERT INTO xhome_xauth_user VALUES(1, "xhome", "xhome", "jLh6xp+PhMvcu3SNdO6ssaPfosKap6O+", "DEFAULT", "xhome@xhomestudio.org", 1, 1, NOW(), NOW(), 0, 15);

DELETE FROM xhome_xauth_user_role;
INSERT INTO xhome_xauth_user_role VALUES(1, 1, 1, 1, 1, NOW(), NOW(), 0, 15);

DELETE FROM xhome_xauth_config;

INSERT INTO xhome_xauth_config (category, item, display, value, owner, modifier, created, modified, version, status) VALUES
    /* 系统配置项 */
    (0, 'base_url', '系统基地址', 'http://127.0.0.1/xauth-test', 1, 1, NOW(), NOW(), 0, 15),
    
    /* 认证管理配置项 */
    (1, 'xauth_next_page', '登录跳转地址', 'http://127.0.0.1/xauth-test/dashboard.htm', 1, 1, NOW(), NOW(), 0, 15),
    (1, 'xauth_try_limit', '登录尝试次数', '3', 1, 1, NOW(), NOW(), 0, 15),
    /* 密码错误锁定时间， 单位为秒 */
    (1, 'xauth_lock_time', '登录锁定时间', '180000', 1, 1, NOW(), NOW(), 0, 15),
    /* 1 开启， 0 关闭 */
    (1, 'xauth_allow_auth_log', '认证日志', '1', 1, 1, NOW(), NOW(), 0, 15),
    /* 1 开启， 0 关闭 */
    (1, 'xauth_allow_manage_log', '管理日志', '0', 1, 1, NOW(), NOW(), 0, 15),
    (1, 'xauth_smtp_host', 'SMTP服务器地址', '127.0.0.1', 1, 1, NOW(), NOW(), 0, 15),
    (1, 'xauth_smtp_port', 'SMTP服务器端口', '25', 1, 1, NOW(), NOW(), 0, 15),
    (1, 'xauth_smtp_username', 'SMTP服务器账号', '', 1, 1, NOW(), NOW(), 0, 15),
    (1, 'xauth_smtp_password', 'SMTP服务器密码', '', 1, 1, NOW(), NOW(), 0, 15),
    (1, 'xauth_smtp_from', '邮件发送源地址', 'no-reply@xhomestudio.org', 1, 1, NOW(), NOW(), 0, 15),
    (1, 'xauth_smtp_ssl', 'SMTP服务器SSL加密', '0', 1, 1, NOW(), NOW(), 0, 15),
    (1, 'xauth_reset_password_subject', '重置密码邮件主题', '重置密码', 1, 1, NOW(), NOW(), 0, 15),
    (1, 'xauth_reset_password_template', '重置密码邮件模板', '${user.name}, 您好！<br/><br/>&nbsp;&nbsp;&nbsp;&nbsp;请在30分钟内访问 <a href="${reset.url}">${reset.url}</a> 以重置密码!<br/><br/><a href="http://www.xhomestudio.org">启梦工作室<a>', 1, 1, NOW(), NOW(), 0, 15);
