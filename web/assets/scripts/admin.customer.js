iv = {
    init: function () {
        this.bind();

        $('#btn-add').click(function () {
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
                $.mdlg.alert('提示', "请选择要同步的客户")
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
                    title: "编号",
                    width: "160px",
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
                    title: "客户名称",
                    width: "150px",
                    field: "Name",
                    encoded: false,
                    attributes: {
                        'class': 'center'
                    }
                },
                {
                    title: "模板",
                    width: "100px",
                    field: "TemplateId",
                    encoded: false,
                    attributes: {
                        'class': 'center'
                    },
                    values: template
                },
                {
                    title: "省",
                    width: "100px",
                    field: "ProvinceId",
                    encoded: false,
                    attributes: {
                        'class': 'center'
                    },
                    values: province
                },
                {
                    title: "市",
                    width: "100px",
                    field: "CityId",
                    encoded: false,
                    attributes: {
                        'class': 'center'
                    },
                    values: province
                },
                {
                    title: "区/县",
                    width: "100px",
                    field: "AreaId",
                    encoded: false,
                    attributes: {
                        'class': 'center'
                    },
                    values: province
                },
                {
                    title: "创建时间",
                    width: "150px",
                    field: "CreateDate",
                    encoded: false,
                    attributes: {
                        'class': 'center'
                    },
                    format: '{0:yyyy-MM-dd HH:mm:ss}'
                },
                {
                    title: "创建者",
                    width: "100px",
                    field: "CreateUserId",
                    encoded: false,
                    attributes: {
                        'class': 'center'
                    },
                    values: users
                },
                {
                    title: "Logo",
                    width: "150px",
                    field: "Logo",
                    encoded: false,
                    filterable: false,
                    template: "# if (typeof(Logo)=='undefined' || Logo == null) { #" +
                    "<span></span>" +
                    "# }else{ #" +
                    "<a class='js-img' href='" + staticUrl + "#=Logo#'>#=Logo#</a> # } #",
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
            dataSource: createDataSource(basePath + 'admin/customer/customerList', {
                CreateDate: {
                    type: "date", parse: function (value) {
                        return new Date(value);
                    }
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

        this.getInfo(filter);
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
    bindDropDownList: function () {
        $('#txt-province').kendoDropDownList({
            dataTextField: "text",
            dataValueField: "value",
            optionLabel: '==请选择省==',
            dataSource: createDataSource(basePath + 'region/getRegions'),
            change: function () {
                var urlCity = basePath + 'region/getRegions?parentId=' + $('#txt-province').data('kendoDropDownList').value();

                $('#txt-city').kendoDropDownList({
                    dataTextField: "text",
                    dataValueField: "value",
                    optionLabel: '==请选择市==',
                    dataSource: createDataSource(urlCity),
                    change: function () {
                        var urlArea = basePath + 'region/getRegions?parentId=' + $('#txt-city').data('kendoDropDownList').value();

                        $('#txt-area').kendoDropDownList({
                            dataTextField: "text",
                            dataValueField: "value",
                            optionLabel: '==请选择区／县==',
                            dataSource: createDataSource(urlArea),
                        });
                    }
                });
                $('#txt-area').data("kendoDropDownList").setDataSource(null);
            }
        });
        $('input[name="TemplateId"]').kendoDropDownList({
            dataTextField: "text",
            dataValueField: "value",
            optionLabel: '==请选择模板==',
            filter: 'contains',
            dataSource: template
        });
    },
    edit: function (id) {
        $.mdlg({
            title: '编辑客户信息',
            width: '950px',
            content: function () {
                return $('#data-tmpl').html();
            },
            onShow: function () {
                iv.bindUpload();
                iv.bindDropDownList();
                $('#txt-city,#txt-area').kendoDropDownList({});

                if (id) {
                    $.get(basePath + 'admin/customer/getCustomer', {Id: id}, function (data) {
                        $('#form-process').formData(data);
                        iv.bindDropDownList();
                        var province = $('#txt-province').data("kendoDropDownList");
                        province.value(data.ProvinceId);
                        province.trigger("change");

                        var city = $('#txt-city').data("kendoDropDownList");
                        city.value(data.CityId);
                        city.trigger("change");

                        $('#txt-area').data("kendoDropDownList").value(data.AreaId);

                        $('#img-icon').attr('src', staticUrl + data.Logo);
                        $('#a-img').removeClass('hidden').attr('href', staticUrl + data.Logo);
                        $('#icon-url').val(data.Logo);

                        $("#a-img").fancybox({
                            'transitionIn': 'none',
                            'transitionOut': 'none'
                        });
                    })
                }
            },
            showCloseButton: false,
            buttons: ["保存", "取消"],
            buttonStyles: ['btn-success', 'btn-default'],
            onButtonClick: function (sender, modal, index) {
                var self = this;
                var params = $("#form-process").serializeJson();
                if (index == 0) {
                    var params = $("#form-process").serializeJson();
                    params['total'] = $("#num").val();
                    $.post(basePath + 'admin/customer/save', JSON.stringify(params), function (data) {
                        if (data.result == true) {
                            $.mdlg.alert('提示', data.message);
                            $('#form-process').clearForm();
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
        });
    },
    delete: function () {
        $.mdlg.confirm("删除", "您确认要将所选择的信息删除么？", function () {
            var params = $("#grid").gridSelectedCols('Id');

            $.post(basePath + 'admin/customer/delete', JSON.stringify(params), function (data) {
                if (data.result) {
                    $.mdlg.alert('提示', data.message);
                    iv.getInfo();
                } else {
                    $.mdlg.error('错误', data.message);
                }
            });
        });
    },
    sync: function (id) {
        iv.syncModal("同步中...");
        $.get(basePath + 'admin/sync/syncSingleCustomer', {"id": id}, function (data) {
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