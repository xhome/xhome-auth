<#assign base_url = "${xconfig('base_url')}" />
<#assign xauth_base_url = base_url + "/xauth" />
<#assign user_base_url = xauth_base_url + "/user" />
<#assign user_login_url = user_base_url + "/login.htm" />
<#assign user_forget_url = user_base_url + "/forget.htm" />
<#assign user_reset_url = user_base_url + "/reset.htm" />
<#assign user_logout_url = user_base_url + "/logout.htm" />

<#-- Start of head -->
<#macro head title description = "" keywords = "" charset = "UTF-8">
<head>
    <title>${title}</title>
    <meta http-equiv="Content-type" content="text/html; charset=${charset}" />
    <meta name="Description" content="${description}" />
    <meta name="Keywords" content="${keywords}" />
    <link href="${base_url}/xlibs/css/bootstrap.css" rel="stylesheet" media="screen" />
    <link rel="shortcut icon" type="image/x-icon" href="${base_url}/favicon.ico" />
    <script type="text/javascript" src="${base_url}/xlibs/js/jquery-v2x.js"></script>
    <script type="text/javascript" src="${base_url}/xlibs/js/bootstrap.js"></script>
    <#nested>
</head>
</#macro>
<#-- End of head -->

<#-- 获取当前登录用户 -->
<#--
${Session["org.xhome.xauth.session.user"]}
-->
<#if statics?? && !statics["org.xhome.xauth.web.util.AuthUtils"].isAnonymousUser()>
    <#assign user = statics["org.xhome.xauth.web.util.AuthUtils"].getCurrentUser() />
</#if>
