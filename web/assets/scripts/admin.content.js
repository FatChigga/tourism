iv = {
    init: function () {
        this.bindTemplete();
        this.bind();

        document.getElementById('menu').addEventListener('click', function (e) {
            iv.edit({
                type: e.target.getAttribute('Id').split('-')[1]
            });
        }, false)

        $('#btn-edit').click(function () {
            var treeObj = $.fn.zTree.getZTreeObj("tree-content");
            var nodes = treeObj.getSelectedNodes();
            if (typeof(nodes[0]) != "undefined") {

                if (nodes[0].id != '0') {
                    var param = $("#grid").gridSelectedCols("Id");
                    if (param.Id.length > 0) {
                        iv.edit({
                            id: param.Id[0]
                        });
                    }
                }
            }
        })

        $('#btn-del').click(function () {
            iv.delete();
        });

        $('#btn-detail').click(function () {
            var param = $("#grid").gridSelectedCols("Id");
            if (param.Id.length > 0) iv.workOrderDetail(param.Id[0]);
        });

        $('#btn-sync').click(function () {
            var param = $("#grid").gridSelectedCols("Id");
            if (param.Id.length > 0) {
                iv.sync(param.Id[0])
            } else {
                $.mdlg.alert('提示', "请选择要同步的内容")
            }
            ;
        })

        $('#btn-shelve-content').click(function () {
            var param = $("#grid").gridSelectedCols("Id");
            if (param.Id.length > 0) {
                iv.shelveContent(param.Id[0])
            } else {
                $.mdlg.alert('提示', "请选择要上架的内容")
            }
            ;
        })

        $('#btn-pull-off').click(function () {
            var param = $("#grid").gridSelectedCols("Id");
            if (param.Id.length > 0) {
                iv.pulloff(param.Id[0])
            } else {
                $.mdlg.alert('提示', "请选择要下架的内容")
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
                    if (typeof(treeNode.name) != "undefined") {
                        if (treeNode.id == '0' || treeNode.getParentNode().id == '0') {
                            var filter = {field: "CategoryId", operator: "eq", value: 0};
                            iv.getInfo(filter);
                        } else {
                            var filter = [{field: "CategoryId", operator: "eq", value: treeNode.id}, {
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

        $.get(basePath + 'admin/customer/customerCategoryNodes', null, function (data) {
            zTreeObj = $.fn.zTree.init($("#tree-content"), setting, data);
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
                    title: "类型",
                    width: "150px",
                    field: "ContentType",
                    encoded: false,
                    attributes: {
                        'class': 'center'
                    },
                    values: contentType
                },
                {
                    title: "标题",
                    width: "250px",
                    field: "Title",
                    encoded: false,
                    attributes: {
                        'class': 'center'
                    }
                },
                {
                    title: "编号",
                    width: "250px",
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
                    title: "开始时间",
                    width: "150px",
                    field: "StartDate",
                    encoded: false,
                    attributes: {
                        'class': 'center'
                    },
                    format: '{0:yyyy-MM-dd HH:mm:ss}'
                },
                {
                    title: "结束时间",
                    width: "150px",
                    field: "EndDate",
                    encoded: false,
                    attributes: {
                        'class': 'center'
                    },
                    format: '{0:yyyy-MM-dd HH:mm:ss}'
                },
                {
                    title: "封面图",
                    width: "150px",
                    field: "PosterUrl",
                    encoded: false,
                    attributes: {
                        'class': 'center'
                    },
                    template: "# if (typeof(PosterUrl)=='undefined' || PosterUrl == null) { #" +
                    "<span></span>" +
                    "# }else{ #" +
                    "<a class='js-img' href='" + staticUrl + "#=PosterUrl#'>#=PosterUrl#</a> # } #",
                    attributes: {
                        'class': 'center'
                    }
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
            dataSource: createDataSource(basePath + 'admin/content/contentList', {
                StartDate: {
                    type: "date", parse: function (value) {
                        return new Date(value);
                    }
                },
                EndDate: {
                    type: "date", parse: function (value) {
                        return new Date(value);
                    }
                },
                CreateDate: {
                    type: "date", parse: function (value) {
                        return new Date(value);
                    }
                },
                CommitDate: {
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

        var filter = {field: "CategoryId", operator: "eq", value: 0};

        iv.getInfo(filter);
    },
    getInfo: function (data) {
        var grid = $('#grid').data('kendoGrid');
        grid.dataSource.filter(data);
    },
    bindUpload: function () {
        var html = "";
        var iconUrl = "";
        $("#file-upload").uploadify({
            height: 34,
            swf: basePath + '/assets/scripts/uploadify.swf',
            uploader: basePath + '/admin/file/upload',
            width: 120,
            multi: true,
            buttonText: '选择图片',
            fileSizeLimit: 500,
            fileTypeDesc: '支持格式:jpg/gif/jpeg/png/bmp.',
            fileTypeExts: '*.jpg;*.gif;*.jpeg;*.png;*.bmp',
            onUploadSuccess: function (file, data, response) {
                data = $.parseJSON(data);

                if (data.result) {
                    html += '<a class="a-img" href="' + data.url + '" style="margin-right:5px;">' +
                        '<img width="120" height="90" src="' + data.url + '" id="img-icon" style="border:1px solid #ccc">' +
                        '</a>';

                    iconUrl = iconUrl != '' ? iconUrl + '|' : '';
                    iconUrl += data.path;
                    ;
                } else {
                    $.mdlg.error('错误', data.message);
                }
            },
            onQueueComplete: function (queueData) {
                $("#surface").html(html);
                $("#icon-url").val(iconUrl);
                $("#surface .a-img").each(function (i, v) {
                    $(v).fancybox({
                        'transitionIn': 'none',
                        'transitionOut': 'none'
                    });
                })
                html = "";
                iconUrl = "";
            }
        });
    },
    bindFileImgUpload: function () {
        var html = "";
        var iconUrl = "";
        $("#image-upload").uploadify({
            height: 34,
            swf: basePath + '/assets/scripts/uploadify.swf',
            uploader: basePath + '/admin/file/upload',
            width: 120,
            multi: true,
            buttonText: '选择图片',
            fileSizeLimit: 500,
            fileTypeDesc: '支持格式:jpg/gif/jpeg/png/bmp.',
            fileTypeExts: '*.jpg;*.gif;*.jpeg;*.png;*.bmp',
            onUploadSuccess: function (file, data, response) {
                data = $.parseJSON(data);

                if (data.result) {
                    html += '<a class="b-img" href="' + data.url + '" style="margin-right:5px;">' +
                        '<img width="120" height="90" src="' + data.url + '" id="img-icon" style="border:1px solid #ccc">' +
                        '</a>';

                    iconUrl = iconUrl != '' ? iconUrl + '|' : '';
                    iconUrl += data.path;
                } else {
                    $.mdlg.error('错误', data.message);
                }
            },
            onQueueComplete: function (queueData) {
                $("#picture").html(html);
                $("#filePath").val(iconUrl);
                $("#picture .a-img").each(function (i, v) {
                    $(v).fancybox({
                        'transitionIn': 'none',
                        'transitionOut': 'none'
                    });
                })
                html = "";
                iconUrl = "";
            }
        });
    },
    edit: function (param) {
        var id;
        var type;
        var pageId;

        if (param['id']) {
            id = param['id']
        }
        if (param['type']) {
            type = param['type']
        }

        var treeObj = $.fn.zTree.getZTreeObj("tree-content");
        var nodes = treeObj.getSelectedNodes();

        if (nodes.length <= 0 || nodes[nodes.length - 1].getParentNode().id == '0' || $("#grid").html() == '') {
            $.mdlg.error('提示', "请选择末级节点");
            return;
        } else {
            pageId = nodes[nodes.length - 1].id;
        }

        $.mdlg({
            title: '内容',
            width: '850px',
            content: function () {
                return $('#data-tmpl').html();
            },
            onShow: function () {
                iv.bindUpload();

                $('input[name="StartDate"],input[name="EndDate"]').kendoDateTimePicker({
                    format: "yyyy-MM-dd HH:mm:ss"
                });

            },
            onShown: function () {
                if (type) {
                    iv.chooseContentType(type);
                }

                if (id) {
                    $.post(basePath + 'admin/content/getContent', JSON.stringify({Id: id}), function (data) {

                        iv.chooseContentType(data.ContentType.toString())

                        $('#form-template').formData(data);

                        if (typeof data.PosterUrl != 'undefined') {
                            var html = '';
                            $.each(data.PosterUrl.split("|"), function (i, v) {
                                html += '<a class="a-img" href="' + staticUrl + v + '" style="margin-right:5px;">' +
                                    '<img width="120" height="90" src="' + staticUrl + v + '" id="img-icon" style="border:1px solid #ccc">' +
                                    '</a>';

                                $("#surface").html(html);
                            })

                            $("#surface .a-img").each(function (i, v) {
                                $(v).fancybox({
                                    'transitionIn': 'none',
                                    'transitionOut': 'none'
                                });
                            })

                            $('#icon-url').val(data.PosterUrl);
                        }

                        $('input[name="StartDate"]').kendoDateTimePicker({
                            format: "yyyy-MM-dd HH:mm:ss",
                            value: new Date(data.StartDate)
                        });
                        $('input[name="EndDate"]').kendoDateTimePicker({
                            format: "yyyy-MM-dd HH:mm:ss",
                            value: new Date(data.EndDate)
                        });

                        if (data.ContentType == '2') {
                            var html = '';
                            $.each(data.FileUrl.split("|"), function (i, v) {
                                html += '<a class="b-img" href="' + staticUrl + v + '" style="margin-right:5px;">' +
                                    '<img width="120" height="90" src="' + staticUrl + v + '" id="img-icon" style="border:1px solid #ccc">' +
                                    '</a>';

                                $("#picture").html(html);
                            })

                            $("#picture .b-img").each(function (i, v) {
                                $(v).fancybox({
                                    'transitionIn': 'none',
                                    'transitionOut': 'none'
                                });
                            })

                            $('#filePath').val(data.FileUrl);
                        } else if (data.ContentType == '3') {
                            $('#filePath').val(data.FileUrl);

                            $.each(data.FileUrl.split("|"), function (i, v) {
                                $('#thelist').append('<div id="WU_FILE_1" class="item"><h4 class="info">' + v.split("/")[v.split("/").length - 1] + '</h4><p class="state">已上传</p></div>')
                            })
                        }

                        type = data.ContentType;
                    });
                }
            },
            showCloseButton: false,
            buttons: ["保存", "取消"],
            buttonStyles: ['btn-success', 'btn-default'],
            onButtonClick: function (sender, modal, index) {
                var self = this;
                if (index == 0) {
                    var params = $("#form-template").serializeJson();
                    switch (type) {
                        case 'article':
                            params['ContentType'] = '1';
                            break;
                        case 'img':
                            params['ContentType'] = '2';
                            break;
                        case 'video':
                            params['ContentType'] = '3';
                            break;
                        default:
                            params['ContentType'] = type;
                            break;
                    }
                    params['CategoryId'] = pageId;
                    $.post(basePath + 'admin/content/save', JSON.stringify(params), function (data) {
                        if (data.result == true) {
                            $.mdlg.alert('提示', data.message);
                            var filter = {field: "CategoryId", operator: "eq", value: pageId};
                            iv.getInfo(filter);
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

        $.mdlg.confirm("删除", "您确认要将所选择的业务参数吗？", function () {
            $.post(basePath + 'admin/content/delete', JSON.stringify(param), function (data) {
                if (data.result) {
                    $.mdlg.alert('提示', data.message);

                    var treeObj = $.fn.zTree.getZTreeObj("tree-content");
                    var nodes = treeObj.getSelectedNodes();
                    if (nodes.length > 0 && nodes[0].id != 0) {
                        $("#" + nodes[0].tId).find('a').trigger('click');
                    }
                } else {
                    $.mdlg.error('错误', data.message);
                }
            }).fail(errors);
        });
    },
    chooseContentType(type){
        if (type == '1') type = 'article';
        if (type == '2') type = 'img';
        if (type == '3') type = 'video';

        switch (type) {
            case 'article':
                $('.form-group:last').after('<div class="form-group"><label class="col-sm-2 control-label">内容</label>' +
                    '<div class="col-sm-9">' +
                    '<textarea id="editor_id" name="Content" style="width:100%;height:200px;" maxlength="700" class="form-control">' +
                    '</textarea>' +
                    '</div>' +
                    '<span class="cols-sm-1 required">*</span></div>');
                break;
            case 'img':
                $('.form-group:last').after('<div class="form-group">' +
                    '<label for="ordrNum" class="col-sm-2 control-label">图片</label>' +
                    '<div class="col-sm-9">' +
                    '<div id="image-upload"></div>' +
                    '<div id="picture"><a class="hidden b-img" href="" style="margin-right:5px;">' +
                    '<img width="120" height="90" src="" id="img-icon" style="border:1px solid #ccc">' +
                    '</a></div>' +
                    '</div>' +
                    '<span class="cols-sm-1 required">*</span>' +
                    '</div>');
                iv.bindFileImgUpload();
                break;
            case 'video':
                $('.form-group:last').after('<div class="form-group"><label class="col-sm-2 control-label">内容</label>' +
                    '<div class="col-sm-9">' +
                    '<textarea id="editor_id" name="Content" style="width:100%;height:200px;" maxlength="700" class="form-control">' +
                    '</textarea>' +
                    '</div>' +
                    '<span class="cols-sm-1 required">*</span></div>' +
                    '<div class="form-group">' +
                    '<label class="col-sm-2 control-label">视频</label>' +
                    '<div class="col-sm-9">' +
                    '<div id="uploader" class="wu-example">' +
                    '<div id="thelist" class="uploader-list"></div>' +
                    '<div class="btns">' +
                    '<div id="picker">选择文件</div>' +
                    '</div>' +
                    '</div>' +
                    '</div>' +
                    '</div>' +
                    '<div class="form-group" id="ups" style="display:none;"><label class="col-sm-2 control-label">上传速度</label>' +
                    '<div class="col-sm-9"><div class="uploadSpeed">' +
                    '<div class="speed"><i class="fa fa-arrow-up text-success"></i><span>0kb/s</span></div>' +
                    '</div></div>' +
                    '</div></div>');
                iv.webUploaderInt();
                break;
        }
    },
    webUploaderInt: function () {
        var $ = jQuery,
            $list = $('#thelist'),
            $btn = $('#ctlBtn'),
            state = 'pending',
            chunkSize = parseInt(chunkSizeM * 1024 * 1024),
            uploader;

        var uploader = WebUploader.create({
            swf: '${basePath}assets/content/Uploader.swf',
            server: basePath + 'front/file/upload',
            pick: '#picker',
            resize: false,
            threads: 1,
            chunked: true,
            chunkSize: chunkSize
        })

        uploader.on('fileQueued', function (file) {
            $('#filePath').val('');
            $list.html('');
            $list.html('<div id="' + file.id + '" class="item">' +
                '<h4 class="info">' + file.name + '</h4>' +
                '<p class="state">等待上传...</p>' +
                '</div>');

            uploader.upload();
        })
        uploader.on('uploadStart', function (file) {
            var date = new Date();
            var getMonth = (date.getMonth() + 1);
            if (getMonth < 10) {
                getMonth = "0" + getMonth;
            }
            var getDate = date.getDate();
            if (getDate < 10) {
                getDate = "0" + getDate;
            }
            var getHours = date.getHours();
            if (getHours < 10) {
                getHours = "0" + getHours;
            }
            var getMinutes = date.getMinutes();
            if (getMinutes < 10) {
                getMinutes = "0" + getMinutes;
            }
            var getSeconds = date.getSeconds();
            if (getSeconds < 10) {
                getSeconds = "0" + getSeconds;
            }
            var getMilliseconds = date.getMilliseconds();
            if (getMilliseconds < 10) {
                getMilliseconds = "0" + getMilliseconds;
            }
            var time = date.getFullYear() + "" + getMonth + "" + getDate + "" + getHours + "" + getMinutes + "" + getSeconds + "" + getMilliseconds + "";
            file.fileName = file.name.substring(0, file.name.lastIndexOf('.')) + '_' + time;
            file.name = file.fileName + "." + file.ext;
        });
        uploader.on('uploadProgress', function (file, percentage) {
            var $li = $('#' + file.id),
                $percent = $li.find('.progress .progress-bar');
            if (!$percent.length) {
                $percent = $('<div class="progress progress-striped active">' +
                    '<div class="progress-bar" role="progressbar" style="width: 0%">' +
                    '</div>' +
                    '</div>').appendTo($li).find('.progress-bar');
            }

            $li.find('p.state').text('上传中');

            $percent.css('width', percentage * 100 + '%');
        })
        uploader.on('uploadSuccess', function (file, response) {
            var path = $('#filePath').val() == '' ? '' : $('#filePath').val() + "|"
            $('#filePath').val(path + response.path);
            $('#' + file.id).find('p.state').text('已上传');
            $("#ups").css("display", "none");
        });

        uploader.on('uploadError', function (file) {
            $('#' + file.id).find('p.state').text('上传出错');
            $("#ups").css("display", "none");
        });

        uploader.on('uploadComplete', function (file) {
            $('#' + file.id).find('.progress').fadeOut();
            $("#ups").css("display", "none");
        })

        uploader.on('all', function (type) {
            if (type === 'startUpload') {
                state = 'uploading';
            } else if (type === 'stopUpload') {
                state = 'paused';
            } else if (type === 'uploadFinished') {
                state = 'done';
            }

            if (state === 'uploading') {
                $btn.text('暂停上传');
            } else {
                $btn.text('开始上传');
            }
        });
    },
    workOrderDetail: function (id) {//工单详情
        $.mdlg({
            title: '工单详情',
            width: '850px',
            content: function () {
                return $('#data-Detail').html();
            },
            onShow: function () {
                if (id) {
                    $.get(basePath + 'admin/content/getWorkOrderDetail', {Id: id}, function (data) {
                        $('#operating-status').html(data.OperatingStatus);
                        $('#txt-xmlfor-add').html(iv.htmlEncode(data.XmlForAdd));
                        $('#txt-xmlfor-update').html(iv.htmlEncode(data.XmlForUpdate));
                        $('#txt-xmlfor-del').html(iv.htmlEncode(data.XmlForDel));
                        $('#txt-xmlfor-url').html(data.XmlUrl);
                    });
                }
            },
            showCloseButton: false,
        });
    },
    htmlEncode: function (str) {
        var s = "";
        if (str.length == 0) return "";
        s = str.replace(/&/g, "&gt;");
        s = s.replace(/</g, "&lt;");
        s = s.replace(/>/g, "&gt;");
        s = s.replace(/ /g, "&nbsp;");
        s = s.replace(/\'/g, "&#39;");
        s = s.replace(/\"/g, "&quot;");
        s = s.replace(/\n/g, "<br>");
        return s;
    },
    shelveContent: function (id) {
        iv.syncModal("上架中...");
        $.get(basePath + 'admin/sync/contentShelveSingle', {"id": id}, function (data) {
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
    sync: function (id) {
        iv.syncModal("同步中...");
        $.get(basePath + 'admin/sync/syncSingleContent', {"id": id}, function (data) {
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
        $.get(basePath + 'admin/sync/singlePullOff', {"id": id, "table": "content"}, function (data) {
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
