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
#footer {
    position: absolute;
    text-align: center;
    bottom: 0px;
    width: 100%;
}
</style>
</@xauth.head>
<body>
<div id="wapper" class="panel panel-primary">
    <div class="panel-heading">用户登录</div>
    <div class="panel-body">
        <#if commonResult?? && (commonResult.status != 0)>
            <div id="error_msg" class="alert alert-danger">
                ${commonResult.message}
            </div>
        <#else>
            <div id="error_msg" class="alert alert-danger" style="display: none;">
            </div>
        </#if>
        <#if commonResult?? && commonResult.data?? && commonResult.data.next_page??>
            <#assign next_page = commonResult.data.next_page /> 
        <#else>
            <#assign next_page = '' />
        </#if>
        <form id="login_form" action="${xauth.user_login_url}?next_page=${next_page}" method="POST" role="form">
            <div class="input-group input-group-lg">
                <span class="glyphicon glyphicon-user input-group-addon"></span>
                <input id="user.name" name="user.name" type="text" class="form-control" placeholder="用户名" maxlength="20"
                <#if commonResult?? && commonResult.data?? && commonResult.data.name??>value="${commonResult.data.name}"</#if>>
            </div>
            <br/>
            <div class="input-group input-group-lg">
                <span class="glyphicon glyphicon-lock input-group-addon"></span>
                <input id="user.password" name="user.password" type="password" class="form-control" placeholder="密码" maxlength="20">
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
<div id="footer">
    <@include file="copyright.ftl" />
</div>
<script type="text/javascript" src="xlibs/js/jquery-validate.js"></script>
<script type="text/javascript" src="xlibs/js/jquery-xvalidate.js"></script>
<script type="text/javascript" src="xauth/js/user/login.js"></script>
</body>
</html>
