// Form表单上下居中
$('#wapper').css({
    'margin-top': function () {
        return ($(this).height() / 2);
    }
});
// Form表单登录验证
$().ready(function() {
    $('#login_form').validate({
        rules: {
            'user.name': {
                required: true,
                minlength: 4,
                maxlength: 20,
                regex: /^[\w-_]{4,20}$/
            },
            'user.password': {
                required: true,
                minlength: 4,
                maxlength: 20,
                regex: /^[\w-_]{4,20}$/
            },
        },
        messages: {
            'user.name': {
                required: "用户名不能为空",
                minlength: "用户名不能少于{0}个字符",
                maxlength: "用户名不能超过{0}个字符",
                regex: "用户名格式错误"
            },
            'user.password': {
                required: "密码不能为空",
                minlength: "密码不能少于{0}个字符",
                maxlength: "密码不能超过{0}个字符",
                regex: "密码格式错误"
            },
        },
        showErrors: function(map, list) {
            if (list.length > 0) {
                $('#error_msg').html(list[0].message).show();
            } else {
                $('#error_msg').hide();
            }
        },
    });
});
