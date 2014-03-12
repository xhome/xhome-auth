<!DOCTYPE html>
<#import "../xauth.ftl" as xauth />
<html lang="zh_CN">
<@xauth.head title="重置密码" description="XAuth" keywords="XHome, XAuth, 重置密码">
    <link href="${xauth.xauth_base_url}/css/user/user.css" rel="stylesheet" media="screen" />
</@xauth.head>
<body>
<#if commonResult?? && (commonResult.status == 0)>
    <#if commonResult.data??>
    <div id="wapper">
        <div class="jumbotron">
            <p>密码重置成功~</p>
            <p style="height: 15px;"><a style="float: right;" class="btn btn-primary btn-lg" role="button" href="${xauth.user_login_url}">登录</a></p>
        </div>
    </div>
    <#else>
    <div id="wapper" class="panel panel-primary">
        <div class="panel-heading">重置密码</div>
        <div class="panel-body">
            <#if commonResult?? && commonResult.message?? && (commonResult.message?length > 0)>
                <div id="user_reset_password_error_msg" class="alert alert-danger">
                    ${commonResult.message}
                </div>
            <#else>
                <div id="user_reset_password_error_msg" class="alert alert-danger" style="display: none;">
                </div>
            </#if>
            <form id="user_reset_password_form" action="${xauth.user_reset_url}" method="POST" role="form">
                <div class="input-group input-group-lg">
                    <span class="glyphicon glyphicon-lock input-group-addon"></span>
                    <input id="password_new" name="password_new" type="password" class="form-control" placeholder="新密码" maxlength="20">
                </div>
                <br/>
                <div class="input-group input-group-lg">
                    <span class="glyphicon glyphicon-lock input-group-addon"></span>
                    <input id="password_confirm" name="password_confirm" type="password" class="form-control" placeholder="确认密码" maxlength="20">
                </div>
                <br/>
                <input class="submit btn btn-primary btn-lg btn-block" type="submit" role="button" value="确认重置"></input>
            </form>
        </div>
    </div>
    <script type="text/javascript" src="${xauth.base_url}/xlibs/js/jquery-validate.js"></script>
    <script type="text/javascript" src="${xauth.base_url}/xlibs/js/jquery-xvalidate.js"></script>
    <script type="text/javascript" src="${xauth.xauth_base_url}/js/user/validate.js"></script>
    </#if>
<#else>
    <div id="wapper">
        <div class="jumbotron">
            <p>${commonResult.message}</p> 
            <p style="height: 15px;"><a style="float: right;" class="btn btn-primary btn-lg" role="button" href="${xauth.user_login_url}">重新登录</a></p>
        </div>
    </div>
</#if>
<div id="footer">
    <@include file="copyright.ftl" />
</div>
<script type="text/javascript" src="${xauth.xauth_base_url}/js/user/reset.js"></script>
</body>
</html>
