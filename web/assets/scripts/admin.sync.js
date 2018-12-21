iv = {
    init: function () {
        $('#btn-sync-category').click(function () {
            iv.syncModal("同步中...");
            $.post(basePath + 'admin/sync/syncCategory', function (data) {
                if (data.result == true) {
                    $("#myModal").modal('hide');
                    $("#myModal").remove();
                    $.mdlg.alert('提示', data.message);
                } else {
                    $("#myModal").modal('hide');
                    $("#myModal").remove();
                    $.mdlg.error('错误', data.message);
                }
            })
        })
        $('#btn-sync-customer').click(function () {
            iv.syncModal("同步中...");
            $.post(basePath + 'admin/sync/syncCustomer', function (data) {
                if (data.result == true) {
                    $("#myModal").modal('hide');
                    $("#myModal").remove();
                    $.mdlg.alert('提示', data.message);
                } else {
                    $("#myModal").modal('hide');
                    $("#myModal").remove();
                    $.mdlg.error('错误', data.message);
                }
            })
        })
        $('#btn-sync-content').click(function () {
            iv.syncModal("同步中...");
            $.post(basePath + 'admin/sync/syncContent', function (data) {
                if (data.result == true) {
                    $("#myModal").modal('hide');
                    $("#myModal").remove();
                    $.mdlg.alert('提示', data.message);
                } else {
                    $("#myModal").modal('hide');
                    $("#myModal").remove();
                    $.mdlg.error('错误', data.message);
                }
            })
        })
        $('#btn-sync-template').click(function () {
            iv.syncModal("同步中...");
            $.post(basePath + 'admin/sync/syncTemplate', function (data) {
                if (data.result == true) {
                    $("#myModal").modal('hide');
                    $("#myModal").remove();
                    $.mdlg.alert('提示', data.message);
                } else {
                    $("#myModal").modal('hide');
                    $("#myModal").remove();
                    $.mdlg.error('错误', data.message);
                }
            })
        })
        $('#btn-sync-page').click(function () {
            iv.syncModal("同步中...");
            $.post(basePath + 'admin/sync/syncPage', function (data) {
                if (data.result == true) {
                    $("#myModal").modal('hide');
                    $("#myModal").remove();
                    $.mdlg.alert('提示', data.message);
                } else {
                    $("#myModal").modal('hide');
                    $("#myModal").remove();
                    $.mdlg.error('错误', data.message);
                }
            })
        })
        $('#btn-column-shelves').click(function () {
            iv.syncModal("上架中...");
            $.post(basePath + 'admin/sync/columnShelve', function (data) {
                if (data.result == true) {
                    $("#myModal").modal('hide');
                    $("#myModal").remove();
                    $.mdlg.alert('提示', data.message);
                } else {
                    $("#myModal").modal('hide');
                    $("#myModal").remove();
                    $.mdlg.error('错误', data.message);
                }
            })
        })
        $('#btn-content-shelves').click(function () {
            iv.syncModal("上架中...");
            $.post(basePath + 'admin/sync/contentShelve', function (data) {
                if (data.result == true) {
                    $("#myModal").modal('hide');
                    $("#myModal").remove();
                    $.mdlg.alert('提示', data.message);
                } else {
                    $("#myModal").modal('hide');
                    $("#myModal").remove();
                    $.mdlg.error('错误', data.message);
                }
            })
        })
        $('#btn-sys-column-shelves').click(function () {
            $.mdlg({
                title: '栏目信息',
                content: function () {
                    return $('#data-tmpl').html();
                },
                showCloseButton: false,
                buttons: ["保存", "取消"],
                buttonStyles: ['btn-success', 'btn-default'],
                onButtonClick: function (sender, modal, index) {
                    var self = this;
                    var params = $("#form-process").serializeJson();
                    if (index == 0) {
                        iv.syncModal("上架中...");
                        var params = $("#form-process").serializeJson();
                        $.post(basePath + 'admin/sync/sysColumnShelve', JSON.stringify(params), function (data) {
                            if (data.result == true) {
                                $("#myModal").modal('hide');
                                $("#myModal").remove();
                                $.mdlg.alert('提示', data.message);
                                $('#form-process').clearForm();
                                $(this).closeDialog(modal);
                            } else {
                                $("#myModal").modal('hide');
                                $("#myModal").remove();
                                $.mdlg.error('错误', data.message);
                            }
                        })
                    } else {
                        $(this).closeDialog(modal);
                    }
                }
            })
        })
        $('#btn-template-column-removed,#btn-page-column-removed,#btn-category-column-removed,#btn-content-removed').click(function (e) {
            var node = e.target.tagName == 'I' ? $(e.target).parent() : $(e.target),
                title,
                url,
                type;
            switch (node.attr("id")) {
                case "btn-template-column-removed":
                    title = "模板信息";
                    url = "admin/sync/templateColumnShelveList";
                    type = "template";
                    break;
                case "btn-page-column-removed":
                    title = "页面信息";
                    url = "admin/sync/pageColumnShelveList";
                    type = "page";
                    break;
                case "btn-category-column-removed":
                    title = "分类信息";
                    url = "admin/sync/categoryColumnShelveList";
                    type = "category";
                    break;
                case "btn-content-removed":
                    title = "内容信息";
                    url = "admin/sync/contentShelveList";
                    type = "content";
                    break;
            }
            $.mdlg({
                title: title,
                content: function () {
                    return $('#grid-tmpl').html();
                },
                width: "850px",
                onShow: function () {
                    iv.bind(url);
                    iv.getInfo();
                },
                showCloseButton: false,
                buttons: ["下架", "取消"],
                buttonStyles: ['btn-success', 'btn-default'],
                onButtonClick: function (sender, modal, index) {
                    var self = this;
                    if (index == 0) {
                        var params = $("#grid").gridSelectedCols("Id");
                        params['table'] = type;
                        if (params.Id.length > 0) {
                            iv.syncModal("下架中...");
                            $.post(basePath + 'admin/sync/columnRemoved', JSON.stringify(params), function (data) {
                                if (data.result == true) {
                                    $("#myModal").modal('hide');
                                    $("#myModal").remove();
                                    $.mdlg.alert('提示', data.message);
                                    $('#form-process').clearForm();
                                    $(this).closeDialog(modal);
                                } else {
                                    $("#myModal").modal('hide');
                                    $("#myModal").remove();
                                    $.mdlg.error('错误', data.message);
                                }
                            })
                        }
                    } else {
                        $(this).closeDialog(modal);
                    }
                }
            })
        })
    },
    bind: function (url) {
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
                    width: "160px",
                    field: "Code",
                    encoded: false,
                    attributes: {
                        'class': 'center'
                    }
                },
                {
                    title: "栏目名称",
                    width: "150px",
                    field: "Name",
                    encoded: false,
                    attributes: {
                        'class': 'center'
                    }
                },
                {
                    title: "关联Id",
                    width: "100px",
                    field: "CorrelateID",
                    encoded: false,
                    attributes: {
                        'class': 'center'
                    }
                },
                {
                    title: "内容Id",
                    width: "100px",
                    field: "ContentID",
                    encoded: false,
                    attributes: {
                        'class': 'center'
                    }
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
            dataSource: createDataSource(basePath + url, {}),
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
    syncModal: function (content) {
        $('<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">' +
            '<div class="modal-dialog">' +
            '<div class="modal-content">' +
            '<div class="modal-header">' +
            '<h4 class="modal-title" id="myModalLabel">' +
            '提示' +
            '</h4>' +
            '</div>' +
            '<div class="modal-body">' +
            content +
            '</div>' +
            '<div class="modal-footer">' +
            '<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>' +
            '</div>' +
            '</div>' +
            '</div>' +
            '</div>').modal();
    }
}