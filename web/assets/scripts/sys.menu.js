$(document).ready(function () {
    $('#level').kendoNumericTextBox({
        format: "#",
        decimals: 0,
        min: 1,
        max: 99999999,
        value: 1
    });

    var setting = {
        callback: {
            onClick: function (event, treeId, treeNode) {
                $('#form-menu').formData(treeNode);
                $('#name').val(treeNode.MenuName);

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

                if (treeNode.IsShortcut) {
                    $('#btnStyle').prop('disabled', '');
                } else {
                    $('#btnStyle').prop('disabled', 'disabled');
                }
            }
        }
    };

    var bindData = function () {
        $('#enable').prop('checked', true);
        $('#visiable').prop('checked', true);
        $.get(basePath + '/sys/menu/menuNodes', null, function (data) {
            zTreeObj = $.fn.zTree.init($("#tree-menu"), setting, data);
        })

        fit();
    };

    $('input[name="IsShortcut"]').change(function () {
        if ($(this).attr('id') === 'isShortcut') {
            $('#btnStyle').prop('disabled', '');
        } else {
            $('#btnStyle').prop('disabled', 'disabled');
        }
    });

    $('#btn-add').click(function () {
        var treeObj = $.fn.zTree.getZTreeObj("tree-menu"),
            nodes = treeObj.getSelectedNodes();

        if (nodes.length == 0) {
            $.mdlg.error('提示', '请选择父级菜单。')
            return;
        }

        $('#form-menu').clearForm();
        $('#menuId').val('');
        $('#parentId').val(nodes[0].Id);
        $('#enable').prop('checked', true);
        $('#visiable').prop('checked', true);
        $('#notShortcut').prop('checked', true);

        var level = (typeof nodes[0].Level == 'undefined') ? 1 : parseInt(nodes[0].Level) + 1;
        $('#level').data('kendoNumericTextBox').value(level);
    });

    $('#btn-save').click(function () {
        var params = $("#form-menu").serializeJson();

        $.post(basePath + 'sys/menu/save', JSON.stringify(params), function (data) {
            if (data.result == true) {
                $.mdlg.alert('提示', data.message);
                //$('#form-menu').clearForm();
                bindData();
            } else {
                $.mdlg.error('错误', data.message);
            }
        })
    })

    $('#btn-del').click(function () {
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
                    var params = {Id: nodes[0].Id};

                    $.post(basePath + 'sys/menu/delete', JSON.stringify(params), function (data) {
                        if (data.result == true) {
                            $.mdlg.alert('提示', data.message);
                            $('#form-menu').clearForm();
                            bindData();
                        } else {
                            $.mdlg.error('错误', data.message);
                        }
                    })
                }

                $(this).closeDialog(modal);
            }
        })
    })

    bindData();
})