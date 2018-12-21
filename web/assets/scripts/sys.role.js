iv = {
    init: function () {
        this.bind();
        this.getInfo();
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
                    title: "编号",
                    width: "150px",
                    field: "Code",
                    encoded: false,
                    attributes: {
                        'class': 'center'
                    }
                },
                {
                    title: "角色名称",
                    width: "15%",
                    field: "Name",
                    encoded: false,
                    attributes: {
                        'class': 'left'
                    }
                },
                {
                    title: "角色等级",
                    width: "15%",
                    field: "Level",
                    encoded: false,
                    attributes: {
                        'class': 'center'
                    }
                },
                {
                    title: "是否启用",
                    width: "20%",
                    field: "Enable",
                    encoded: false,
                    attributes: {
                        'class': 'center'
                    },
                    template: "# if (Enable == 0) { #" +
                    "<span>关闭</span>" +
                    "# }else{ #" +
                    "<span>启用</span> # } #"
                },
                {
                    title: "是否系统级",
                    width: "20%",
                    field: "IsSystem",
                    encoded: false,
                    attributes: {
                        'class': 'center'
                    },
                    template: "# if (IsSystem == 0) { #" +
                    "<span>否</span>" +
                    "# }else{ #" +
                    "<span>是</span> # } #"
                }],
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
            dataSource: createDataSource(basePath + 'sys/role/roleList', {
                Code: {
                    type: "string"
                },
                Name: {
                    type: "string"
                },
                Level: {
                    type: "number"
                },
                Enable: {
                    type: "number"
                },
                IsSystem: {
                    type: "number"
                },
                OrderNum: {
                    type: "number"
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
    getInfo: function () {
        var grid = $('#grid').data('kendoGrid');
        grid.dataSource.filter({});
    },
    edit: function (id) {
        var p;
        //弹窗
        $.mdlg({
            title: '角色',
            width: '850px',
            content: function () {
                return $('#data-tmpl').html();
            },
            //页面需要预加载的内容
            onShow: function () {
                $('#txt-order').kendoNumericTextBox({
                    format: "#",
                    decimals: 0,
                    min: 1,
                    max: 99999999,
                    value: 1
                });
                //通过get请求异步获得指定id的用户信息，然后通过封装的formData方法，给每个标签对应name赋值
                if (id) {
                    $.get(basePath + 'sys/role/getRole', {
                        'id': id
                    }, function (data) {
                        $('#form-data').formData(data);
                        p = $("input[name='Code']").val();
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
                    params["BeforeCode"] = p;
                    $.post(basePath + 'sys/role/save', JSON.stringify(params), function (data) {
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
        $.mdlg.confirm("删除", "您确认要将所选择的角色么？", function () {
            var params = $("#grid").gridSelectedCols('Id');
            $.post(basePath + 'sys/role/delete', JSON.stringify(params), function (data) {
                if (data.result) {
                    $.mdlg.alert('提示', data.message);
                    iv.getInfo();
                } else {
                    $.mdlg.error('错误', data.message);
                }
            }).fail(errors);
        })
    },
}
