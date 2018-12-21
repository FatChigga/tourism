iv = {
    init: function () {
        this.bind();
        this.getInfo();

        $('#btn-add').click(function () {
            iv.edit();
        })
        $('#btn-edit').click(function () {
            var param = $("#grid").gridSelectedCols("Id");
            if (param.Id.length > 0) {
                iv.edit(param.Id[0]);
            }
        })
        $('#btn-del').click(function () {
            iv.delete();
        })
    },
    bind: function () {
        var isEnableList = [{"text": "启用", "value": "1"}, {"text": "禁用", "value": "0"}];

        $("#grid").kendoGrid({
            columns: [
                {
                    title: "序号",
                    width: "80px",
                    field: "Id",
                    encoded: false,
                    attributes: {'class': 'center'},
                    filterable: false,
                    template: "<span class='row-number'></span>"
                },
                {
                    title: "编号",
                    width: "20%",
                    field: "Code",
                    encoded: false,
                    attributes: {'class': 'center'}
                },
                {
                    title: "名称",
                    width: "20%",
                    field: "Name",
                    encoded: false,
                    attributes: {'class': 'center'}
                },
                {
                    title: "图标",
                    width: "20%",
                    field: "IconCss",
                    encoded: false,
                    attributes: {'class': 'center'},
                    template: "#if(IconCss != null){ #" +
                    "<i class='fa #=IconCss#'></i> #}#"
                },
                {
                    title: "是否启用",
                    width: "20%",
                    field: "IsEnable",
                    encoded: false,
                    attributes: {'class': 'center'},
                    values: isEnableList
                }],
            page: 1,
            filterable: true,
            selectable: "Multiple, Row",
            scrollable: true,
            resizable: true,
            pageable: {
                "refresh": true,
                "autoBind": false,
                "input": true,
                "buttonCount": 10
            },
            autoBind: false,
            dataSource: createDataSource(basePath + 'sys/permision/permisionList', {
                IsEnable: {type: "number"},
                IconCss: {type: "string"}
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
        $.mdlg({
            title: '编辑权限',
            content: function () {
                return $('#data-tmpl').html();
            },
            onShow: function () {
                if (id) {
                    $.get(basePath + 'sys/permision/getPermision', {Id: id}, function (data) {
                        $('#form-data').formData(data);
                        p = $("input[name='Code']").val();
                    });
                }
                $("#btn-search").click(function () {
                    var t;
                    $.mdlg({
                        title: '图标列表',
                        content: function () {
                            return $('#icon-tmpl').html();
                        },
                        onShow: function () {
                            $(".fa-hover").click(function (e) {
                                $(".sd").css("background-color", "").css("color", "#222").css("text-decoration", "").removeClass("sd");
                                t = $(e.target)[0].tagName == 'A' ? $(e.target) : $(e.target).parent();
                                $(t).css("background-color", "#1d9d74");
                                $(t).css("color", "#fff");
                                $(t).css("text-decoration", "none");
                                $(t).addClass("sd");
                            })
                        },
                        showCloseButton: false,
                        buttons: ["保存", "取消"],
                        buttonStyles: ['btn-success', 'btn-default'],
                        onButtonClick: function (sender, modal, index) {
                            $("input[name='IconCss']").val(t.children("i").attr("class").substring(3));
                            $(this).closeDialog(modal);
                        }
                    })
                })
            },
            showCloseButton: false,
            buttons: ["保存", "取消"],
            buttonStyles: ['btn-success', 'btn-default'],
            onButtonClick: function (sender, modal, index) {
                var self = this;

                if (index == 0) {
                    var params = $("#form-data").serializeJson();
                    params["BeforeCode"] = p;
                    $.post(basePath + 'sys/permision/save', JSON.stringify(params), function (data) {
                        if (data.result == true) {
                            $.mdlg.alert('提示', data.message);
                            $('#form-data').clearForm();
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
        $.mdlg.confirm("删除", "您确认要删除么？", function () {
            var params = $("#grid").gridSelectedCols('Id');

            $.post(basePath + 'sys/permision/delete', JSON.stringify(params), function (data) {
                if (data.result) {
                    $.mdlg.alert('提示', data.message);
                    iv.getInfo();
                } else {
                    $.mdlg.error('错误', data.message);
                }
            }).fail(errors);
        })
    }
}