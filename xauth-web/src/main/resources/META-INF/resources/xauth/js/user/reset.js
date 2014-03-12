/**
 * Author:   jhat
 * Date:     2014-03-10
 * Email:    cpf624@126.com
 * Home:     http://pfchen.org
 * Describe: 密码重置页面JS 
 */

$(document).ready(function() {
    // 内容上下居中
    $('#wapper').css({
        'margin-top': function () {
            return (($(window).height() - $(this).height()) / 2);
        }
    });
    
    // Form表单验证
    try {
        validateUserResetPasswordForm();
    } catch (exp) {}
});
