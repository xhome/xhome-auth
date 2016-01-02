package org.xhome.xauth.web.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.xhome.common.constant.Status;
import org.xhome.common.util.RandomUtils;
import org.xhome.common.util.StringUtils;
import org.xhome.crypto.AES;
import org.xhome.db.query.QueryBase;
import org.xhome.spring.mvc.extend.bind.annotation.RequestAttribute;
import org.xhome.validator.Validator;
import org.xhome.web.action.AbstractAction;
import org.xhome.web.response.CommonResult;
import org.xhome.web.response.DataResult;
import org.xhome.web.util.RequestUtils;
import org.xhome.web.util.ResponseUtils;
import org.xhome.web.util.SessionUtils;
import org.xhome.xauth.AuthException;
import org.xhome.xauth.Role;
import org.xhome.xauth.User;
import org.xhome.xauth.core.service.AuthConfigService;
import org.xhome.xauth.core.service.EmailService;
import org.xhome.xauth.core.service.UserService;
import org.xhome.xauth.web.util.AuthUtils;
import org.xhome.xauth.web.validator.UserPasswordNotEmptyValidator;

/**
 * @project xauth-web
 * @author jhat
 * @email cpf624@126.com
 * @date Aug 13, 201310:50:23 PM
 * @description
 */
@Controller
public class UserAction extends AbstractAction {

    @Autowired
    private UserService        userService;
    @Autowired
    private EmailService       emailService;
    @Autowired
    private AuthConfigService  authConfigService;

    public final static String RM_USER_AUTH_CODE       = "xauth/user/authcode";
    public final static String RM_USER_LOGIN           = "xauth/user/login";
    public final static String RM_USER_LOGOUT          = "xauth/user/logout";
    public final static String RM_USER_FORGET          = "xauth/user/forget";
    public final static String RM_USER_RESET           = "xauth/user/reset";

    public final static String RM_USER_ADD             = "xauth/user/add";
    public final static String RM_USER_UPDATE          = "xauth/user/update";
    public final static String RM_USER_CHPASSWD        = "xauth/user/chpasswd";
    public final static String RM_USER_LOCK            = "xauth/user/lock";
    public final static String RM_USER_UNLOCK          = "xauth/user/unlock";
    public final static String RM_USER_DELETE          = "xauth/user/delete";

    public final static String RM_USER_EXISTS          = "xauth/user/exists";
    public final static String RM_USER_UPDATEABLE      = "xauth/user/updateable";
    public final static String RM_USER_LOCKED          = "xauth/user/locked";
    public final static String RM_USER_DELETEABLE      = "xauth/user/deleteable";
    public final static String RM_USER_GET             = "xauth/user/get";
    public final static String RM_USER_QUERY           = "xauth/user/query";
    public final static String RM_USER_COUNT           = "xauth/user/count";

    public final static String RM_USER_ROLE_ADD        = "xauth/user/role/add";
    public final static String RM_USER_ROLE_LOCK       = "xauth/user/role/lock";
    public final static String RM_USER_ROLE_UNLOCK     = "xauth/user/role/unlock";
    public final static String RM_USER_ROLE_DELETE     = "xauth/user/role/delete";

    public final static String RM_USER_ROLE_EXISTS     = "xauth/user/role/exists";
    public final static String RM_USER_ROLE_UPDATEABLE = "xauth/user/role/updateable";
    public final static String RM_USER_ROLE_LOCKED     = "xauth/user/role/locked";
    public final static String RM_USER_ROLE_DELETEABLE = "xauth/user/role/deleteable";

    public final static String RM_USER_LOGIN_QUERY     = "xauth/user/login/query";
    public final static String RM_USER_LOGIN_COUNT     = "xauth/user/login/count";

    public final static String LOGIN_NEXT_PAGE         = "next_page";

    /**
     * 用户登录页面获取请求
     * 
     * @return
     */
    @RequestMapping(value = RM_USER_LOGIN, method = RequestMethod.GET)
    public Object login_get(HttpServletRequest request) {
        // 将Referer作为next_page，用于登录成功后跳转
        String referer = request.getHeader("Referer");
        Map<String, Object> data = new HashMap<String, Object>();
        data.put(LOGIN_NEXT_PAGE, referer);
        return new CommonResult(Status.SUCCESS, "", data);
    }

