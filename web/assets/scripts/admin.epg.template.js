iv = {
    init: function () {
        this.bind();
        if (data != null) {
            this.bindData(data);
        }

        $('#btn-save').click(function () {
            var params = $("#form-template").serializeJson();
            $.post(basePath + 'admin/epgTemplate/save', JSON.stringify(params), function (data) {
                if (data.result == true) {
                    $.mdlg.alert('提示', data.message);
                    iv.bindRemoteData();
                } else {
                    $.mdlg.error('错误', data.message);
                }
            })
        })

        $('#btn-sync').click(function () {
            $.post(basePath + 'admin/epgTemplate/syncEpgTemplate', function (data) {
                if (data.result == true) {
                    $.mdlg.alert('提示', data.message);
                    iv.bindRemoteData();
                } else {
                    $.mdlg.error('错误', data.message);
                }
            })
        })
    },
    bindRemoteData: function () {
        $.post(basePath + 'admin/epgTemplate/getInfo', function (data) {
            iv.bindData(data);
        })
    },
    bind: function () {
        $('input[name="SuccessDate"],input[name="CommitDate"],input[name="CreateDate"]').kendoDateTimePicker({
            format: "yyyy-MM-dd HH:mm:ss"
        });
        iv.webUploaderInt();
    },
    bindData: function (data) {
        $('#form-template').formData(data);

        if (data.Code) $("#code").html(data.Code);
        if (data.CommitDate) $("#successDate").html(data.SuccessDate == null ? "" : new Date(data.SuccessDate).format("yyyy-MM-dd hh:mm:ss"));
        if (data.CommitDate) $("#commitDate").html(new Date(data.CommitDate).format("yyyy-MM-dd hh:mm:ss"));
        if (data.CreateDate) $("#createDate").html(new Date(data.CreateDate).format("yyyy-MM-dd hh:mm:ss"));
        if (data.IsSyncSuccess) $("#isSyncSuccess").html(data.IsSyncSuccess);
        if (data.IsSync) $("#isSync").html(data.IsSync);
        if (data.OperatingStatus) $("#operatingStatus").html(data.OperatingStatus);

        if (data.FileUrl) {
            $('#filePath').val(data.FileUrl);

            $('#thelist').html('<div id="WU_FILE_1" class="item"><h4 class="info">'
                + data.FileUrl.split("/")[data.FileUrl.split("/").length - 1]
                + '</h4><p class="state">已上传</p></div>')
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

        $("#picker").click(function () {
        })

        uploader.on('fileQueued', function (file) {
            $list.html('');
            $list.append('<div id="' + file.id + '" class="item">' +
                '<h4 class="info">' + file.name + '</h4>' +
                '<p class="state">等待上传...</p>' +
                '</div>');

            uploader.upload();
        })

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
            $('#filePath').val(response.path);
            $('#' + file.id).find('p.state').text('已上传');
        })

        uploader.on('uploadError', function (file) {
            $('#' + file.id).find('p.state').text('上传出错');
        });

        uploader.on('uploadComplete', function (file) {
            $('#' + file.id).find('.progress').fadeOut();
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
    }
}