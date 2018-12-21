<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>

<!DOCTYPE HTML>
<html lang="en">
<head>
    <%@include file="/assets/inc.jsp" %>
    <link href="${basePath}assets/content/zTreeStyle/zTreeStyle.css" rel="stylesheet">
    <script src="${basePath}assets/scripts/jquery.ztree.all.js"></script>
    <script src="${basePath}assets/scripts/sys.user.group.js"></script>
</head>
<body>
<div class="container-fluid page-container">
    <div class="col-md-4">
        <div class="panel panel-default" data-fit="true" id="testPanel">
            <div class='panel-heading'>
                <h4 class='panel-title'>
                    用户列表 <a data-toggle='collapse' href='#collapse-testPanel'
                            aria-expanded='true' class='pull-right btn-collapse'> <i
                        class='fa fa-chevron-circle-up'></i>
                </a>
                </h4>
            </div>
            <div id="collapse-testPanel" class="panel-collapse collapse in">
                <div class="panel-body">
                    <ul id="tree-user" class="ztree"></ul>
                </div>
            </div>
        </div>
    </div>
    <div class="col-md-8">
        <div class="panel panel-default" data-fit="true" id="editPanel">
            <div class='panel-heading'>
                <h4 class='panel-title'>
                    用户分组 <a data-toggle='collapse' href='#collapse-editPanel'
                            aria-expanded='true' class='pull-right btn-collapse'> <i
                        class='fa fa-chevron-circle-up'></i>
                </a>
                </h4>
            </div>
            <div class='toolbar'>
                <button id='btn-save' class='btn btn-default' type='Button' data-permision='Save'>保存</button>
            </div>
            <div id="collapse-editPanel" class="panel-collapse collapse in">
                <div class="panel-body">
                    <ul id="tree-group" class="ztree"></ul>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
