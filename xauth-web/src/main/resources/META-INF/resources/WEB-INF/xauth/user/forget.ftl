<!DOCTYPE html>
<#import "../xauth.ftl" as xauth />
<html lang="zh_CN">
<@xauth.head title="找回密码" description="XAuth" keywords="XHome, XAuth, 找回密码">
    <link href="${xauth.xauth_base_url}/css/user/user.css" rel="stylesheet" media="screen" />
</@xauth.head>
<body>
<#if commonResult?? && (commonResult.status == 0) && commonResult.data??>
    <div id="wapper">
        <div class="jumbotron">
            <#assign email = commonResult.data.email /> 
            <p>密码重置链接已发送至邮箱: ${email}~</p>
            <p style="height: 15px;"><a style="float: right;" class="btn btn-primary btn-lg" role="button" href="http://${email}">登录邮箱</a></p>
        </div>
    </div>
<#else>
    <div id="wapper" class="panel panel-primary">
        <div class="panel-heading">找回密码</div>
        <div class="panel-body">
            <#if commonResult?? && commonResult.message?? && (commonResult.message?length > 0)>
                <div id="user_forget_error_msg" class="alert alert-danger">
                    ${commonResult.message}
                </div>
            <#else>
                <div id="user_forget_error_msg" class="alert alert-danger" style="display: none;">
                </div>
            </#if>
            <form id="user_forget_form" action="${xauth.user_forget_url}" method="POST" role="form">
                <div class="input-group input-group-lg">
                    <span class="glyphicon glyphicon-user input-group-addon"></span>
                    <input id="user.name" name="user.name" type="text" class="form-control" placeholder="用户名" maxlength="20"
                    <#if commonResult?? && commonResult.data?? && commonResult.data.name??>value="${commonResult.data.name}"</#if>>
                </div>
                <br/>
                <input class="submit btn btn-primary btn-lg btn-block" type="submit" role="button" value="确认找回"></input>
            </form>
        </div>
    </div>
    <script type="text/javascript" src="${xauth.base_url}/xlibs/js/jquery-validate.js"></script>
    <script type="text/javascript" src="${xauth.base_url}/xlibs/js/jquery-xvalidate.js"></script>
    <script type="text/javascript" src="${xauth.xauth_base_url}/js/user/validate.js"></script>
</#if>
<div id="footer">
    <@include file="copyright.ftl" />
</div>
<script type="text/javascript" src="${xauth.xauth_base_url}/js/user/forget.js"></script>
</body>
</html>