    /**
     * 用户登录请求
     * 
     * @param user
     *            验证后的用户信息
     * @param remberPassword
     *            是否记住密码
     * @param next
     *            认证成功后的跳转页面（允许为空）
     * @param result
     *            验证结果
     * @param request
     * @return
     */
    @RequestMapping(value = RM_USER_LOGIN, method = RequestMethod.POST)
    public Object login(
                    @Validated @RequestAttribute("user") User user,
                    @RequestParam(required = false, value = "rember_password") String remberPassword,
                    HttpServletRequest request, HttpServletResponse response) {
        short status = 0;
        String msg = null;

        try {
            User u = userService.auth(user,
                            RequestUtils.getRequestAddress(request),
                            RequestUtils.getRequestUserAgent(request),
                            RequestUtils.getRequestUserAgentName(request));
            AuthUtils.setCurrentUser(request, u);
            status = Status.SUCCESS;
            msg = "用户" + user.getName() + "登录成功";
            user = u;

            // 记住密码
            if ("on".equalsIgnoreCase(remberPassword)
                            || "true".equalsIgnoreCase(remberPassword)) {
                AuthUtils.setCookieUser(response, u);
            }
        } catch (AuthException e) {
            status = e.getStatus();
            msg = e.getMessage();
        }

        if (logger.isInfoEnabled()) {
            logger.info("[" + status + "]" + msg);
        }

        if (status == Status.SUCCESS) {
            String accept = request.getHeader("Accept");
            // 认证成功后，如果不是JSON请求方式，需要进行页面跳转
            // 需要next_page是BASE_URL开头的，且不是RM_USER_LOGIN或RM_USER_LOGOUT
            // 否则跳转至系统配置的登录跳转地址
            if (accept == null || !accept.startsWith("application/json")) {
                String nextPage = request.getParameter(LOGIN_NEXT_PAGE), baseUrl = authConfigService
                                .getBaseURL(), next = null;
                if (StringUtils.isNotEmpty(nextPage)
                                && nextPage.startsWith(baseUrl)
                                && !(nextPage.startsWith(baseUrl
                                                + RM_USER_LOGIN)
                                                || nextPage.startsWith(baseUrl
                                                                + RM_USER_LOGOUT) || nextPage
                                                    .contains(RM_USER_RESET))) {
                    next = nextPage;
                } else {
                    next = authConfigService.getNextPage();
                }
                if (StringUtils.isNotEmpty(next)) {
                    return "redirect:" + next;
                }
            }
        }
        return new CommonResult(status, msg, user);
    }

    @RequestMapping(value = RM_USER_LOGOUT, method = RequestMethod.GET)
    public Object logout(HttpServletRequest request,
                    HttpServletResponse response) {
        AuthUtils.cleanResetPasswordHash(request);
        User user = AuthUtils.getCurrentUser(request);
        AuthUtils.removeCurrentUser(request);
        AuthUtils.removeCookieUser(response, user);
        String uname = user.getName();
        String msg = null;

        short status = Status.SUCCESS;
        if (logger.isInfoEnabled()) {
            logger.info("[{}] 用户{}退出登录", status, uname);
        }

        return new CommonResult(status, msg, user);
    }

    /**
     * 用户忘记密码页面获取请求
     * 
     * @return
     */
    @RequestMapping(value = RM_USER_FORGET, method = RequestMethod.GET)
    public Object forget_get(HttpServletRequest request) {
        return new CommonResult(Status.SUCCESS, "", null);
    }

