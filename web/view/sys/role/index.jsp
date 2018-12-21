<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <%@include file="/assets/inc.jsp" %>
    <script type="text/javascript" src="${basePath}assets/scripts/sys.role.js"></script>
    <script type="text/javascript" src="${basePath}assets/scripts/jquery.md5.js"></script>
    <script type="text/javascript">
    </script>
</head>
<body>
<div class="container-fluid page-container">
    <div class="panel panel-default" data-fit="true" id="editPanel">
        <div class='toolbar'>
            <button id='btn-add' class='btn btn-default' type='Button' data-permision='Add'>新增</button>
            <button id='btn-edit' class='btn btn-default' type='Button' data-permision='Edit'>编辑</button>
            <button id='btn-del' class='btn btn-default' type='Button' data-permision='Delete'>删除</button>
            <div class="wrap-collapse">
                <a data-toggle='collapse' href='#collapse-editPanel'
                   aria-expanded='true' class='pull-right btn-collapse'> <i
                        class='fa fa-chevron-circle-up'></i>
                </a>
            </div>
        </div>
        <div id="collapse-editPanel" class="panel-collapse collapse in">
            <div class="">
                <div id="grid"></div>
            </div>
        </div>
    </div>
</div>
<script type="text/x-jquery-tmpl" id="data-tmpl">
		<form class="form-horizontal" id="form-data">
			<input type="hidden" name="Id" id="menuId">
				<div class="form-group">
					<label class="col-sm-2 control-label">编号</label>
					<div class="col-sm-4">
						<div class="col-sm-11">
							<input type="text" class="form-control" placeholder="编号" maxlength="15" name="Code" >
						</div>
						<span class="cols-sm-1 required">*</span>
					</div>
					<label class="col-sm-2 control-label">角色名称</label>
					<div class="col-sm-4">
						<div class="col-sm-11">
							<input type="text" class="form-control" placeholder="角色名称" maxlength="20" name="Name" >
						</div>
						<span class="cols-sm-1 required">*</span>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">角色等级</label>
					<div class="col-sm-4">
						<div class="col-sm-11">
							<input type="text" class="form-control" placeholder="角色等级" maxlength="8" name="Level"  >
						</div>
						<span class="cols-sm-1 required">*</span>
					</div>
					<label class="col-sm-2 control-label">是否启用</label>
					<div class="col-sm-4">
						<div class="col-sm-11">
							<div class="radio">
								<lable class="radio-inline"><input type="radio"  value="0" name="Enable">否</lable>
								<lable class="radio-inline"><input type="radio"  value="1" name="Enable" checked="checked">是</lable>
							</div>
						</div>
						<span class="cols-sm-1 required">*</span>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">是否系统级</label>
					<div class="col-sm-4">
						<div class="col-sm-11">
							<div class="radio">
								<lable class="radio-inline"><input type="radio"  value="0" name="IsSystem" checked="checked">否</lable>
								<lable class="radio-inline"><input type="radio"  value="1" name="IsSystem">是</lable>
							</div>
						</div>
						<span class="cols-sm-1 required">*</span>
					</div>
					<label class="col-sm-2 control-label">排序号</label>
						<div class="col-sm-4">
							<div class="col-sm-11">
								<input type="text" class="form-control" id="txt-order" name="OrderNum" placeholder="排序号" maxlength="8">
							</div>
							<span class="cols-sm-1 required">*</span>
						</div>
				</div>
		</form>

</script>
</body>
</html>
