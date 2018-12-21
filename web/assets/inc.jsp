<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
            + path + "/";

    request.setAttribute("basePath", basePath);
%>

<meta http-equiv="X-UA-Compatible" content="IE=edge"/>
<!-- Set render engine for 360 browser -->
<meta name="renderer" content="webkit">
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<link rel="shortcut icon" type="image/ico" href="<%=basePath %>assets/images/favicon.ico">
<link href="${basePath}assets/bootstrap3/css/bootstrap.min.css" rel="stylesheet">
<link href="${basePath}assets/content/admin.css" rel="stylesheet">
<link href="${basePath}assets/content/font-awesome/4.7/css/font-awesome.css" rel="stylesheet" type="text/css">
<!--[if lte IE 8]> <link rel="stylesheet" href="${basePath}assets/content/admin-ie.css?" /> <![endif]-->

<script src="${basePath}assets/scripts/jquery.min.js"></script>
<script src="${basePath}assets/scripts/jquery.form.js"></script>
<script src="${basePath}assets/bootstrap3/js/bootstrap.min.js"></script>

<link href="${basePath}assets/content/kendo/2015.2.624/kendo.ht.common-material.min.css" rel="stylesheet"
      type="text/css"/>
<link href="${basePath}assets/content/kendo/2015.2.624/kendo.ht.material.min.css" rel="stylesheet" type="text/css"/>

<!--link href="${basePath}assets/content/kendo/2015.2.624/kendo.dataviz.min.css" rel="stylesheet" type="text/css" />
<link href="${basePath}assets/content/kendo/2015.2.624/kendo.dataviz.default.min.css" rel="stylesheet" type="text/css" /-->
<script src="${basePath}assets/scripts/kendo/2015.2.624/kendo.all.min.js"></script>
<%-- <script src="${basePath}assets/scripts/kendo/2015.2.624/kendo.aspnetmvc.min.js"> </script> --%>
<script src="${basePath}assets/scripts/kendo/2015.2.624/cultures/kendo.culture.zh-CN.min.js"></script>

<script src="${basePath}assets/scripts/kendo.extends.js"></script>
<script src="${basePath}assets/scripts/admin.js"></script>

<script type="text/javascript">
    var basePath = '${basePath}';

    $(function () {
        if (typeof iv != "undefined" && typeof iv.init !== "undefined") {
            iv.init();
            fit();
        }

        kendo.culture("zh-CN");
    })
</script>
