<!DOCTYPE html>
<#import "../xauth.ftl" as xauth />
<html lang="zh_CN">
<@xauth.head title="用户登录" description="XAuth" keywords="XHome, XAuth, 用户登录">
<style>
body {
    background: url('xauth/images/user/login_backgroud.png');
}
.panel {
    width: 400px;
    margin: auto;
}
.panel-heading {
    font-size: 18px;
}
.glyphicon {
    top: 0px;
}
</style>
</@xauth.head>
<body>
<div id="form-wapper" class="panel panel-primary">
    <div class="panel-heading">用户登录</div>
    <div class="panel-body">
        <#if result??>
            <div id="error_msg" class="alert alert-danger">
                ${result.message}
            </div>
        <#else>
            <div id="error_msg" class="alert alert-danger" style="display: none;">
            </div>
        </#if>
        <form id="login_form" action="" method="POST" role="form">
            <div class="input-group input-group-lg">
                <span class="glyphicon glyphicon-user input-group-addon"></span>
                <input id="name" name="name" type="text" class="form-control" placeholder="用户名" maxlength="20">
            </div>
            <br/>
            <div class="input-group input-group-lg">
                <span class="glyphicon glyphicon-lock input-group-addon"></span>
                <input id="password" name="password" type="password" class="form-control" placeholder="密码" maxlength="20">
            </div>
            <br/>
            <input class="submit btn btn-primary btn-lg btn-block" type="submit" role="button" value="登 录"></input>
            <p>
                <div class="checkbox">
                    <label>
                        <input name="rember_password" type="checkbox" checked="on"/> 记住密码
                    </label>
                    <a class="pull-right" href="#">忘记密码</a>
                </div>
            </p>
        </form>
    </div>
</div>
<@xauth.copyright />
<script type="text/javascript" src="xlibs/js/jquery-validate.js"></script>
<script type="text/javascript" src="xlibs/js/jquery-xvalidate.js"></script>
<script>
// Form表单上下居中
$('#form-wapper').css({
    'margin-top': function () {
        return ($(this).height() / 2);
    }
});
// Form表单登录验证
$().ready(function() {
    $('#login_form').validate({
        rules: {
            name: {
                required: true,
                minlength: 4,
                maxlength: 20,
                regex: /^[\w-_]{4,20}$/
            },
            password: {
                required: true,
                minlength: 4,
                maxlength: 20,
                regex: /^[\w-_]{4,20}$/
            },
        },
        messages: {
            name: {
                required: "用户名不能为空",
                minlength: "用户名不能少于{0}个字符",
                maxlength: "用户名不能超过{0}个字符",
                regex: "用户名格式错误"
            },
            password: {
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
</script>
</body>
</html>