    /**
     * 处理用户忘记密码请求，将密码重置链接发送至用户邮箱
     * 
     * @return
     */
    @RequestMapping(value = RM_USER_FORGET, method = RequestMethod.POST)
    public Object forget(@Validated @RequestAttribute("user") User user,
                    HttpServletRequest request) {
        String userName = user.getName();
        User duser = userService.getUser(user, userName);
        short status = Status.ERROR;
        String msg = null;

        if (duser != null) {
            String email = duser.getEmail();
            try {
                String key = RandomUtils.randomString(16);
                AES aes = new AES(key);
                String hash = aes.encrypt(userName);
                String uri = request.getRequestURI(), suffix = ".htm";
                int pos = uri.lastIndexOf('.');
                if (pos > 0) {
                    suffix = uri.substring(pos);
                }
                String url = authConfigService.getBaseURL() + "/"
                                + RM_USER_RESET + suffix + "?hash=" + hash;
                String subject = authConfigService.getResetPasswordSubject();
                String template = authConfigService.getResetPasswordTemplate();
                String body = template.replaceAll("\\$\\{user.name\\}",
                                userName).replaceAll("\\$\\{reset.url\\}", url);
                emailService.sendEmail(email, subject, body);
                msg = "密码重置链接已经发送值邮箱: " + email;
                status = Status.SUCCESS;

                AuthUtils.setResetPasswordUser(request, hash, duser);
            } catch (Exception e) {
                msg = "密码重置邮件发送失败";
                logger.error(msg, e);
            }
        } else {
            status = Status.NOT_EXISTS;
            msg = "用户名" + userName + "不存在";
        }

        if (logger.isInfoEnabled()) {
            logger.info("[{}] 用户{}尝试找回密码,{}", status, userName, msg);
        }

        return new CommonResult(status, msg, duser);
    }

    /**
     * 用户重置密码页面获取请求
     * 
     * @return
     */
    @RequestMapping(value = RM_USER_RESET, method = RequestMethod.GET)
    public Object reset_get(@RequestParam("hash") String hash,
                    HttpServletRequest request) {
        String msg = null;
        short status = Status.ERROR;
        String userName = "";

        User user = AuthUtils.getResetPasswordUser(request, hash);

        if (user != null) {
            userName = user.getName();
            status = Status.SUCCESS;
        } else {
            msg = "密码重置链接已过期~";
        }

        if (logger.isInfoEnabled()) {
            logger.info("[{}] 用户尝试重置密码,{}", status, userName, msg);
        }

        return new CommonResult(status, msg, null);
    }

    /**
     * 用户重置密码请求
     * 
     * @return
     */
    @RequestMapping(value = RM_USER_RESET, method = RequestMethod.POST)
    public Object reset(
                    @RequestParam(value = "password_new") String newPassword,
                    @RequestParam(value = "password_confirm") String confirmPassword,
                    HttpServletRequest request) {
        CommonResult result = null;
        short status = Status.ERROR;
        String msg = null;

        User user = AuthUtils.getResetPasswordUser(request);
        if (user != null) {
            Validator validator = validatorMapping
                            .getValidatorByName(UserPasswordNotEmptyValidator.class
                                            .getName());
            User vuser = new User();
            BindException be = new BindException(vuser, "user");

            // 校验用户新密码的合法性
            vuser.setPassword(newPassword);
            validator.validate(vuser, be);
            if (be.hasErrors()) {
                result = errorResult(be);
                status = result.getStatus();
                msg = "新" + result.getMessage();
                result.setMessage(msg);
            } else {
                // 校验确认密码是否与新密码一致
                if (!newPassword.equals(confirmPassword)) {
                    status = Status.PASSWD_NOT_MATCH;
                    msg = "新密码确认错误";
                } else {
                    user.setPassword(newPassword);
                    status = (short) userService.resetPassword(user, user);
                    if (status == Status.SUCCESS) {
                        msg = "用户[" + user.getId() + "]" + user.getName()
                                        + "重置密码成功";
                    } else {
                        msg = "用户[" + user.getId() + "]" + user.getName()
                                        + "重置密码失败";
                    }
                }
            }
        } else {
            status = Status.NOT_EXISTS;
            msg = "密码重置链接已过期~";
        }

        if (logger.isInfoEnabled()) {
            logger.info("[" + status + "]" + msg);
        }

        return new CommonResult(status, msg, user);
    }

