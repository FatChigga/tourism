iv = {
    init: function () {
        this.bind();
        this.getInfo();
        //this.chart2();
        $("#btn-add").click(function () {
            iv.edit();
        })
        $('#btn-edit').click(function () {
            var param = $("#grid").gridSelectedCols("Id");
            if (param.Id.length > 0) iv.edit(param.Id[0]);
        })
        $('#btn-del').click(function () {
            iv.delete();
        })
        $('#btn-synchronize').click(function () {
            $.mdlg.alert('提示', '正在同步中...');
            iv.synchronize();
        })
    },
    bind: function () {
        //生成表格
        $("#grid").kendoGrid({
            columns: [{
                title: "序号",
                width: "80px",
                field: "Id",
                encoded: false,
                attributes: {'class': 'center'},
                filterable: false,
                sortable: false,
                template: "<span class='row-number'></span>"
            },
                {
                    title: "用户编号",
                    width: "150px",
                    field: "Code",
                    encoded: false,
                    attributes: {
                        'class': 'center'
                    }
                },
                {
                    title: "登录名",
                    width: "15%",
                    field: "LoginName",
                    encoded: false,
                    attributes: {
                        'class': 'left'
                    }
                },
                {
                    title: "真实姓名",
                    width: "15%",
                    field: "UserName",
                    encoded: false,
                    attributes: {
                        'class': 'left'
                    }
                },
                {
                    title: "所属机构",
                    width: "20%",
                    field: "OrganizationName",
                    encoded: false,
                    attributes: {
                        'class': 'left'
                    }
                },
                {
                    title: "性别",
                    width: "20%",
                    field: "Gender",
                    encoded: false,
                    attributes: {
                        'class': 'center'
                    },
                    template: "# if (Gender == 1) { #" +
                    "<span>男</span>" +
                    "# }else{ #" +
                    "<span>女</span> # } #"
                },
                {
                    title: "电话",
                    width: "20%",
                    field: "Tel",
                    encoded: false,
                    attributes: {
                        'class': 'left'
                    }
                },
                {
                    title: "邮箱",
                    width: "20%",
                    field: "Email",
                    encoded: false,
                    attributes: {
                        'class': 'left'
                    }
                },
                {
                    title: "用户角色",
                    width: "20%",
                    field: "RoleName",
                    encoded: false,
                    attributes: {
                        'class': 'left'
                    }
                },
                {
                    title: "部门负责人",
                    width: "100px",
                    field: "IsLeader",
                    encoded: false,
                    attributes: {
                        'class': 'center'
                    },
                    template: "# if (IsLeader == 1) { #" +
                    "<span>是</span>" +
                    "# }else{ #" +
                    "<span>否</span> # } #"
                }
            ],
            page: 1,
            filterable: true,
            selectable: "Multiple, Row",
            scrollable: true,
            sortable: true,
            resizable: true,
            pageable: {
                "refresh": true,
                "autoBind": false,
                "input": true,
                "buttonCount": 10
            },
            autoBind: false,
            //向服务器发送ajax请求，返回的数据对应每个字段的值
            dataSource: createDataSource(basePath + 'sys/user/userList', {
                LoginName: {
                    type: "string"
                },
                UserName: {
                    type: "string"
                },
                OrganizationId: {
                    type: "number"
                },
                Gender: {
                    type: "number"
                },
                Tel: {
                    type: "string"
                },
                Email: {
                    type: "string"
                },
                RoleName: {
                    type: "string"
                },
                Code: {
                    type: "string"
                }
            }),
            dataBound: function () {
                var rows = this.items();
                var page = this.pager.page() - 1;
                var pagesize = this.pager.pageSize();
                $(rows).each(function () {
                    var index = $(this).index() + 1 + page * pagesize;
                    var rowLabel = $(this).find(".row-number");
                    $(rowLabel).html(index);
                });
            }
        });
    },
    bindDropDownList: function () {
        $('#organizationId').kendoDropDownList({
            dataTextField: "text",
            dataValueField: "value",
            optionLabel: '==请选择组织机构==',
            filter: "contains",
            dataSource: createDataSource(basePath + 'sys/user/orgOptions', {})
        });
    },
    getInfo: function () {
        var grid = $('#grid').data('kendoGrid');
        grid.dataSource.filter({});
    },
    edit: function (id) {
        var l;
        //弹窗
        $.mdlg({
            title: '用户',
            width: '850px',
            content: function () {
                return $('#data-tmpl').html();
            },
            //页面需要预加载的内容
            onShow: function () {
                iv.bindDropDownList();
                $('#rdi-sex').radioButtonList(sex, 'Gender', 'Text', 'value', 1);
                //通过get请求异步获得指定id的用户信息，然后通过封装的formData方法，给每个标签对应name赋值
                if (id) {
                    $.get(basePath + 'sys/user/getUser', {
                        'id': id
                    }, function (data) {
                        $('#form-data').formData(data);
                        iv.bindDropDownList();
                        $('#roleList').html(data.RoleName);
                        $("input[name='ConfrimPassword']").val($("input[name='Password']").val());
                        l = $("input[name='LoginName']").val();
                    });
                }
            },
            showCloseButton: false,
            buttons: ["保存", "取消"],
            buttonStyles: ['btn-success', 'btn-default'],
            //点击事件
            onButtonClick: function (sender, modal, index) {
                var self = this;
                if (index == 0) {
                    var params = $("#form-data").serializeJson();
                    params["BeforeLoginName"] = l;
                    $.post(basePath + 'sys/user/save', JSON.stringify(params), function (data) {
                        if (data.result == true) {
                            $.mdlg.alert('提示', data.message);
                            iv.getInfo();
                            $(this).closeDialog(modal);
                        } else {
                            $.mdlg.error('错误', data.message);
                        }
                    })
                } else {
                    $(this).closeDialog(modal);
                }
            }
        })
    },
    delete: function () {
        $.mdlg.confirm("删除", "您确认要将所选择的用户么？", function () {
            var params = $("#grid").gridSelectedCols('Id');
            $.post(basePath + 'sys/user/delete', JSON.stringify(params), function (data) {
                if (data.result) {
                    $.mdlg.alert('提示', data.message);
                    iv.getInfo();
                } else {
                    $.mdlg.error('错误', data.message);
                }
            }).fail(errors);
        })
    },
    synchronize: function () {
        $.post(basePath + 'sys/user/synchronize', function (data) {
            $('.modal').modal('hide');
            if (data.result) {
                $.mdlg.alert('提示', data.message);
                iv.getInfo();
            } else {
                $.mdlg.error('错误', data.message);
            }
        }).fail(errors);
    }
}
