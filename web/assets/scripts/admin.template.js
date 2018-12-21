iv = {
    init: function () {
        this.bind();

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
        $('#btn-sync').click(function () {
            var param = $("#grid").gridSelectedCols("Id");
            if (param.Id.length > 0) {
                iv.sync(param.Id[0])
            } else {
                $.mdlg.alert('提示', "请选择要同步的模板")
            }
            ;
        })
        $('#btn-shelve').click(function () {
            var param = $("#grid").gridSelectedCols("Id");
            if (param.Id.length > 0) {
                iv.shelve(param.Id[0])
            } else {
                $.mdlg.alert('提示', "请选择要上架的模板")
            }
            ;
        })
        $('#btn-pull-off').click(function () {
            var param = $("#grid").gridSelectedCols("Id");
            if (param.Id.length > 0) {
                iv.pulloff(param.Id[0])
            } else {
                $.mdlg.alert('提示', "请选择要下架的模板")
            }
            ;
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
                    title: "编码",
                    width: "200px",
                    field: "Code",
                    encoded: false,
                    attributes: {
                        'class': 'center'
                    }
                },
                {
                    title: "是否同步成功",
                    width: "120px",
                    field: "IsSync",
                    encoded: false,
                    attributes: {
                        'class': 'center'
                    },
                    template: "# if (typeof(IsSync)!='undefined' && IsSync == 1) { #" +
                    "<span class=\"text-danger\">是</span>" +
                    "# }else{ #" +
                    "<span class=\"text-danger\">否</span>" +
                    " # } #"
                },
                {
                    title: "是否上架",
                    width: "120px",
                    field: "IsShelves",
                    encoded: false,
                    attributes: {
                        'class': 'center'
                    },
                    template: "# if (typeof(IsShelves)!='undefined' && IsShelves == 1) { #" +
                    "<span class=\"text-danger\">是</span>" +
                    "# }else{ #" +
                    "<span class=\"text-danger\">否</span>" +
                    " # } #"
                },
                {
                    title: "是否删除",
                    width: "120px",
                    field: "IsDel",
                    encoded: false,
                    attributes: {
                        'class': 'center text-danger'
                    },
                    values: isdel
                },
                {
                    title: "模板名称",
                    width: "200px",
                    field: "Name",
                    encoded: false,
                    attributes: {
                        'class': 'center'
                    }
                },
                {
                    title: "缩略图",
                    width: "350px",
                    field: "ImageUrl",
                    encoded: false,
                    template: "# if (typeof(ImageUrl)=='undefined' || ImageUrl == null) { #" +
                    "<span></span>" +
                    "# }else{ #" +
                    "<a class='js-img' href='" + staticUrl + "#=ImageUrl#'>#=ImageUrl#</a> # } #",
                    attributes: {
                        'class': 'center'
                    }
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
            dataSource: createDataSource(basePath + 'admin/template/templateList', {}),
            dataBound: function () {
                var rows = this.items();
                var page = this.pager.page() - 1;
                var pagesize = this.pager.pageSize();
                $(rows).each(function () {
                    var index = $(this).index() + 1 + page * pagesize;
                    var rowLabel = $(this).find(".row-number");
                    $(rowLabel).html(index);
                });

                $(".js-img").fancybox({
                    'transitionIn': 'none',
                    'transitionOut': 'none'
                });
            }
        });

        var filter = new Array();
        filter.push({
            field: "IsDel",
            operator: 'in',
            value: ["0"]
        });

        iv.getInfo(filter);
    },
    bindUpload: function () {
        $("#file-upload").uploadify({
            height: 34,
            swf: basePath + '/assets/scripts/uploadify.swf',
            uploader: basePath + '/admin/file/upload',
            width: 120,
            multi: false,
            buttonText: '选择图片',
            fileSizeLimit: 500,
            fileTypeDesc: '支持格式:jpg/gif/jpeg/png/bmp.',
            fileTypeExts: '*.jpg;*.gif;*.jpeg;*.png;*.bmp',
            onUploadSuccess: function (file, data, response) {
                data = $.parseJSON(data);

                if (data.result) {
                    $('#img-icon').attr('src', data.url);
                    $('#a-img').removeClass('hidden').attr('href', data.url);
                    $('#icon-url').val(data.path);
                    $("#a-img").fancybox({
                        'transitionIn': 'none',
                        'transitionOut': 'none'
                    });
                } else {
                    $.mdlg.error('错误', data.message);
                }
            }
        })
    },
    getInfo: function (data) {
        var grid = $('#grid').data('kendoGrid');
        grid.dataSource.filter(data);
    },
    edit: function (id) {
        $.mdlg({
            title: '模板',
            content: function () {
                return $('#data-tmpl').html();
            },
            onShow: function () {

                iv.bindUpload();

                if (id) {
                    $.get(basePath + 'admin/template/getTemplate', {
                        'Id': id
                    }, function (data) {
                        $('#form-template').formData(data);

                        $('#img-icon').attr('src', staticUrl + data.ImageUrl);
                        $('#a-img').removeClass('hidden').attr('href', staticUrl + data.ImageUrl);
                        $('#icon-url').val(data.ImageUrl);

                        $("#a-img").fancybox({
                            'transitionIn': 'none',
                            'transitionOut': 'none'
                        });
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
                    var params = $("#form-template").serializeJson();
                    $.post(basePath + 'admin/template/save', JSON.stringify(params), function (data) {
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
        $.mdlg.confirm("删除", "您确认要将所选择的模板么？", function () {
            var params = $("#grid").gridSelectedCols('Id');
            $.post(basePath + 'admin/template/delete', JSON.stringify(params), function (data) {
                if (data.result) {
                    $.mdlg.alert('提示', data.message);
                    iv.getInfo();
                } else {
                    $.mdlg.error('错误', data.message);
                }
            }).fail(errors);
        })
    },
    sync: function (id) {
        iv.syncModal("同步中...");
        $.get(basePath + 'admin/sync/syncSingleTemplate', {"id": id}, function (data) {
            if (data.result == true) {
                $("#myModal").modal('hide');
                $("#myModal").remove();
                $.mdlg.alert('提示', data.message);
                $(this).closeDialog(modal);
            } else {
                $("#myModal").modal('hide');
                $("#myModal").remove();
                $.mdlg.error('错误', data.message);
            }
        })
    },
    shelve: function (id) {
        iv.syncModal("上架中...");
        $.get(basePath + 'admin/sync/columnShelveSingle', {"id": id, "table": "template"}, function (data) {
            if (data.result == true) {
                $("#myModal").modal('hide');
                $("#myModal").remove();
                $.mdlg.alert('提示', data.message);
                $(this).closeDialog(modal);
            } else {
                $("#myModal").modal('hide');
                $("#myModal").remove();
                $.mdlg.error('错误', data.message);
            }
        })
    },
    pulloff: function (id) {
        iv.syncModal("下架中...");
        $.get(basePath + 'admin/sync/singlePullOff', {"id": id, "table": "template"}, function (data) {
            if (data.result == true) {
                $("#myModal").modal('hide');
                $("#myModal").remove();
                $.mdlg.alert('提示', data.message);
                $(this).closeDialog(modal);
            } else {
                $("#myModal").modal('hide');
                $("#myModal").remove();
                $.mdlg.error('错误', data.message);
            }
        })
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