    @RequestMapping(value = RM_USER_ADD, method = RequestMethod.POST)
    public Object addUser(@Validated @RequestAttribute("user") User user,
                    HttpServletRequest request) {
        short status = 0;
        String msg = null;

        User cuser = AuthUtils.getCurrentUser(request);
        AuthUtils.setOwner(request, user);
        AuthUtils.setModifier(request, user);
        try {
            status = (short) userService.addUser(cuser, user);
            if (status == Status.SUCCESS) {
                msg = "添加用户" + user.getName() + "成功";
            } else {
                msg = "添加用户" + user.getName() + "失败";
            }
        } catch (AuthException e) {
            status = e.getStatus();
            msg = e.getMessage();
        }

        if (logger.isInfoEnabled()) {
            logger.info("[" + status + "]" + cuser.getName() + msg);
        }

        return new CommonResult(status, msg, user);
    }

    @RequestMapping(value = RM_USER_UPDATE, method = RequestMethod.POST)
    public Object updateUser(@Validated @RequestAttribute("user") User user,
                    HttpServletRequest request) {
        short status = 0;
        String msg = null;

        User cuser = AuthUtils.getCurrentUser(request);
        AuthUtils.setModifier(request, user);
        try {
            status = (short) userService.updateUser(cuser, user);
            if (status == Status.SUCCESS) {
                msg = "更新用户[" + user.getId() + "]" + user.getName() + "成功";
            } else {
                msg = "更新用户[" + user.getId() + "]" + user.getName() + "失败";
            }
        } catch (AuthException e) {
            status = e.getStatus();
            msg = e.getMessage();
        }

        if (logger.isInfoEnabled()) {
            logger.info("[" + status + "]" + cuser.getName() + msg);
        }

        return new CommonResult(status, msg, user);
    }

    /**
     * 修改用户密码
     * 
     * @param oldPassword
     *            用户旧密码
     * @param newPassword
     *            用户新密码
     * @param confirmPassword
     *            确认新密码，需要与新密码一致
     * @param request
     * @return
     */
    @RequestMapping(value = RM_USER_CHPASSWD, method = RequestMethod.POST)
    public Object changePassword(
                    @RequestParam(value = "password_old") String oldPassword,
                    @RequestParam(value = "password_new") String newPassword,
                    @RequestParam(value = "password_confirm") String confirmPassword,
                    HttpServletRequest request) {
        CommonResult result = null;
        short status = -1;
        String msg = null;

        User user = AuthUtils.getCurrentUser(request);

        // 校验用户是否已登录
        if (AuthUtils.isAnonymousUser(user)) {
            status = Status.NOT_EXISTS;
            msg = "用户尚未登录，不能修改密码";
        } else {
            Validator validator = validatorMapping
                            .getValidatorByName(UserPasswordNotEmptyValidator.class
                                            .getName());
            User vuser = new User();
            BindException be = new BindException(vuser, "user");

            // 校验用户旧密码的合法性
            vuser.setPassword(oldPassword);
            validator.validate(vuser, be);
            if (be.hasErrors()) {
                result = errorResult(be);
                status = result.getStatus();
                msg = "旧" + result.getMessage();
                result.setMessage(msg);
            } else {
                // 校验用户新密码的合法性
                vuser.setPassword(newPassword);
                validator.validate(vuser, be);
                if (be.hasErrors()) {
                    result = errorResult(be);
                    status = result.getStatus();
                    msg = "新" + result.getMessage();
                    result.setMessage(msg);
                } else {
                    // 校验确认密码是否与新密码一致
                    if (!newPassword.equals(confirmPassword)) {
                        status = Status.PASSWD_NOT_MATCH;
                        msg = "新密码确认错误";
                    } else {
                        AuthUtils.setModifier(request, user);
                        user.setPassword(oldPassword);
                        status = (short) userService.changePassword(user, user,
                                        newPassword);
                        if (status == Status.SUCCESS) {
                            msg = "用户[" + user.getId() + "]" + user.getName()
                                            + "修改密码成功";
                        } else {
                            msg = "用户[" + user.getId() + "]" + user.getName()
                                            + "修改密码失败";
                        }
                    }
                    result = new CommonResult(status, msg);
                }
            }
        }

        if (logger.isInfoEnabled()) {
            logger.info("[" + status + "]" + user.getName() + msg);
        }

        return result;
    }

