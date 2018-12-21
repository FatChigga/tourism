iv = {
    init: function () {
        this.bind();

        $("#btn-add").click(function () {
            iv.add();
        })
        $('#btn-save').click(function () {
            iv.edit();
        })
        $('#btn-del').click(function () {
            iv.delete();
        })
    },
    bind: function () {
        $('#level').kendoNumericTextBox({
            format: "#",
            decimals: 0,
            min: 1,
            max: 99999999,
            value: 1
        });

        var setting = {
            view: {
                nameIsHTML: true,
                showTitle: false
            },
            callback: {
                onClick: function (event, treeId, treeNode) {
                    $('#form-menu').formData(treeNode);
                    $('#name').val(treeNode.OrganizationName);

                    if (typeof(treeNode.ParentId) == 'undefined' || treeNode.ParentId == 0) {
                        $('#parentId').val('');
                    }

                    $('#level').kendoNumericTextBox({
                        format: "#",
                        decimals: 0,
                        min: 1,
                        max: 99999999,
                        value: treeNode.Level
                    });
                }
            }
        };
        $('#enable').prop('checked', true);
        $.get(basePath + '/sys/organization/organizationNodes', null, function (data) {
            zTreeObj = $.fn.zTree.init($("#tree-menu"), setting, data);
        })
    },
    add: function () {
        var treeObj = $.fn.zTree.getZTreeObj("tree-menu"),
            nodes = treeObj.getSelectedNodes();

        if (nodes.length == 0) {
            $.mdlg.error('提示', '请选择上级机构。');
            return;
        }

        $('#form-menu').clearForm();
        $('#id').val('');
        $('#parentId').val(nodes[0].Id);
        $('#enable').prop('checked', true);

        var level = (typeof nodes[0].Level == 'undefined') ? 1 : parseInt(nodes[0].Level) + 1;
        $('#level').data('kendoNumericTextBox').value(level);
    },
    edit: function () {
        var params = $("#form-menu").serializeJson();
        $.post(basePath + 'sys/organization/save', JSON.stringify(params), function (data) {
            if (data.result == true) {
                $.mdlg.alert('提示', data.message);
                $('#form-menu').clearForm();
                iv.bind();
            } else {
                $.mdlg.error('错误', data.message);
            }
        })
    },
    delete: function () {
        var treeObj = $.fn.zTree.getZTreeObj("tree-menu"),
            nodes = treeObj.getSelectedNodes();

        if (nodes.length == 0) {
            $.mdlg.error('提示', '请选择要删除的记录。')
            return;
        }

        $.mdlg({
            title: '提示',
            content: '确认删除本条记录?',
            showCloseButton: false,
            buttons: ["确定", "取消"],
            buttonStyles: ['btn-success', 'btn-default'],
            onButtonClick: function (sender, modal, index) {
                if (index == 0) {
                    var params = {
                        Id: nodes[0].Id
                    };

                    $.post(basePath + 'sys/organization/delete', JSON.stringify(params), function (data) {
                        if (data.result == true) {
                            $.mdlg.alert('提示', data.message);
                            $('#form-menu').clearForm();
                            iv.bind();
                        } else {
                            $.mdlg.error('错误', data.message);
                        }
                    })
                }

                $(this).closeDialog(modal);
            }
        })
    }
}