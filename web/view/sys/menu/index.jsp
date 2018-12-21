<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>

<!DOCTYPE HTML>
<html lang="en">
<head>
    <%@include file="/assets/inc.jsp" %>
    <link href="${basePath}assets/content/zTreeStyle/zTreeStyle.css" rel="stylesheet">
    <script src="${basePath}assets/scripts/jquery.ztree.all.js"></script>
    <script src="${basePath}assets/scripts/sys.menu.js"></script>
</head>
<body>
<div class="container-fluid page-container">
    <div class="col-md-4">
        <div class="panel panel-default" data-fit="true" id="TestPanel">
            <div class='panel-heading'>
                <h4 class='panel-title'>
                    系统菜单 <a data-toggle='collapse' href='#collapse-TestPanel'
                            aria-expanded='true' class='pull-right btn-collapse'> <i
                        class='fa fa-chevron-circle-up'></i>
                </a>
                </h4>
            </div>
            <div id="collapse-TestPanel" class="panel-collapse collapse in">
                <div class="panel-body">
                    <ul id="tree-menu" class="ztree"></ul>
                </div>
            </div>
        </div>
    </div>
    <div class="col-md-8">
        <div class="panel panel-default" data-fit="true" id="editPanel">
            <div class='panel-heading'>
                <h4 class='panel-title'>
                    &nbsp; <a data-toggle='collapse' href='#collapse-editPanel'
                              aria-expanded='true' class='pull-right btn-collapse'> <i
                        class='fa fa-chevron-circle-up'></i>
                </a>
                </h4>
            </div>
            <div class='toolbar'>
                <button id='btn-add' class='btn btn-default' type='Button' data-permision='Add'>新增</button>
                <button id='btn-save' class='btn btn-default' type='Button' data-permision='Save'>保存</button>
                <button id='btn-del' class='btn btn-default' type='Button' data-permision='Delete'>删除</button>
            </div>
            <div id="collapse-editPanel" class="panel-collapse collapse in">
                <div class="panel-body">
                    <form class="form-horizontal" id="form-menu">
                        <input type="hidden" name="Id" id="menuId">
                        <input type="hidden" name="ParentId" id="parentId">
                        <div class="form-group">
                            <label for="code" class="col-sm-2 control-label">菜单编码</label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control" id="code" name="Code"
                                       placeholder="菜单编码">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="name" class="col-sm-2 control-label">菜单名称</label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control" id="name" name="Name"
                                       placeholder="菜单名称">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="level" class="col-sm-2 control-label">菜单层级</label>
                            <div class="col-sm-10">
                                <input type="number" class="form-control" id="level" name="Level"
                                       placeholder="菜单层级">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="pageUrl" class="col-sm-2 control-label">菜单地址</label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control" id="pageUrl"
                                       name="PageUrl" placeholder="菜单地址">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="menuIcon" class="col-sm-2 control-label">菜单图标</label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control" id="menuIcon"
                                       name="MenuIcon" placeholder="菜单图标">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="orderNum" class="col-sm-2 control-label">序号</label>
                            <div class="col-sm-10">
                                <input type="number" class="form-control" id="orderNum"
                                       name="OrderNum" placeholder="序号">
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-offset-2 col-sm-10">
                                <div class="radio">
                                    <label class="radio-inline"> <input type="radio"
                                                                        name="Enable" id="enable" value="1" checked> 启用
                                    </label> <label class="radio-inline"> <input type="radio"
                                                                                 name="Enable" id="disenable" value="0">
                                    禁用
                                </label>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-offset-2 col-sm-10">
                                <div class="radio">
                                    <label class="radio-inline"> <input type="radio"
                                                                        name="IsVisiable" id="visiable" value="1"
                                                                        checked> 可见
                                    </label> <label class="radio-inline"> <input type="radio"
                                                                                 name="IsVisiable" id="unvisiable"
                                                                                 value="0"> 不可见
                                </label>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="orderNum" class="col-sm-2 control-label">快捷功能</label>
                            <div class="col-sm-10">
                                <div class="radio">
                                    <label class="radio-inline"> <input type="radio"
                                                                        name="IsShortcut" value="1" id="isShortcut"> 是
                                    </label> <label class="radio-inline"> <input type="radio"
                                                                                 name="IsShortcut" value="0"
                                                                                 id="notShortcut" checked> 否
                                </label>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="level" class="col-sm-2 control-label">样式</label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control" id="btnStyle" name="BtnStyle"
                                       placeholder="快捷菜单按钮样式" disabled="disabled">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="orderNum" class="col-sm-2 control-label">备注</label>
                            <div class="col-sm-10">
									<textarea rows="3" class="form-control" id="remark"
                                              name="Remark" placeholder="备注"></textarea>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
