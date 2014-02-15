/**
 * Author:   jhat
 * Date:     2014-02-15
 * Email:    cpf624@126.com
 * Home:     http://pfchen.org
 * Describe: 登录页面加载完成后执行的JS
 */

$(document).ready(function() {
    // Form表单上下居中
    $('#wapper').css({
        'margin-top': function () {
            return ($(this).height() / 2);
        }
    });
    
    // Form表单登录验证
    validateUserLoginForm();
});