    // @RequestMapping(value = RM_USER_LOCK, method = RequestMethod.POST)
    public Object lockUser(@Validated @RequestAttribute("user") User user,
                    HttpServletRequest request) {
        short status = 0;
        String msg = null;

        User cuser = AuthUtils.getCurrentUser(request);
        AuthUtils.setModifier(request, user);
        status = (short) userService.lockUser(cuser, user);
        if (status == Status.SUCCESS) {
            msg = "锁定用户[" + user.getId() + "]" + user.getName() + "成功";
        } else {
            msg = "锁定用户[" + user.getId() + "]" + user.getName() + "失败";
        }

        if (logger.isInfoEnabled()) {
            logger.info("[" + status + "]" + cuser.getName() + msg);
        }

        return new CommonResult(status, msg, user);
    }

    // @RequestMapping(value = RM_USER_UNLOCK, method = RequestMethod.POST)
    public Object unlockUser(@Validated @RequestAttribute("user") User user,
                    HttpServletRequest request) {
        short status = 0;
        String msg = null;

        User cuser = AuthUtils.getCurrentUser(request);
        AuthUtils.setModifier(request, user);
        status = (short) userService.unlockUser(cuser, user);
        if (status == Status.SUCCESS) {
            msg = "解锁用户[" + user.getId() + "]" + user.getName() + "成功";
        } else {
            msg = "解锁用户[" + user.getId() + "]" + user.getName() + "失败";
        }

        if (logger.isInfoEnabled()) {
            logger.info("[" + status + "]" + cuser.getName() + msg);
        }

        return new CommonResult(status, msg, user);
    }

    @RequestMapping(value = RM_USER_DELETE, method = RequestMethod.POST)
    public Object deleteUser(
                    @Validated @RequestAttribute("users") List<User> users,
                    HttpServletRequest request) {
        short status = 0;
        String msg = null;

        User cuser = AuthUtils.getCurrentUser(request);
        try {
            status = (short) userService.deleteUsers(cuser, users);
        } catch (RuntimeException e) {
            status = Status.ERROR;
        }
        if (status == Status.SUCCESS) {
            msg = "用户删除成功";
        } else {
            msg = "用户删除失败";
        }

        if (logger.isInfoEnabled()) {
            logger.info("[" + status + "]" + cuser.getName() + msg);
        }

        return new CommonResult(status, msg, users);
    }

    // @RequestMapping(value = RM_USER_EXISTS, method = RequestMethod.GET)
    public Object isUserExists(@Validated @RequestAttribute("user") User user,
                    HttpServletRequest request) {
        short status = Status.SUCCESS;
        String msg = null;

        User cuser = AuthUtils.getCurrentUser(request);
        boolean exists = userService.isUserExists(cuser, user);
        if (exists) {
            msg = "查询用户" + user.getName() + "存在";
        } else {
            msg = "查询用户" + user.getName() + "不存在";
        }

        if (logger.isInfoEnabled()) {
            logger.info("[" + status + "]" + cuser.getName() + msg);
        }

        return new CommonResult(status, msg, exists);
    }

    // @RequestMapping(value = RM_USER_UPDATEABLE, method = RequestMethod.GET)
    public Object isUserUpdateable(
                    @Validated @RequestAttribute("user") User user,
                    HttpServletRequest request) {
        short status = Status.SUCCESS;
        String msg = null;

        User cuser = AuthUtils.getCurrentUser(request);
        boolean updateable = userService.isUserUpdateable(cuser, user);
        if (updateable) {
            msg = "查询到用户[" + user.getId() + "]" + user.getName() + "可以更新";
        } else {
            msg = "查询到用户[" + user.getId() + "]" + user.getName() + "不可以更新";
        }

        if (logger.isInfoEnabled()) {
            logger.info("[" + status + "]" + cuser.getName() + msg);
        }

        return new CommonResult(status, msg, updateable);
    }

