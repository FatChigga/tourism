<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <%@include file="/assets/inc.jsp" %>
    <script type="text/javascript" src="${basePath}assets/scripts/sys.log.login.js"></script>
    <style type="text/css">
        .panel-heading {
            border-bottom: 0px;
        }

        .t {
            border-top: 0px;
        }
    </style>
</head>
<body>
<div class="container-fluid page-container" id="editPanel">
    <div class="panel panel-default" data-fit="true" id="test-panel">
        <div class='panel-heading'>
            <h4 class='panel-title'>
                登录日志<a data-toggle='collapse' href='#collapse-TestPanel'
                       aria-expanded='true' class='pull-right btn-collapse'> <i
                    class='fa fa-chevron-circle-up'></i></a>
            </h4>
        </div>
    </div>
    <div id="collapse-TestPanel" class="panel-collapse collapse in">
        <div class="panel panel-default t" data-fit="true">
            <div id="grid"></div>
        </div>
    </div>
</div>
</body>
</html>
