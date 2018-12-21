iv = {
    init: function () {
        this.bindData();
        var self = this;
        $("#btn-add").click(function () {
            iv.edit();
        })

        $('#btn-edit').click(function () {
            var treeObj = $.fn.zTree.getZTreeObj("tree-category");
            var nodes = treeObj.getSelectedNodes();
            if (typeof(nodes[0]) != "undefined") {

                if (nodes[0].id != '0') {
                    var param = $("#grid").gridSelectedCols("Id");
                    if (param.Id.length > 0) {
                        iv.edit(param.Id[0]);
                    }
                }
            }
        })

        $('#btn-del').click(function () {
            iv.delete();
        });

        $("#btn-add-left").click(function () {
            iv.editLeft();
        })
        $('#btn-edit-left').click(function () {
            iv.editLeft("");
        });
        $('#btn-del-left').click(function () {
            iv.deleteLeft();
        });
    },
    bindData: function (code) {
        var self = this;
        var setting = {
            view: {
                nameIsHTML: true,
                showTitle: false
            },
            callback: {
                onClick: function (event, treeId, treeNode) {
                    if (typeof(treeNode.KeyName) != "undefined") {
                        var data = {"KeyName": treeNode.KeyName, "KeyCode": treeNode.KeyCode};
                        iv.bind(data)
                    } else {
                        $("#grid").empty();
                    }
                    /*$.post(basePath + 'admin/bizParams/getParam', JSON.stringify(
                     data  //  'KeyCode': key,'KeyValue':value
                     ), function(data) {
                     $('#form-dictionary').formData(data);

                     beforeKey = key;
                     beforeValue = value;
                     });*/
                }
            }
        };

        $.get(basePath + 'admin/bizParams/bizParamsNodes', null, function (data) {
            zTreeObj = $.fn.zTree.init($("#tree-category"), setting, data);
            if (typeof(code) != 'undefined') {
                var treeObj = $.fn.zTree.getZTreeObj("tree-category");
                var node = treeObj.getNodeByParam("KeyCode", code, null);
                treeObj.selectNode(node, true, false);
                var obj = $(".curSelectedNode");
                obj.trigger('click');
            }
        })

        $('input[name="OrderNum"]').kendoNumericTextBox({
            format: "#",
            decimals: 0,
            min: 1,
            max: 99999999,
            value: 1
        });
    },
    bind: function (data) {
        $("#grid").kendoGrid({
            columns: [
                {
                    title: "序号",
                    width: "40px",
                    field: "Id",
                    encoded: false,
                    attributes: {'class': 'center'},
                    filterable: false,
                    sortable: false,
                    template: "<span class='row-number'></span>"
                },
                {
                    title: "参数编码",
                    width: "150px",
                    field: "KeyCode",
                    encoded: false,
                    sortable: false,
                    attributes: {
                        'class': 'center'
                    }
                },
                {
                    title: "参数值",
                    width: "60px",
                    field: "KeyValue",
                    encoded: false,
                    //    template: "<a class='js-img' href='"+staticUrl+"#=KeyValue#'>#=KeyValue#</a>",
                    attributes: {
                        'class': 'center'
                    }
                },
                {
                    title: "显示文本",
                    width: "90px",
                    field: "Text",
                    encoded: false,
                    attributes: {
                        'class': 'center'
                    }
                },
                {
                    title: "是否启用",
                    width: "80px",
                    field: "IsEnable",
                    encoded: false,
                    attributes: {
                        'class': 'center'
                    },
                    template: "# if (IsEnable == 0) { #" +
                    "<span>关闭</span>" +
                    "# }else{ #" +
                    "<span>启用</span> # } #"
                },
                {
                    title: "是否可见",
                    width: "80px",
                    field: "IsVisible",
                    encoded: false,
                    attributes: {
                        'class': 'center'
                    },
                    template: "# if (IsVisible ==false) { #" +
                    "<span>不可见</span>" +
                    "# }else{ #" +
                    "<span>可见</span> # } #"
                },
                {
                    title: "备注",
                    width: "20%",
                    field: "Remark",
                    encoded: false
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
            dataSource: createDataSource(basePath + 'admin/bizParams/bizParamsList', {
                KeyCode: {type: "string"},
                KeyValue: {type: "string"},
                Text: {type: "string"},
                IsEnable: {type: "number"},
                IsVisible: {type: "boolen"},
                OrderNum: {type: "number"},
                Remark: {type: "string"}
            }),//{field:'KeyCode'}
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
                /*$(".js-img").fancybox({
                 'transitionIn'	: 'none',
                 'transitionOut'	: 'none'	
                 });*/
            }
        });
        iv.getInfo(data);
    },
    getInfo: function (data) {
        var grid = $('#grid').data('kendoGrid');
        grid.dataSource.filter(data);
    },
    bindUpload: function () {
        $('#upload').uploadify({
            height: 34,
            swf: basePath + '/assets/scripts/uploadify.swf',
            uploader: basePath + '/admin/file/upload',
            width: 120,
            multi: false,
            buttonText: '选择文件',
            fileSizeLimit: 4000,
            fileTypeDesc: '支持格式:jpg/gif/jpeg/png/bmp/txt/rar/zip/excel/word/ppt.',
            fileTypeExts: '*.jpg;*.gif;*.jpeg;*.png;*.bmp;*.txt;*.rar;*.zip;*.excel;*.word;*.ppt',
            onUploadSuccess: function (file, data, response) {
                data = $.parseJSON(data);

                if (data.result) {
                    $('#file-name').html(file.name);
                    $('#file-url').val(data.path);
                } else {
                    $.mdlg.error('错误', data.message);
                }
            }
        });
    },
    editLeft: function (sign) {
        if (typeof(sign) != "undefined") {
            var treeObj = $.fn.zTree.getZTreeObj("tree-category");
            var nodes = treeObj.getSelectedNodes();
            if (nodes.length == 0) {
                $.mdlg.error('提示', "请选择业务参数");
                return;
            }
        }
        var beforeKey;
        var beforeValue;
        $.mdlg({
            title: '业务参数',
            width: '400px',
            content: function () {
                return $('#data-tmpl-left').html();
            },
            onShow: function () {
                if (typeof(sign) != "undefined") {
                    var treeObj = $.fn.zTree.getZTreeObj("tree-category");
                    var nodes = treeObj.getSelectedNodes();
                    if (nodes.length > 0) {
                        $('#data-tmpl-left').formData(nodes[0]);
                        beforeKey = nodes[0].KeyName;
                        beforeValue = nodes[0].KeyCode;
                    }
                }
            },
            showCloseButton: false,
            buttons: ["保存", "取消"],
            buttonStyles: ['btn-success', 'btn-default'],
            onButtonClick: function (sender, modal, index) {
                var self = this;
                if (index == 0) {
                    var params = $("#form-dictionary-left").serializeJson();
                    params["sign"] = "left";
                    params['BeforeValue'] = beforeValue;
                    params['BeforeKey'] = beforeKey;
                    $.post(basePath + 'admin/bizParams/update', JSON.stringify(params), function (data) {
                        if (data.result == true) {
                            $.mdlg.alert('提示', data.message);
                            console.log(params.KeyCode);
                            iv.bindData(params.KeyCode);
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
    edit: function (id) {
        var treeObj = $.fn.zTree.getZTreeObj("tree-category");
        var nodes = treeObj.getSelectedNodes();
        if (nodes.length == 0) {
            $.mdlg.error('提示', "请选择业务参数");
            return;
        }
        if ($("#grid").html() == '') {
            $.mdlg.error('提示', "请选择业务参数");
            return;
        }
        var beforeKey;
        var beforeValue;
        var dataParam;
        var id;
        $.mdlg({
            title: '业务参数',
            width: '850px',
            content: function () {
                return $('#data-tmpl').html();
            },
            onShow: function () {
                $('#txt-order').kendoNumericTextBox({
                    format: "#",
                    decimals: 0,
                    min: 1,
                    max: 99999999,
                    value: 1
                });

                $('input:radio[name="IsFile"]').change(function () {
                    if (this.value == 1) {
                        $('#file-url').val('');
                        $('#file-name').html('');
                        $('#text').show();
                        $('#file').hide();
                    } else {
                        $('#text-content').val('');
                        $('#text').hide();
                        $('#file').show();
                        iv.bindUpload();
                    }
                })

//                if (typeof(key)!='undefined' && typeof(value)!='undefined') {
                if (id) {
                    var parma = {"Id": id, "sign": "right"};
                    $.post(basePath + 'admin/bizParams/getParam', JSON.stringify(parma), function (data) {
                        $('#form-dictionary').formData(data);
                        var regex = '\/.*\/.*\.(jpg|gif|jpeg|png|bmp|txt|rar|zip|excel|word|ppt)?'
                        if (typeof(data.KeyValue) != 'undefined' && data.KeyValue.match(regex)) {
                            iv.bindUpload();
                            $('#file').show();
                            $('#text').hide();
                            $('#text-content').val('');
                            var arr = data.KeyValue.split('\/');
                            $('#file-name').html(arr.pop());
                            $('input:radio[name="IsFile"]:eq(0)').attr("checked", 'checked');
                        } else {
                            $('#file-url').val('');
                            $('#file-name').html('');
                        }
                        if (typeof(data.OrderNum) == 'undefined') {
                            $("#txt-order").val(1);
                        }

                        beforeKey = data.KeyCode;
                        beforeValue = data.KeyValue;
                    });
                }
                var treeObj = $.fn.zTree.getZTreeObj("tree-category");
                var nodes = treeObj.getSelectedNodes();
                if (nodes.length > 0) {
                    dataParam = {"KeyName": nodes[0].KeyName, "KeyCode": nodes[0].KeyCode}
                    $("#name").val(nodes[0].KeyName);
                    $("#code").val(nodes[0].KeyCode);
                }
            },
            showCloseButton: false,
            buttons: ["保存", "取消"],
            buttonStyles: ['btn-success', 'btn-default'],
            onButtonClick: function (sender, modal, index) {
                var self = this;
                if (index == 0) {
                    var params = $("#form-dictionary").serializeJson();
                    params["Id"] = id;
                    params['BeforeValue'] = beforeValue;
                    params['BeforeKey'] = beforeKey;
                    params['sign'] = "right";
                    $.post(basePath + 'admin/bizParams/update', JSON.stringify(params), function (data) {
                        if (data.result == true) {
                            $.mdlg.alert('提示', data.message);
                            iv.getInfo(dataParam);
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
        if ($("#grid").html() == '') {
            $.mdlg.error('提示', "请选择业务参数");
            return;
        }
        var param = $("#grid").gridSelectedCols('Id');
        if (param.Id.length == 0) {
            $.mdlg.error('提示', "请选择要删除的数据");
            return;
        }

        $.mdlg.confirm("删除", "您确认要将所选择的业务参数吗？", function () {
            var keyParam = $("#grid").gridSelectedCols("KeyCode");
            var valueParam = $("#grid").gridSelectedCols("KeyValue");
            param["sign"] = "right";
//            param['KeyCode'] = keyParam.KeyCode;
//            param['KeyValue'] = valueParam.KeyValue;
//            param['Id'] = id;

            $.post(basePath + 'admin/bizParams/delete', JSON.stringify(param), function (data) {
                if (data.result) {
                    $.mdlg.alert('提示', data.message);
                    var treeObj = $.fn.zTree.getZTreeObj("tree-category");
                    var nodes = treeObj.getSelectedNodes();
                    if (nodes.length > 0) {
                        dataParam = {"KeyName": nodes[0].KeyName, "KeyCode": nodes[0].KeyCode}
                    }
                    iv.getInfo(dataParam);
                } else {
                    $.mdlg.error('错误', data.message);
                }
            }).fail(errors);
        });
    },
    deleteLeft: function () {
        $.mdlg.confirm("删除", "您确认要将所选择的业务参数吗？", function () {
            var treeObj = $.fn.zTree.getZTreeObj("tree-category");
            var nodes = treeObj.getSelectedNodes();
            var keyName = null;
            var keyCode = null;
            if (nodes.length > 0) {
                keyName = nodes[0].KeyName;
                keyCode = nodes[0].KeyCode;
            }
            var param = {};
            param['KeyName'] = keyName;
            param['KeyCode'] = keyCode;
            param['sign'] = "left";

            $.post(basePath + 'admin/bizParams/delete', JSON.stringify(param), function (data) {
                if (data.result) {
                    $.mdlg.alert('提示', data.message);
                    iv.bindData();
                    $("#grid").empty();
                } else {
                    $.mdlg.error('错误', data.message);
                }
            }).fail(errors);
        });
    }
}