    // @RequestMapping(value = RM_USER_LOCKED, method = RequestMethod.GET)
    public Object isUserLocked(@Validated @RequestAttribute("user") User user,
                    HttpServletRequest request) {
        short status = Status.SUCCESS;
        String msg = null;

        User cuser = AuthUtils.getCurrentUser(request);
        boolean locked = userService.isUserLocked(cuser, user);
        if (locked) {
            msg = "查询到用户[" + user.getId() + "]" + user.getName() + "已被锁定";
        } else {
            msg = "查询到用户[" + user.getId() + "]" + user.getName() + "未被锁定";
        }

        if (logger.isInfoEnabled()) {
            logger.info("[" + status + "]" + cuser.getName() + msg);
        }

        return new CommonResult(status, msg, locked);
    }

    // @RequestMapping(value = RM_USER_DELETEABLE, method = RequestMethod.GET)
    public Object isUserDeleteable(
                    @Validated @RequestAttribute("user") User user,
                    HttpServletRequest request) {
        short status = Status.SUCCESS;
        String msg = null;

        User cuser = AuthUtils.getCurrentUser(request);
        boolean deleteable = userService.isUserDeleteable(cuser, user);
        if (deleteable) {
            msg = "查询到用户[" + user.getId() + "]" + user.getName() + "可以删除";
        } else {
            msg = "查询到用户[" + user.getId() + "]" + user.getName() + "不可以删除";
        }

        if (logger.isInfoEnabled()) {
            logger.info("[" + status + "]" + cuser.getName() + msg);
        }

        return new CommonResult(status, msg, deleteable);
    }

    // @RequestMapping(value = RM_USER_GET, method = RequestMethod.GET)
    public Object getUser(
                    @RequestParam(value = "id", required = false) Long id,
                    @RequestParam(value = "name", required = false) String name,
                    HttpServletRequest request) {
        User cuser = AuthUtils.getCurrentUser(request);
        String uname = cuser.getName();
        User user = null;
        if (id != null) {
            logger.info("用户" + uname + "按ID[" + id + "]查询用户");
            user = userService.getUser(cuser, id);
        } else if (StringUtils.isNotEmpty(name)) {
            logger.info("用户" + uname + "按名称[" + name + "]查询用户");
            user = userService.getUser(cuser, name);
        }

        String msg = null;
        short status = Status.SUCCESS;

        if (user != null) {
            msg = "用户[" + user.getId() + "]" + user.getName() + "查询成功";
        } else {
            status = Status.ERROR;
            msg = "用户查询失败";
        }

        if (logger.isInfoEnabled()) {
            logger.info("[" + status + "]" + uname + msg);
        }

        return new CommonResult(status, msg, user);
    }

    @RequestMapping(value = RM_USER_QUERY, method = RequestMethod.GET)
    public Object getUsers(QueryBase query, HttpServletRequest request) {
        User cuser = AuthUtils.getCurrentUser(request);
        String uname = cuser.getName();

        if (query == null) {
            query = new QueryBase();
        }
        if (logger.isInfoEnabled()) {
            logger.info("用户" + uname + "按条件" + query.getParameters() + "查询用户信息");
        }
        userService.getUsers(cuser, query);

        String msg = "条件查询用户信息";
        short status = Status.SUCCESS;

        if (logger.isInfoEnabled()) {
            logger.info("[" + status + "]" + uname + msg);
        }

        return new DataResult(status, msg, query);
    }

    // @RequestMapping(value = RM_USER_COUNT, method = RequestMethod.GET)
    public Object countUsers(QueryBase query, HttpServletRequest request) {
        User cuser = AuthUtils.getCurrentUser(request);
        String uname = cuser.getName();

        if (query == null) {
            query = new QueryBase();
        }
        if (logger.isInfoEnabled()) {
            logger.info("用户" + uname + "按条件" + query.getParameters() + "统计用户信息");
        }
        long count = userService.countUsers(cuser, query);

        String msg = "条件统计用户信息，共" + count;
        short status = Status.SUCCESS;

        if (logger.isInfoEnabled()) {
            logger.info("[" + status + "]" + uname + msg);
        }

        return new CommonResult(status, msg, count);
    }

