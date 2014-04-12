package org.xhome.xauth.core.service;

/**
 * @project xauth-core
 * @author jhat
 * @email cpf624@126.com
 * @homepage http://pfchen.org
 * @date Jan 28, 2014
 * @describe 认证管理参数
 */
public interface AuthConfigService extends ConfigService {

    String ITEM_NEXT_PAGE               = "xauth_next_page";              // 认证跳转地址

    String ITEM_TRY_LIMIT               = "xauth_try_limit";              // 认证尝试次数

    String ITEM_LOCK_TIME               = "xauth_lock_time";              // 认证锁定时间

    String ITEM_ALLOW_AUTH_LOG          = "xauth_allow_auth_log";         // 认证日志

    String ITEM_ALLOW_MANAGE_LOG        = "xauth_allow_manage_log";       // 管理日志

    String ITEM_SMTP_HOST               = "xauth_smtp_host";              // 邮件发送服务器地址

    String ITEM_SMTP_PORT               = "xauth_smtp_port";              // 邮件发送服务器端口

    String ITEM_SMTP_USERNAME           = "xauth_smtp_username";          // 邮件发送服务器用户名

    String ITEM_SMTP_PASSWORD           = "xauth_smtp_password";          // 邮件发送服务器密码

    String ITEM_SMTP_FROM               = "xauth_smtp_from";              // 邮件发送源地址

    String ITEM_SMTP_SSL                = "xauth_smtp_ssl";               // 邮件发送服务器是否需要SSL加密

    String ITEM_RESET_PASSWORD_SUBJECT  = "xauth_reset_password_subject"; // 重置密码邮件主题

    String ITEM_RESET_PASSWORD_TEMPLATE = "xauth_reset_password_template"; // 重置密码邮件模板

    /**
     * 获取认证成功后的跳转地址
     * 
     * @return
     */
    public String getNextPage();

    /**
     * 获取认证尝试次数
     * 
     * @return
     */
    public int getAuthTryLimit();

    /**
     * 获取认证锁定时间
     * 
     * @return
     */
    public long getAuthLockTime();

    /**
     * 判断是否开启认证日志
     * 
     * @return
     */
    public boolean allowAuthLog();

    /**
     * 判断是否开启管理日志
     * 
     * @return
     */
    public boolean allowManageLog();

    /**
     * 获取邮件发送服务器地址
     * 
     * @return
     */
    public String getSMTPHost();

    /**
     * 获取邮件发送服务器端口
     * 
     * @return
     */
    public int getSMTPPort();

    /**
     * 获取邮件发送服务器用户名
     * 
     * @return
     */
    public String getSMTPUserName();

    /**
     * 获取邮件发送服务器密码
     * 
     * @return
     */
    public String getSMTPPassword();

    /**
     * 获取邮件发送源地址
     * 
     * @return
     */
    public String getSMTPFrom();

    /**
     * 获取邮件发送服务器是否需要SSL加密
     * 
     * @return
     */
    public boolean enableSMTPSSL();

    /**
     * 获取重置密码邮件主题
     * 
     * @return
     */
    public String getResetPasswordSubject();

    /**
     * 获取重置密码邮件模板
     * 
     * @return
     */
    public String getResetPasswordTemplate();

}
