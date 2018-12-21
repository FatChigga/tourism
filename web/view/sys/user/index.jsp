<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <%@include file="/assets/inc.jsp" %>
    <script type="text/javascript" src="${basePath}assets/scripts/sys.user.js"></script>
    <script type="text/javascript" src="${basePath}assets/scripts/jquery.md5.js"></script>
    <script type="text/javascript">
        var sex = ${sex};
    </script>
</head>
<body>
<div class="container-fluid page-container">
    <div class="panel panel-default" data-fit="true" id="editPanel">
        <div class='toolbar'>
            <button id='btn-add' class='btn btn-default' type='Button' data-permision='Add'>新增</button>
            <button id='btn-edit' class='btn btn-default' type='Button' data-permision='Edit'>编辑</button>
            <button id='btn-del' class='btn btn-default' type='Button' data-permision='Delete'>删除</button>
            <button id='btn-synchronize' class='btn btn-default' type='Button'><i class="fa fa-spinner"></i>同步</button>
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
					<label class="col-sm-2 control-label">用户编号</label>
					<div class="col-sm-4">
						<div class="col-sm-11">
							<input type="text" class="form-control" placeholder="用户编号" maxlength="10" name="Code" >
						</div>
						<span class="cols-sm-1 required">*</span>
					</div>
					<label class="col-sm-2 control-label">登录名</label>
						<div class="col-sm-4">
							<div class="col-sm-11">
								<input type="text" class="form-control" placeholder="登录名" maxlength="15" name="LoginName"  >
							</div>
							<span class="cols-sm-1 required">*</span>
						</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">真实姓名</label>
					<div class="col-sm-4">
						<div class="col-sm-11">
							<input type="text" class="form-control" placeholder="真实姓名" maxlength="10" name="UserName" >
						</div>
						<span class="cols-sm-1 required">*</span>
					</div>
					<label class="col-sm-2 control-label">所属机构</label>
					<div class="col-sm-4">
						<div class="col-sm-11">
							<input type="text" class="form-control" placeholder="所属机构" maxlength="15" name="OrganizationId" id="organizationId">
						</div>
						<span class="cols-sm-1 required">*</span>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">密码</label>
					<div class="col-sm-4">
						<div class="col-sm-11">
							<input type="password" class="form-control" placeholder="密码" maxlength="20" name="Password">
						</div>
						<span class="cols-sm-1 required">*</span>
					</div>
                    <label class="col-sm-2 control-label">确认密码</label>
					<div class="col-sm-4">
						<div class="col-sm-11">
							<input type="password" class="form-control" placeholder="再次输入密码" maxlength="20" name="ConfrimPassword">
						</div>
						<span class="cols-sm-1 required">*</span>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">性别</label>
					<div class="col-sm-4">
						<div class="col-sm-11" id="rdi-sex">
						</div>
						<span class="cols-sm-1 required">*</span>
					</div>
					<label class="col-sm-2 control-label">电话</label>
						<div class="col-sm-4">
							<div class="col-sm-11">
								<input type="text" class="form-control" placeholder="电话" maxlength="13" name="Tel">
							</div>
							<span class="cols-sm-1 required">*</span>
						</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">邮箱</label>
					<div class="col-sm-4">
						<div class="col-sm-11">
							<input type="text" class="form-control" placeholder="邮箱" maxlength="25" name="Email">
						</div>
						<span class="cols-sm-1 required">*</span>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">用户角色</label>
					<div class="col-sm-9 control-label" style="text-align:left" id="roleList">
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">是否部门负责人</label>
					<div class="col-sm-9" style="text-align:left">
						<div class="radio">
							<label class="radio-inline"><input type="radio" name="IsLeader" value="1">是</label>
							<label class="radio-inline"><input type="radio" name="IsLeader" value="0" checked="checked">否</label>
						</div>
					</div>
				</div>
		</form>

</script>
</body>
</html>
