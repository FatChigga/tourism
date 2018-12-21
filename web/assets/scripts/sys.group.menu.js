iv = {
    init: function () {
        this.bind();

        $('#btn-save').click(function () {
            iv.edit();
        })
    },
    bind: function () {
        var roleSetting = {
            view: {
                nameIsHTML: true,
                showTitle: false
            },
            callback: {
                onClick: function (event, treeId, treeNode) {
                    var treeObj = $.fn.zTree.getZTreeObj("tree-menu");
                    treeObj.checkAllNodes(false);
                    $('.js-chk').prop('checked', false);
                    $('.js-component-chk').prop('checked', false);

                    $.get(basePath + '/sys/groupMenu/menuList', {
                        'id': treeNode.id
                    }, function (data) {
                        for (var i = 0, l = data.length; i < l; i++) {
                            var node = treeObj.getNodeByParam("id", data[i].MenuId, null);
                            treeObj.checkNode(node, true, false);

                            if (typeof data[i].PermisionId !== 'undefined') {
                                if ((data[i].PermisionId).indexOf("[") < 0) {
                                    $('#' + node.tId + '_ul input[type="checkbox"][class="js-chk"][value="' + data[i].PermisionId + '"]').prop('checked', true);
                                }
                                if ((data[i].PermisionId).indexOf("[") >= 0) {
                                    $('#' + node.tId + '_ul input[type="checkbox"][class="js-component-chk"][value="'
                                        + data[i].PermisionId.substring(0, (data[i].PermisionId).indexOf("[")) + '"]').prop('checked', true);
                                }
                            }
                        }

                        if (data != null && data.length > 0) {
                            var firstNode = treeObj.getNodeByParam("id", 0, null);
                            treeObj.checkNode(firstNode, true, false);
                        }

                        var checkedNodes = treeObj.getCheckedNodes();
                        $("#btn-save").data("checkedNodes", checkedNodes);
                    })
                }
            }
        };
        var menuSetting = {
            view: {
                nameIsHTML: true,
                showTitle: false
            },
            check: {
                enable: true,
                chkStyle: "checkbox",
                chkboxType: {
                    "Y": "ps",
                    "N": "ps"
                }
            },
            callback: {
                onCheck: function (event, treeId, treeNode) {
                    $('#' + treeNode.tId + '_ul').find('input[type="checkbox"]').each(function () {
                        $(this).prop("checked", treeNode.checked);
                    })
                }
            }
        };

        $.get(basePath + '/sys/groupMenu/groupMenuNodes', null, function (data) {
            zTreeObj = $.fn.zTree.init($("#tree-group"), roleSetting, data[0]);
            zTreeObj = $.fn.zTree.init($("#tree-menu"), menuSetting, data[1]);
        })
    },
    edit: function () {
        var menuTree = $.fn.zTree.getZTreeObj("tree-menu");
        var groupTree = $.fn.zTree.getZTreeObj("tree-group");
        var nodes = groupTree.getSelectedNodes();

        if (nodes.length == 0 || nodes[0].id == 0) {
            $.mdlg.alert('提示', '请选分组');
            return;
        }

        var checkNodes = menuTree.getCheckedNodes();
        var checkedNodes = $("#btn-save").data("checkedNodes");

        var param = new Array();
        var params = {
            'param': param
        };

        if (typeof(nodes[0]) != 'undefined') {
            params['groupId'] = nodes[0].id;
        }

        if (checkNodes.length == 0) {
            param = null;
        } else {
            iv.getCheckedNodes(checkNodes[0], param);
        }

        $.post(basePath + 'sys/groupMenu/save', JSON.stringify(params), function (data) {
            if (data.result == true) {
                var treeObj = $.fn.zTree.getZTreeObj("tree-menu");
                var checkedNodes = treeObj.getCheckedNodes();
                $("#btn-save").data("checkedNodes", checkedNodes);
                $.mdlg.alert('提示', data.message);
            } else {
                $.mdlg.error('错误', data.message);
            }
        })
    },
    getCheckedNodes: function (currNode, checkedData) {
        var item = {};
        var nodes = currNode.children;

        if (typeof(nodes) != 'undefined' && nodes.length > 0) {
            $.each(nodes, function (i, childernNode) {
                var checked = childernNode.checked;
                if (checked == true) {
                    iv.getCheckedNodes(childernNode, checkedData);
                }
            })
        }

        if (currNode.id != 0) {
            item.id = currNode.id;
            if (currNode.isLast) {
                item.permision = iv.getPermision($('#' + currNode.tId + '_ul'));
            }
            checkedData.push(item);
        }
    },
    getPermision: function (obj) {
        var res = new Array();

        $(obj).find('input[type="checkbox"][class="js-chk"]:checked').each(function () {
            res.push(parseInt($(this).val()));
        })
        $(obj).find('input[type="checkbox"][class="js-component-chk"]:checked').each(function () {
            res.push(parseInt($(this).val()) + '[Component]');
        })
        return res;
    }
}