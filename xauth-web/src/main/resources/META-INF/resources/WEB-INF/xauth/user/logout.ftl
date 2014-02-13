<!DOCTYPE html>
<#import "../xauth.ftl" as xauth />
<html lang="zh_CN">
<@xauth.head title="用户登出" description="XAuth" keywords="XHome, XAuth, 用户登出">
<style>
body {
    background: url('xauth/images/user/login_backgroud.png');
}
.alert {
    width: 400px;
    margin: auto;
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
<div id="wapper" class="alert alert-success">
Bye
<#if result??>
${result.data.name}
</#if>
...
</div>
<div id="footer">
    <@include file="copyright.ftl" />
</div>
<script>
// 内容上下居中
$('#wapper').css({
    'margin-top': function () {
        return ($(document).height() / 3);
    }
});
</script>
</body>
</html>