    // @RequestMapping(value = RM_USER_ROLE_ADD, method = RequestMethod.POST)
    public Object addUserRole(@Validated @RequestAttribute("user") User user,
                    HttpServletRequest request) {
        short status = 0;
        String msg = null;

        User cuser = AuthUtils.getCurrentUser(request);
        AuthUtils.setOwner(request, user);
        AuthUtils.setModifier(request, user);
        try {
            status = (short) userService.addUserRole(cuser, user,
                            user.getRoles());
            if (status == Status.SUCCESS) {
                msg = "添加用户" + user.getName() + "角色成功";
            } else {
                msg = "添加用户" + user.getName() + "角色失败";
            }
        } catch (AuthException e) {
            status = e.getStatus();
            msg = e.getMessage();
        }

        if (logger.isInfoEnabled()) {
            logger.info("[" + status + "]" + cuser.getName() + msg);
        }

        return new CommonResult(status, msg, user);
    }

    // @RequestMapping(value = RM_USER_ROLE_LOCK, method = RequestMethod.POST)
    public Object lockUserRole(@Validated @RequestAttribute("user") User user,
                    HttpServletRequest request) {
        short status = 0;
        String msg = null;

        User cuser = AuthUtils.getCurrentUser(request);
        AuthUtils.setModifier(request, user);
        status = (short) userService.lockUserRole(cuser, user, user.getRoles());
        if (status == Status.SUCCESS) {
            msg = "锁定用户[" + user.getId() + "]" + user.getName() + "角色成功";
        } else {
            msg = "锁定用户[" + user.getId() + "]" + user.getName() + "角色失败";
        }

        if (logger.isInfoEnabled()) {
            logger.info("[" + status + "]" + cuser.getName() + msg);
        }

        return new CommonResult(status, msg, user);
    }

    // @RequestMapping(value = RM_USER_ROLE_UNLOCK, method = RequestMethod.POST)
    public Object unlockUserRole(
                    @Validated @RequestAttribute("user") User user,
                    HttpServletRequest request) {
        short status = 0;
        String msg = null;

        User cuser = AuthUtils.getCurrentUser(request);
        AuthUtils.setModifier(request, user);
        status = (short) userService.unlockUserRole(cuser, user,
                        user.getRoles());
        if (status == Status.SUCCESS) {
            msg = "解锁用户[" + user.getId() + "]" + user.getName() + "角色成功";
        } else {
            msg = "解锁用户[" + user.getId() + "]" + user.getName() + "角色失败";
        }

        if (logger.isInfoEnabled()) {
            logger.info("[" + status + "]" + cuser.getName() + msg);
        }

        return new CommonResult(status, msg, user);
    }

    @RequestMapping(value = RM_USER_ROLE_DELETE, method = RequestMethod.POST)
    public Object deleteUserRole(
                    @Validated @RequestAttribute("user") User user,
                    HttpServletRequest request) {
        short status = 0;
        String msg = null;

        User cuser = AuthUtils.getCurrentUser(request);
        status = (short) userService.deleteUserRole(cuser, user,
                        user.getRoles());
        if (status == Status.SUCCESS) {
            msg = "删除用户[" + user.getId() + "]" + user.getName() + "角色成功";
        } else {
            msg = "删除用户[" + user.getId() + "]" + user.getName() + "角色失败";
        }

        if (logger.isInfoEnabled()) {
            logger.info("[" + status + "]" + cuser.getName() + msg);
        }

        return new CommonResult(status, msg, user);
    }

    // @RequestMapping(value = RM_USER_ROLE_EXISTS, method = RequestMethod.GET)
    public Object isUserRoleExists(
                    @Validated @RequestAttribute("user") User user,
                    HttpServletRequest request) {
        short status = Status.SUCCESS;
        String msg = null;

        User cuser = AuthUtils.getCurrentUser(request);
        List<Role> roles = user.getRoles();
        Role role = roles.get(0);
        boolean has = userService.hasUserRole(cuser, user, role);
        if (has) {
            msg = "查询用户[" + user.getId() + "]" + user.getName() + "角色["
                            + role.getId() + "]" + role.getName() + "存在";
        } else {
            msg = "查询用户[" + user.getId() + "]" + user.getName() + "角色["
                            + role.getId() + "]" + role.getName() + "不存在";
        }

        if (logger.isInfoEnabled()) {
            logger.info("[" + status + "]" + cuser.getName() + msg);
        }

        return new CommonResult(status, msg, has);
    }

