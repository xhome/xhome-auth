<#assign base_url = "${xconfig('base_url')}" />

<#-- Start of head -->
<#macro head title description = "" keywords = "" charset = "UTF-8">
<head>
    <title>${title}</title>
    <meta http-equiv="Content-type" content="text/html; charset=${charset}">
    <meta name="Description" content="${description}"/>
    <meta name="Keywords" content="${keywords}"/>
    <base href="${base_url}"/>
    <link href="xlibs/css/bootstrap.css" rel="stylesheet" media="screen"/>
    <link rel="shortcut icon" type="image/x-icon" href="http://www.xhomestudio.org/favicon.ico"/>
    <script type="text/javascript" src="xlibs/js/jquery-v2x.js"></script>
    <script type="text/javascript" src="xlibs/js/bootstrap.js"></script>
    <#nested>
</head>
</#macro>
<#-- End of head -->
