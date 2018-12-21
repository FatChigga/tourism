<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <%@include file="/assets/inc.jsp" %>
    <script type="text/javascript" src="${basePath}assets/scripts/sys.permision.component.js"></script>
    <script type="text/javascript" src="${basePath}assets/scripts/jquery.md5.js"></script>

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
			<input type="hidden" name="Id" id="Id">
				<div class="form-group">
					<label class="col-sm-2 control-label">编号</label>
					<div class="col-sm-4">
						<div class="col-sm-11">
							<input type="text" class="form-control" placeholder="编号" maxlength="15" name="Code" >
						</div>
						<span class="cols-sm-1 required">*</span>
					</div>
					<label class="col-sm-2 control-label">名称</label>
						<div class="col-sm-4">
							<div class="col-sm-11">
								<input type="text" class="form-control" placeholder="名称" maxlength="40" name="Name"  >
							</div>
							<span class="cols-sm-1 required">*</span>
						</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">是否启用</label>
					<div class="col-sm-4">
						<div class="col-sm-11">
							<div class="radio">
								<label class="radio-inline"> <input type="radio"
									name="IsEnable" id="yes" value="1" checked> 是
								</label> <label class="radio-inline"> <input type="radio"
									name="IsEnable" id="no" value="0"> 否
								</label>
							</div>
						</div>
						<span class="cols-sm-1 required">*</span>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">样式(css)</label>
					<div class="col-sm-4">
						<div class="col-sm-11">
							<textarea class="form-control" rows="5" placeholder="css样式" name="CssContent" maxlength="50" style="width: 300%;"></textarea>
						</div>
					</div>
				</div>
		</form>

</script>
</body>
</html>
