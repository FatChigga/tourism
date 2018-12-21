<%@page import="com.iptv.core.common.Configuration"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org" xmlns:shiro="http://www.pollix.at/thymeleaf/shiro">
<html lang="zh_CN" style="overflow: hidden;">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <!-- Set render engine for 360 browser -->
    <meta name="renderer" content="webkit">
    <meta charset="utf-8">
    <title>酒店屏 - 管理中心</title>
    <meta name="description" content="This is page-header (.page-header &gt; h1)">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link rel="shortcut icon" type="image/ico" href="<%=basePath %>assets/images/favicon.ico">
    <link href="<%=basePath %>assets/bootstrap3/css/bootstrap.min.css" rel="stylesheet">
    <link href="<%=basePath %>assets/content/admin.css" rel="stylesheet">
    <link href="<%=basePath %>assets/content/font-awesome/4.7/css/font-awesome.min.css" rel="stylesheet"
          type="text/css">
    <!--[if lte IE 8]>
    <link rel="stylesheet" href="<%=basePath %>assets/content/admin-ie.css?"/> <![endif]-->
    <script src="<%=basePath %>assets/scripts/jquery.min.js"></script>
    <script src="<%=basePath %>assets/bootstrap3/js/bootstrap.min.js"></script>
    <script src="<%=basePath %>assets/scripts/admin.js"></script>
    <script type="text/javascript">
        var basePath = '<%=basePath%>';

        $(document).ready(function () {
            $('#btn-loginout').click(function () {
                $.mdlg.confirm('提示', '您确认要退出么?', function () {
                    window.location.href = '<%=basePath%>admin/loginout';
                })
            })

            $("#btn-modify").click(function () {
                $.mdlg({
                    title: '修改密码',
                    content: function () {
                        return $('#data-tmpl').html();
                    },
                    showCloseButton: false,
                    buttons: ["保存", "取消"],
                    buttonStyles: ['btn-success', 'btn-default'],
                    onButtonClick: function (sender, modal, index) {
                        var self = this;
                        if (index == 0) {
                            var params = $("#form-password-modify").serializeJson();
                            params['UserName'] = "${userName}";
                            params['Id'] = "${userId}";

                            $.post('<%=basePath%>sys/user/passwordModify', JSON.stringify(params), function (data) {
                                if (data.result == true) {
                                    $.mdlg.alert('提示', data.message);
                                    $(this).closeDialog(modal);
                                } else {
                                    $("#error-info > div").html(data.message);
                                }

                                $("#error-info").removeClass('hide');
                            })
                        } else {
                            $(this).closeDialog(modal);
                        }
                    }
                })
            });

            //用户信息
            $("#btn-user-info").click(function () {
                $.mdlg({
                    title: '用户信息',
                    content: function () {
                        return $('#data-info').html();
                    },
                    onShow: function () {
                        var data = {"id": "${userId}"};
                        $.get('<%=basePath%>sys/user/getUser', data, function (data) {
                            if (data != null && data != '') {
                                $('#txt-code').html(data.Code);
                                $('#txt-userName').html(data.UserName);
                                $('#txt-roleList').html(data.RoleName);
                                $('#txt-portrait').html('<img alt="" src="">');
                            }
                        });
                    }
                });
            });
        })
    </script>
</head>
<style>
    .my-title {
        letter-spacing: 1px;
        color: #00ccff;
        text-align: center;
        display: inline-block;
        margin-top: 1px;
    }

    .my-title small {
        letter-spacing: 0px;
        font-size: 12px;
        padding-left: 5px;
        color: #fff;
    }
</style>
<body screen_capture_injected="true">
<div id="loading"><i class="loadingicon"></i><span>努力加载中...</span></div>
<div class="navbar">
    <div class="navbar-inner">
        <div class="container-fluid">
            <a href="#" class="navbar-brand">
                <small><img src="<%=basePath %>assets/images/logo.png" width="22" height="22"></small>
                <span class="my-title">酒店屏<small>管理中心</small></span>
            </a>
            <div class="pull-left nav_shortcuts">
                <!--a class="btn btn-xs btn-danger" href="javascript:openapp('admin/sync/index','m-560','同步到行业平台',true);" title="同步到行业平台"> <i class="fa fa-globe" style="font-size:14px;"></i> </a-->
                <span id="shortcut-list"></span>
            </div>
            <ul class="nav simplewind-nav pull-right">
                <li>
                    <a data-toggle="dropdown" href="#" class="dropdown-toggle">
                        <img class="nav-user-photo" width="30" height="30"
                             src="<%=basePath %>assets/images/admin/user.png" alt="admin">
                        <span class="user-info">欢迎, ${userName}&nbsp;</span>
                        <i class="fa fa-caret-down"></i>
                    </a>
                    <ul class="user-menu pull-right dropdown-menu dropdown-yellow dropdown-caret dropdown-closer">
                        <li><a href="javascript:void(0);" id="btn-user-info"><i class="fa fa-user-circle-o"></i>
                            用户信息</a></li>
                        <li><a href="javascript:void(0);" id="btn-modify"><i class="fa fa-key"></i> 修改密码</a></li>
                        <li><a href="javascript:void(0);" id="btn-loginout"><i class="fa fa-sign-out"></i> 退出</a></li>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
