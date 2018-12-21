iv = {
    init: function () {
        this.bindMessages();
    },
    audit: function (piid, tiid, tid, pid, processName, nodeType) {
        var $this = this;

        $.mdlg({
            title: processName,
            width: '850px',
            content: function () {
                return $('#data-tmpl').html();
            },
            iframe: basePath + 'wf/audit/index?piid=' + piid + '&tiid=' + tiid + '&tid=' + tid + '&nodeType=' + nodeType,
            showCloseButton: false,
            buttons: ["通过", "退回", "拒绝", "取消"],
            buttonStyles: ['btn-success', 'btn-warning', 'btn-danger', 'btn-default'],
            onButtonClick: function (sender, modal, index) {
                var self = this;

                if (index == 0) {
                    modal.childPage.iv.agree($.mdlg, piid, tiid, tid, nodeType, modal, $this);
                } else if (index == 1) {
                    modal.childPage.iv.sendBack($.mdlg, piid, tiid, modal, $this);
                } else if (index == 2) {
                    modal.childPage.iv.reject($.mdlg, piid, tiid, pid, modal, $this);
                }
                else {
                    $(this).closeDialog(modal);
                }
            }
        });
    },
    bindMessages: function () {
        Date.prototype.Format = function () {
            var o = {
                "Y": this.getFullYear(),
                "M": supply(this.getMonth() + 1),
                "d": supply(this.getDate()),
                "h": supply(this.getHours()),
                "m": supply(this.getMinutes()),
                "s": supply(this.getSeconds())
            }

            return o.Y + "-" + o.M + "-" + o.d + " " + o.h + ":" + o.m + ":" + o.s;
        }

        function supply(time) {
            time = time.toString().length > 1 ? time : "0" + time;
            return time;
        }

        $.post(basePath + 'admin/message/homeMessage', JSON.stringify({
            take: 20,
            skip: 0,
            page: 1,
            pageSize: 20
        }), function (result) {
            $.each(result.data, function (i, v) {
                $('#grid-message').append('<tr class="message-tr"><td class="message-left-td">' + v.Title + '</td><td class="message-right-td">' + new Date(v.CreateDate).Format() + '</td></tr>')
            })
        })
    }
}