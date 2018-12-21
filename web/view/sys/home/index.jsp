<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<head>
    <%@include file="/assets/inc.jsp" %>
    <script type="text/javascript" src="${basePath}assets/scripts/admin.home.js"></script>
    <style type="text/css">
        .btn-info {
            width: 25px;
            height: 25px;
            border: 1px #dadada solid;
            border-radius: 50%;
            text-align: center;
            vertical-align: middle;
            line-height: 25px;
        }

        .fa-chevron-left {
            color: #F8F8FF;
            margin: 0;
            padding: 0;
            margin-left: -3px;
            margin-top: 2px;
            text-align: center;
        }

        .fa-chevron-right {
            color: #F8F8FF;
            margin: 0;
            padding: 0;
            margin-left: 1px;
            margin-top: 2px;
            text-align: center;
        }

        .login-record {
            color: #333;
            font-size: 12px;
        }

        .login-record .user-name {
            display: inline-block;
            min-width: 35px;
        }

        .tab-pane {
            overflow: hidden;
        }

        .message-left-td {
            width: 70%;
            border-bottom: 1px #dadada solid;
            text-align: center;
            overflow: hidden;
            white-space: nowrap;
            word-break: keep-all;
        }

        .message-right-td {
            width: 30%;
            border-bottom: 1px #dadada solid;
            border-left: 1px #dadada solid;
            text-align: center;
            overflow: hidden;
            white-space: nowrap;
            word-break: keep-all;
        }

        .message-tr {
            height: 2em;
        }
    </style>
</head>
<body>
<div class="container-fluid page-container">
    <div class="form-group clearfix">
        <div class="col-sm-8">
            <div class="panel panel-default" data-fit="true">
                <div class="panel-heading">
                    代办工作
                    <div class="wrap-collapse">
                        <a href="javascript:top.openapp('wf/task/toDo','m-78','代办工作',true);" class='pull-right'
                           title="更多"><i class='fa fa-plus'></i></a>
                    </div>
                </div>
                <div class="panel-body no-padding" style="height: 285px;">
                </div>
            </div>
        </div>
        <div class="col-sm-4">
            <div class="panel panel-default">
                <div id="collapse-editPanel">
                    <div class="panel-body">
                        <marquee direction="up" truespeed="truespeed" height="117px" behavior="scroll"
                                 scrolldelay="300">
                            <c:forEach items="${loginRecord}" var="record">
                                <div class="login-record">用户 <span
                                        class="text-primary user-name">${record.UserName}</span> 于
                                    <span class="text-warning"><fmt:formatDate value="${record.LoginDate}"
                                                                               pattern="yyyy-MM-dd HH:mm:ss"/></span>
                                    登录系统。
                                </div>
                            </c:forEach>
                        </marquee>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-sm-4" style="margin-top:10px;">
            <div class="panel ">
                <div class="panel-body no-padding">
                    <ul class="nav nav-tabs" role="tablist">
                        <li role="presentation" class="active"><a href="#message" aria-controls="message" role="tab"
                                                                  data-toggle="tab" id="tab-message">我的消息</a></li>
                        <li role="presentation"><a href="#reminder" aria-controls="reminder" role="tab"
                                                   data-toggle="tab" id="tab-reminder">我的提醒</a></li>
                    </ul>
                    <div class="tab-content">
                        <div role="tabpanel" class="tab-pane fade in active" id="message" style="height:143px">
                            <table id="grid-message" style="width:100%;"></table>
                        </div>
                        <div role="tabpanel" class="tab-pane fade" id="reminder" style="height:143px">
                            <table id="grid-reminder" style="width:100%;"></table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="form-group clearfix">
        <div class="col-sm-6">
        </div>
        <div class="col-sm-6">
        </div>
    </div>
</div>
</body>
</html>