</div>
<div class="main-container container-fluid">
    <div class="sidebar" id="sidebar">
        <!-- <div class="sidebar-shortcuts" id="sidebar-shortcuts"> </div> -->
        <div id="nav_wraper">
            <ul class="nav nav-list" id="nav-menu-list">
                ${session.menus}
            </ul>
        </div>
    </div>
    <div class="main-content">
        <div class="breadcrumbs" id="breadcrumbs">
            <a id="task-pre" class="task-changebt"><i class="fa fa-caret-left"></i></a>
            <div id="task-content">
                <ul class="macro-component-tab clearfix" id="task-content-inner">
                    <li class="macro-component-tabitem noclose" app-id="0" app-url="index.php" app-name="首页">
                        <span class="macro-tabs-item-text">首页</span>
                    </li>
                </ul>
            </div>
            <a id="task-next" class="task-changebt"><i class="fa fa-caret-right"></i></a>

            <div class="dropdown pull-right task-btn" id="btn-tabs-close">
                <a class="dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown">
                    关闭<span class="caret"></span>
                </a>
                <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1">
                    <li role="presentation"><a id="btn-close-current" role="menuitem" tabindex="-1"
                                               href="javascript:void(0);">关闭当前</a></li>
                    <li role="presentation" class="divider"></li>
                    <li role="presentation"><a id="btn-close-all" role="menuitem" tabindex="-1"
                                               href="javascript:void(0);">关闭全部</a></li>
                </ul>
            </div>

            <div id="refresh_wrapper" class="task-btn pull-right" title="刷新"><i
                    class="fa fa-refresh right_tool_icon"></i></div>

            <div id="task-extend" class="dropdown pull-right task-btn">
                <a class="dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" title="更多">
                    <span class="glyphicon glyphicon-list-alt"></span>
                </a>
                <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1">
                    <li role="presentation"><a id="btn-close-current" role="menuitem" tabindex="-1"
                                               href="javascript:void(0);">关闭当前</a></li>
                    <li role="presentation"><a id="btn-close-all" role="menuitem" tabindex="-1"
                                               href="javascript:void(0);">关闭全部</a></li>
                </ul>
            </div>
        </div>

        <div class="page-content" id="content">
            <iframe src="<%=basePath %>admin/home" style="width:100%;height: 100%;" frameborder="0" id="appiframe-0"
                    class="appiframe"></iframe>
        </div>
    </div>
</div>
<script type="text/x-jquery-tmpl" id="data-tmpl">
		<form class="form-horizontal" id="form-password-modify">
			<div class="form-group hide" id="error-info">
				<div class="col-sm-12 bg-danger text-danger text-error"></div>
			</div>
			<div class="form-group">
				<label class="col-sm-3 control-label">用户名称</label>
				<div class="col-sm-8 control-label text-danger" style="text-align:left;"> ${userName} </div>
			</div>
			<div class="form-group">
				<label class="col-sm-3 control-label">旧密码</label>
				<div class="col-sm-8">
					<input type="password" class="form-control" placeholder="请输入旧密码" maxlength="20" name="BeforePassword" >
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-3 control-label">新密码</label>
				<div class="col-sm-8">
					<input type="password" class="form-control" placeholder="请输入新密码" maxlength="20" name="Password"  >
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-3 control-label">确认密码</label>
				<div class="col-sm-8">
					<input type="password" class="form-control" placeholder="请再次输入密码" maxlength="20" name="ConfirmPassword">
				</div>
			</div>
		</form>

</script>
<script type="text/x-jquery-tmpl" id="data-info">
		<form class="form-horizontal" id="form-user-info">
			<div class="form-group">
				<label class="col-sm-3 control-label">工号</label>
				<div class="col-sm-8 control-label " style="text-align:left;"id="txt-code"></div>
			</div>
			<div class="form-group">
				<label class="col-sm-3 control-label">姓名</label>
				<div class="col-sm-8 control-label " style="text-align:left;" id="txt-userName"></div>
			</div>
			<div class="form-group">
				<label class="col-sm-3 control-label">职位</label>
				<div class="col-sm-8 control-label " style="text-align:left;" id="txt-roleList"></div>
			</div>
			<div class="form-group">
				<label class="col-sm-3 control-label">头像</label>
				<div class="col-sm-8 control-label " style="text-align:left;" id="txt-portrait"></div>
			</div>
		</form>

</script>
</body>
</html>
