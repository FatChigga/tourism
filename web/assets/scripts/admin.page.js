iv = {
    init: function () {
        this.bindTemplete();
        this.bind();

        $("#btn-add").click(function () {
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
        });

        $('#btn-shelve').click(function () {
            var param = $("#grid").gridSelectedCols("Id");
            if (param.Id.length > 0) {
                iv.shelve(param.Id[0])
            } else {
                $.mdlg.alert('提示', "请选择要上架的栏目")
            }
            ;
        })
        $('#btn-pull-off').click(function () {
            var param = $("#grid").gridSelectedCols("Id");
            if (param.Id.length > 0) {
                iv.pulloff(param.Id[0])
            } else {
                $.mdlg.alert('提示', "请选择要下架的页面")
            }
            ;
        })
    },
    bindTemplete: function (code) {
        var self = this;
        var setting = {
            view: {
                nameIsHTML: true,
                showTitle: false
            },
            callback: {
                onClick: function (event, treeId, treeNode) {
                    $("#templateId").val(treeNode.id);
                    if (typeof(treeNode.name) != "undefined") {
                        if (treeNode.id == 0) {
                            iv.getInfo({});
                        } else {
                            var filter = [{field: "TemplateId", operator: "eq", value: treeNode.id}, {
                                field: "IsDel",
                                operator: 'in',
                                value: ["0"]
                            }];
                            iv.getInfo(filter)
                        }
                    } else {
                        $("#grid").empty();
                    }
                }
            }
        };

        $.get(basePath + 'admin/template/templateNodes', null, function (data) {
            zTreeObj = $.fn.zTree.init($("#tree-template"), setting, data);
        })
    },
    bind: function () {
        $("#grid").kendoGrid({
            columns: [
                {
                    title: "序号",
                    width: "60px",
                    field: "Id",
                    encoded: false,
                    attributes: {'class': 'center'},
                    filterable: false,
                    sortable: false,
                    template: "<span class='row-number'></span>"
                },
                {
                    title: "编码",
                    width: "150px",
                    field: "Code",
                    encoded: false,
                    sortable: false,
                    attributes: {
                        'class': 'center'
                    }
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
                        'class': 'center'
                    },
                    values: isdel
                },
                {
                    title: "名称",
                    width: "250px",
                    field: "Name",
                    encoded: false,
                    attributes: {
                        'class': 'center'
                    }
                },
                {
                    title: "所属模版",
                    width: "250px",
                    field: "TemplateName",
                    encoded: false,
                    attributes: {
                        'class': 'center'
                    }
                },
                {
                    title: "缩略图",
                    width: "280px",
                    field: "ImageUrl",
                    encoded: false,
                    filterable: false,
                    template: "# if (typeof(ImageUrl)=='undefined' || ImageUrl == null) { #" +
                    "<span></span>" +
                    "# }else{ #" +
                    "<a class='js-img' href='" + staticUrl + "#=ImageUrl#'>#=ImageUrl#</a> # } #",
                    attributes: {
                        'class': 'center'
                    }
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
            dataSource: createDataSource(basePath + 'admin/page/pageList', {}),
            dataBound: function () {
                fit();

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
    getInfo: function (data) {
        var grid = $('#grid').data('kendoGrid');
        grid.dataSource.filter(data);
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
        });
    },
    edit: function (id) {
        if (typeof(id) == "undefined") {
            var treeObj = $.fn.zTree.getZTreeObj("tree-template");
            var nodes = treeObj.getSelectedNodes();
            if (nodes.length == 0) {
                $.mdlg.error('提示', "请选择模板");
                return;
            }
            if (nodes[0].id == 0) {
                $.mdlg.error('提示', "请选择模板");
                return;
            }
        }
        $.mdlg({
            title: '编辑页面',
            width: '550px',
            content: function () {
                return $('#data-tmpl-page').html();
            },
            onShow: function () {
                iv.bindUpload();

                if (id) {
                    $.get(basePath + 'admin/page/getPage', {
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
            onButtonClick: function (sender, modal, index) {
                var self = this;
                if (index == 0) {
                    var treeObj = $.fn.zTree.getZTreeObj("tree-template");
                    var nodes = treeObj.getSelectedNodes();
                    if (nodes.length != 0) {
                        $("#templateId").val(nodes[0].id);
                    }
                    var params = $("#form-template").serializeJson();
                    $.post(basePath + 'admin/page/savePage', JSON.stringify(params), function (data) {
                        if (data.result == true) {
                            $.mdlg.alert('提示', data.message);
                            var treeObj = $.fn.zTree.getZTreeObj("tree-template");
                            var nodes = treeObj.getSelectedNodes();
                            if (nodes.length != 0 && nodes[0].id != 0) {
                                $("#" + nodes[0].tId).find('a').trigger('click');
                            } else {
                                iv.getInfo({});
                            }
                            $(this).closeDialog(modal);
                        } else {
                            $.mdlg.error('错误', data.message);
                        }
                    })
                } else {
                    $(this).closeDialog(modal);
                }
            }
        });
    },
    delete: function () {
        var param = $("#grid").gridSelectedCols('Id');
        if (param.Id.length == 0) {
            $.mdlg.error('提示', "请选择要删除的数据");
            return;
        }

        $.mdlg.confirm("删除", "您确认要将所选择的数据吗？", function () {

            $.post(basePath + 'admin/page/deletePage', JSON.stringify(param), function (data) {
                if (data.result) {
                    $.mdlg.alert('提示', data.message);
                    var treeObj = $.fn.zTree.getZTreeObj("tree-template");
                    var nodes = treeObj.getSelectedNodes();
                    if (nodes.length > 0 && nodes[0].id != 0) {
                        $("#" + nodes[0].tId).find('a').trigger('click');
                    } else {
                        iv.getInfo({});
                    }

                } else {
                    $.mdlg.error('错误', data.message);
                }
            }).fail(errors);
        });
    },
    shelve: function (id) {
        iv.syncModal("上架中...");
        $.get(basePath + 'admin/sync/columnShelveSingle', {"id": id, "table": "page"}, function (data) {
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
        $.get(basePath + 'admin/sync/singlePullOff', {"id": id, "table": "page"}, function (data) {
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
