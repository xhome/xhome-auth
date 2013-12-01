USE xauth;

DELETE FROM xhome_xauth_role;
INSERT INTO xhome_xauth_role VALUES(1, "ADMIN", 1, 1, 1, NOW(), NOW(), 0, 2);
INSERT INTO xhome_xauth_role VALUES(2, "MEMBER", 2, 1, 1, NOW(), NOW(), 0, 2);

DELETE FROM xhome_xauth_user;
/* 密码为：xhome_admin */
INSERT INTO xhome_xauth_user VALUES(1, "xhome", "xhome", "jLh6xp+PhMvcu3SNdO6ssaPfosKap6O+", "DEFAULT", "cpf624@126.com", 1, 1, NOW(), NOW(), 0, 2);

DELETE FROM xhome_xauth_user_role;
INSERT INTO xhome_xauth_user_role VALUES(1, 1, 1, 1, 1, NOW(), NOW(), 0, 2);

INSERT INTO xhome_xauth_config VALUES(1, 0, 'base_url', '基地址', '', 1, 1, NOW(), NOW(), 0, 2);
