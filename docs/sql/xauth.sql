/**
* 所有TABLE均包含如下字段(ID)作为主键：
	id           INTEGER NOT NULL AUTO_INCREMENT COMMENT 'ID',
    owner        BIGINT NOT NULL COMMENT '创建者',
	modifier     BIGINT NOT NULL COMMENT '修改者',
	created      TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
	modified     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
	version      TINYINT NOT NULL DEFAULT 0 COMMENT '数据版本',
	status       TINYINT NOT NULL DEFAULT 1 COMMENT '状态标记'
*/

/*创建数据库*/
DROP database IF EXISTS xauth;
CREATE database xauth;
USE xauth;

/*创建用户*/
GRANT SELECT,INSERT,UPDATE,DELETE,EXECUTE ON xauth.* TO 'xauth'@'localhost' IDENTIFIED BY 'xauth';

DROP TABLE IF EXISTS xhome_xauth_role;
CREATE TABLE xhome_xauth_role
(
	id           INTEGER NOT NULL AUTO_INCREMENT COMMENT '角色ID',
    name         VARCHAR(20) NOT NULL COMMENT '角色名称',
	tip          INTEGER NOT NULL DEFAULT 0 COMMENT '角色数字标识',
    owner        BIGINT NOT NULL COMMENT '创建者',
	modifier     BIGINT NOT NULL COMMENT '修改者',
	created      TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
	modified     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
	version      TINYINT NOT NULL DEFAULT 0 COMMENT '数据版本',
    status       TINYINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '状态标记',
	PRIMARY KEY PK_ROLE (id),
    -- UNIQUE UN_ROLE (name),
    INDEX IN_ROLE (name)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = UTF8
COLLATE = utf8_general_ci
AUTO_INCREMENT = 1;
ALTER TABLE xhome_xauth_role COMMENT '角色';

DROP TABLE IF EXISTS xhome_xauth_user;
CREATE TABLE xhome_xauth_user
(
    id           INTEGER NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    name         VARCHAR(20) NOT NULL COMMENT '用户名',
    nick         VARCHAR(20) NOT NULL COMMENT '用户昵称',
    password     CHAR(32) NOT NULL COMMENT '密码',
    method       VARCHAR(10) NOT NULL COMMENT '认证方式',
    email        VARCHAR(50) NOT NULL COMMENT '用户邮箱',
    owner        BIGINT NOT NULL COMMENT '创建者',
    modifier     BIGINT NOT NULL COMMENT '修改者',
    created      TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
    modified     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
	version      TINYINT NOT NULL DEFAULT 0 COMMENT '数据版本',
    status       TINYINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '状态标记',
    PRIMARY KEY PK_USER (id),
    -- UNIQUE UN_USER (name),
    INDEX IN_USER (name)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = UTF8
COLLATE = utf8_bin
AUTO_INCREMENT = 1;
ALTER TABLE xhome_xauth_user COMMENT '系统用户';

DROP TABLE IF EXISTS xhome_xauth_temp_user;
CREATE TABLE xhome_xauth_temp_user(
    user BIGINT NOT NULL
)
ENGINE = Memory
DEFAULT CHARACTER SET = UTF8
COLLATE = utf8_general_ci;
ALTER TABLE xhome_xauth_temp_user COMMENT '临时用户ID';
    
DROP TABLE IF EXISTS xhome_xauth_user_role;
CREATE TABLE xhome_xauth_user_role
(
    id           BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户角色ID',
    user         BIGINT NOT NULL COMMENT '用户ID',
    role         INTEGER NOT NULL COMMENT '角色ID',
    owner        BIGINT NOT NULL COMMENT '创建者',
    modifier     BIGINT NOT NULL COMMENT '修改者',
    created      TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
    modified     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
	version      TINYINT NOT NULL DEFAULT 0 COMMENT '数据版本',
    status       TINYINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '状态标记',
    -- UNIQUE UN_USER_ROLE (user,role),
    PRIMARY KEY PK_USER_ROLE (id),
    INDEX IN_USER_ROLE (user,role)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = UTF8
COLLATE = utf8_general_ci
AUTO_INCREMENT = 1;
ALTER TABLE xhome_xauth_user_role COMMENT '用户角色';

DROP TABLE IF EXISTS xhome_xauth_manage_log;
CREATE TABLE xhome_xauth_manage_log
(
   id                   BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
   content              VARCHAR(50) COMMENT '内容描述',
   action               TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '0:Add, 1:Update, 2: Remove, 3: Search...',
   type                 TINYINT UNSIGNED NOT NULL DEFAULT 4 COMMENT '1: 角色, 2: 用户, 3: 用户角色, 4: 用户认证日志, 5: 管理日志',
   obj                  BIGINT COMMENT '对象ID,NULL表示未知',
   owner                BIGINT COMMENT '创建者,NULL表示匿名用户',
   created              TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
   status               TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '操作结果,0:成功,其它：错误状态码',
   PRIMARY KEY (id)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci
AUTO_INCREMENT = 1;
ALTER TABLE xhome_xauth_manage_log COMMENT '管理日志';


DROP TABLE IF EXISTS xhome_xauth_auth_log;
CREATE TABLE xhome_xauth_auth_log(
    id           BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    user         VARCHAR(20) NOT NULL COMMENT '用户名',
    method       VARCHAR(10) NOT NULL DEFAULT 0 COMMENT '认证方式', 
    address      VARBINARY(16) NOT NULL COMMENT '访问地址(IPv4/IPv6)',
    agent        TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '0:Other,1:Chrome, 2:Firefox, 3:IE, 4:Android',
    number       VARCHAR(100) NOT NULL COMMENT '设备编号',
    created      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '认证时间',
	status       TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '认证结果,0:认证成功,其它：错误状态码',
    PRIMARY KEY PK_LOG_USER_LOGIN (id)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = UTF8
COLLATE = utf8_general_ci
AUTO_INCREMENT = 1;
ALTER TABLE xhome_xauth_auth_log COMMENT '用户认证日志';