    // @RequestMapping(value = RM_USER_ROLE_UPDATEABLE, method =
    // RequestMethod.GET)
    public Object isUserRoleUpdateable(
                    @Validated @RequestAttribute("user") User user,
                    HttpServletRequest request) {
        short status = Status.SUCCESS;
        String msg = null;

        User cuser = AuthUtils.getCurrentUser(request);
        List<Role> roles = user.getRoles();
        Role role = roles.get(0);
        boolean updateable = userService
                        .isUserRoleUpdateable(cuser, user, role);
        if (updateable) {
            msg = "查询到用户[" + user.getId() + "]" + user.getName() + "角色["
                            + role.getId() + "]" + role.getName() + "可以更新";
        } else {
            msg = "查询到用户[" + user.getId() + "]" + user.getName() + "角色["
                            + role.getId() + "]" + role.getName() + "不可以更新";
        }

        if (logger.isInfoEnabled()) {
            logger.info("[" + status + "]" + cuser.getName() + msg);
        }

        return new CommonResult(status, msg, updateable);
    }

    // @RequestMapping(value = RM_USER_ROLE_LOCKED, method = RequestMethod.GET)
    public Object isUserRoleLocked(
                    @Validated @RequestAttribute("user") User user,
                    HttpServletRequest request) {
        short status = Status.SUCCESS;
        String msg = null;

        User cuser = AuthUtils.getCurrentUser(request);
        List<Role> roles = user.getRoles();
        Role role = roles.get(0);
        boolean locked = userService.isUserRoleLocked(cuser, user, role);
        if (locked) {
            msg = "查询到用户[" + user.getId() + "]" + user.getName() + "角色["
                            + role.getId() + "]" + role.getName() + "已被锁定";
        } else {
            msg = "查询到用户[" + user.getId() + "]" + user.getName() + "角色["
                            + role.getId() + "]" + role.getName() + "未被锁定";
        }

        if (logger.isInfoEnabled()) {
            logger.info("[" + status + "]" + cuser.getName() + msg);
        }

        return new CommonResult(status, msg, locked);
    }

    // @RequestMapping(value = RM_USER_ROLE_DELETEABLE, method =
    // RequestMethod.GET)
    public Object isUserRoleDeleteable(
                    @Validated @RequestAttribute("user") User user,
                    HttpServletRequest request) {
        short status = Status.SUCCESS;
        String msg = null;

        User cuser = AuthUtils.getCurrentUser(request);
        List<Role> roles = user.getRoles();
        Role role = roles.get(0);
        boolean deleteable = userService
                        .isUserRoleDeleteable(cuser, user, role);
        if (deleteable) {
            msg = "查询到用户[" + user.getId() + "]" + user.getName() + "角色["
                            + role.getId() + "]" + role.getName() + "可以删除";
        } else {
            msg = "查询到用户[" + user.getId() + "]" + user.getName() + "角色["
                            + role.getId() + "]" + role.getName() + "不可以删除";
        }

        if (logger.isInfoEnabled()) {
            logger.info("[" + status + "]" + cuser.getName() + msg);
        }

        return new CommonResult(status, msg, deleteable);
    }

    @RequestMapping(value = RM_USER_AUTH_CODE, method = RequestMethod.GET)
    public void authCode(HttpServletRequest request,
                    HttpServletResponse response) {
        try {
            String authCode = ResponseUtils.responseAuthCode(request, response);
            SessionUtils.setSessionAttribute(request,
                            AuthUtils.AUTHCODE_SESSION_KEY, authCode);
            logger.info("生成随机验证码" + authCode);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public UserService getUserService() {
        return userService;
    }

    /**
     * @return the authConfigService
     */
    public AuthConfigService getAuthConfigService() {
        return authConfigService;
    }

    /**
     * @param authConfigService
     *            the authConfigService to set
     */
    public void setAuthConfigService(AuthConfigService authConfigService) {
        this.authConfigService = authConfigService;
    }

    /**
     * @return the emailService
     */
    public EmailService getEmailService() {
        return emailService;
    }

    /**
     * @param emailService
     *            the emailService to set
     */
    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }

}
