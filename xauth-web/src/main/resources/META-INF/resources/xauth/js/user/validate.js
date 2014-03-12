/**
 * Author:   jhat
 * Date:     2014-02-15
 * Email:    cpf624@126.com
 * Home:     http://pfchen.org
 * Describe: 用户相关信息校验 
 */

/**
 * 校验用户登录表单
 * 
 * @param submitHandler 表单提交处理函数
 * @param showErrors 校验错误提示
 * @param form 登录表单ID
 */
function validateUserLoginForm(submitHandler, showErrors, form) {
    if (!form) {
        form = '#user_login_form'; 
    }
    
    if (!showErrors) {
        showErrors = function(map, list) {
            if (list.length > 0) {
                $('#user_login_error_msg').html(list[0].message).show();
            } else {
                $('#user_login_error_msg').hide();
            }
        }; 
    } 
   
    return $(form).validate({
        rules: {
            'user.name': {
                required: true,
                minlength: 4,
                maxlength: 20,
                regex: /^[\w-_]{4,20}$/,
            },
            'user.password': {
                required: true,
                minlength: 4,
                maxlength: 20,
                regex: /^[\w-_]{4,20}$/,
            },
        },
        messages: {
            'user.name': {
                required: "用户名不能为空",
                minlength: "用户名不能少于{0}个字符",
                maxlength: "用户名不能超过{0}个字符",
                regex: "用户名格式错误",
            },
            'user.password': {
                required: "密码不能为空",
                minlength: "密码不能少于{0}个字符",
                maxlength: "密码不能超过{0}个字符",
                regex: "密码格式错误",
            },
        },
        showErrors: showErrors,
        submitHandler: submitHandler,
    });
}

/**
 * 校验修改用户密码表单
 * 
 * @param submitHandler 表单提交处理函数
 * @param showErrors 校验错误提示
 * @param form 修改密码表单ID
 */
function validateUserChangePasswordForm(submitHandler, showErrors, form) {
    if (!form) {
        form = '#user_change_password_form'; 
    }
    
    if (!showErrors) {
        showErrors = function(map, list) {
            if (list.length > 0) {
                $('#user_change_password_error_msg').html(list[0].message).show();
            } else {
                $('#user_change_password_error_msg').hide();
            }
        }; 
    } 
   
    return $(form).validate({
        rules: {
            'password_old': {
                required: true,
                minlength: 4,
                maxlength: 20,
                regex: /^[\w-_]{4,20}$/,
            },
            'password_new': {
                required: true,
                minlength: 4,
                maxlength: 20,
                regex: /^[\w-_]{4,20}$/,
            },
            'password_confirm': {
                equalTo: '#password_new',
            },
        },
        messages: {
            'password_old': {
                required: '旧密码不能为空',
                minlength: '旧密码不能少于{0}个字符',
                maxlength: '旧密码不能超过{0}个字符',
                regex: '旧密码格式错误',
            }, 
            'password_new': {
                required: '新密码不能为空',
                minlength: '新密码不能少于{0}个字符',
                maxlength: '新密码不能超过{0}个字符',
                regex: '新密码格式错误',
            }, 
            'password_confirm': {
                equalTo: '确认密码不正确',
            }, 
        },
        showErrors: showErrors,
        submitHandler: submitHandler,
    });
}

/**
 * 校验用户找回密码表单
 * 
 * @param submitHandler 表单提交处理函数
 * @param showErrors 校验错误提示
 * @param form 登录表单ID
 */
function validateUserForgetForm(submitHandler, showErrors, form) {
    if (!form) {
        form = '#user_forget_form'; 
    }
    
    if (!showErrors) {
        showErrors = function(map, list) {
            if (list.length > 0) {
                $('#user_forget_error_msg').html(list[0].message).show();
            } else {
                $('#user_forget_error_msg').hide();
            }
        }; 
    } 
   
    return $(form).validate({
        rules: {
            'user.name': {
                required: true,
                minlength: 4,
                maxlength: 20,
                regex: /^[\w-_]{4,20}$/,
            },
        },
        messages: {
            'user.name': {
                required: "用户名不能为空",
                minlength: "用户名不能少于{0}个字符",
                maxlength: "用户名不能超过{0}个字符",
                regex: "用户名格式错误",
            },
        },
        showErrors: showErrors,
        submitHandler: submitHandler,
    });
}

/**
 * 校验用户重置密码表单
 * 
 * @param submitHandler 表单提交处理函数
 * @param showErrors 校验错误提示
 * @param form 修改密码表单ID
 */
function validateUserResetPasswordForm(submitHandler, showErrors, form) {
    if (!form) {
        form = '#user_reset_password_form'; 
    }
    
    if (!showErrors) {
        showErrors = function(map, list) {
            if (list.length > 0) {
                $('#user_reset_password_error_msg').html(list[0].message).show();
            } else {
                $('#user_reset_password_error_msg').hide();
            }
        }; 
    } 
   
    return $(form).validate({
        rules: {
            'password_new': {
                required: true,
                minlength: 4,
                maxlength: 20,
                regex: /^[\w-_]{4,20}$/,
            },
            'password_confirm': {
                equalTo: '#password_new',
            },
        },
        messages: {
            'password_new': {
                required: '新密码不能为空',
                minlength: '新密码不能少于{0}个字符',
                maxlength: '新密码不能超过{0}个字符',
                regex: '新密码格式错误',
            }, 
            'password_confirm': {
                equalTo: '确认密码不正确',
            }, 
        },
        showErrors: showErrors,
        submitHandler: submitHandler,
    });
}
