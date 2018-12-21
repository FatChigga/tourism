<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>

<!DOCTYPE HTML>
<html lang="en">
<head>
    <%@include file="/assets/inc.jsp" %>
    <link href="${basePath}assets/content/zTreeStyle/zTreeStyle.css" rel="stylesheet">
    <script src="${basePath}assets/scripts/jquery.ztree.all.js"></script>
    <script src="${basePath}assets/scripts/sys.organization.js"></script>
</head>
<body>
<div class="container-fluid page-container">
    <div class="col-md-4">
        <div class="panel panel-default" data-fit="true" id="testPanel">
            <div class='panel-heading'>
                <h4 class='panel-title'>
                    组织机构 <a data-toggle='collapse' href='#collapse-testPanel'
                            aria-expanded='true' class='pull-right btn-collapse'> <i
                        class='fa fa-chevron-circle-up'></i>
                </a>
                </h4>
            </div>
            <div id="collapse-testPanel" class="panel-collapse collapse in">
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
                        <input type="hidden" name="Id" id="id">
                        <input type="hidden" name="ParentId" id="parentId">
                        <div class="form-group">
                            <label for="code" class="col-sm-2 control-label">机构编码</label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control" id="code" name="Code"
                                       maxlength="15" placeholder="机构编码">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="name" class="col-sm-2 control-label">机构名称</label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control" id="name" name="Name"
                                       maxlength="50" placeholder="机构名称">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="level" class="col-sm-2 control-label">机构层级</label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control" id="level" name="Level" placeholder="机构层级"
                                       maxlength="8" readonly="readonly">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="tel" class="col-sm-2 control-label">机构电话</label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control" id="tel"
                                       maxlength="15" name="Tel" placeholder="机构电话">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="fax" class="col-sm-2 control-label">机构传真</label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control" id="fax"
                                       maxlength="15" name="Fax" placeholder="机构传真">
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-offset-2 col-sm-10">
                                <div class="radio">
                                    <label class="radio-inline"> <input type="radio"
                                                                        name="Enable" id="enable" value="1"
                                                                        checked="checked"> 启用
                                    </label> <label class="radio-inline"> <input type="radio"
                                                                                 name="Enable" id="disenable" value="0">
                                    禁用
                                </label>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="remark" class="col-sm-2 control-label">备注</label>
                            <div class="col-sm-10">
									<textarea rows="5" class="form-control" id="remark"
                                              maxlength="230" name="Remark" placeholder="备注"></textarea>
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
