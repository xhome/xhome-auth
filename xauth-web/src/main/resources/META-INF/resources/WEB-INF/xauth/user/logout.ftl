<!DOCTYPE html>
<#import "../xauth.ftl" as xauth />
<html lang="zh_CN">
<@xauth.head title="用户登出" description="XAuth" keywords="XHome, XAuth, 用户登出">
    <link href="${xauth.xauth_base_url}/css/user/user.css" rel="stylesheet" media="screen" />
</@xauth.head>
<body>
<div id="wapper" class="alert alert-success" style="width: 400px; margin: auto;">
Bye
<#if result??>
${result.data.name}
</#if>
...
</div>
<div id="footer">
    <@include file="copyright.ftl" />
</div>
<script type="text/javascript" src="${xauth.xauth_base_url}/js/user/logout.js"></script>
</body>
</html>
