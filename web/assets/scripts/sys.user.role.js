iv = {
    init: function () {
        this.bind();
        $('#btn-save').click(function () {
            iv.edit();
        })
    },
    bind: function () {
        var userSetting = {
            view: {
                nameIsHTML: true
            },
            callback: {
                onClick: function (event, treeId, treeNode) {
                    var treeObj = $.fn.zTree.getZTreeObj("tree-role");
                    treeObj.checkAllNodes(false);

                    $.get(basePath + '/sys/userRole/roleList', {
                        'id': treeNode.id
                    }, function (data) {
                        for (var i = 0, l = data.length; i < l; i++) {
                            var node = treeObj.getNodeByParam("id", data[i].RoleId, null);
                            treeObj.checkNode(node, true, true);
                        }

                        var checkedNodes = treeObj.getCheckedNodes();
                        $("#btn-save").data("checkedNodes", checkedNodes);
                    })
                }
            }
        };
        var roleSetting = {
            check: {
                enable: true,
                chkStyle: "checkbox",
                chkboxType: {
                    "Y": "ps",
                    "N": "ps"
                }
            }
        };

        $.get(basePath + '/sys/userRole/userRoleNodes', null, function (data) {
            zTreeObj = $.fn.zTree.init($("#tree-role"), roleSetting, data[0]);
            zTreeObj = $.fn.zTree.init($("#tree-user"), userSetting, data[1]);
        })
    },
    edit: function () {
        var userTree = $.fn.zTree.getZTreeObj("tree-user");
        var roleTree = $.fn.zTree.getZTreeObj("tree-role");
        var nodes = userTree.getSelectedNodes();

        if (nodes.length == 0 || nodes[0].id == 0) {
            $.mdlg.alert('提示', '请选用户');
            return;
        }

        var checkNodes = roleTree.getCheckedNodes();
        var checkedNodes = $("#btn-save").data("checkedNodes");

        var b = iv.compareNodes(checkNodes, checkedNodes);

        if (checkedNodes != null && checkedNodes.length > 0 && b) {
            $.mdlg.alert('提示', "保存成功");
            return;
        }

        var param = new Array();
        var params = {
            'param': param
        };

        if (typeof(nodes[0]) != 'undefined') {
            params['userId'] = nodes[0].id;
        }

        if (checkNodes.length == 0) {
            param = null;
        } else {
            iv.getCheckedNodes(checkNodes[0], param);
        }

        $.post(basePath + 'sys/userRole/save', JSON.stringify(params), function (data) {
            if (data.result == true) {
                $.mdlg.alert('提示', data.message);

                var treeObj = $.fn.zTree.getZTreeObj("tree-role");
                var checkedNodes = treeObj.getCheckedNodes();
                $("#btn-save").data("checkedNodes", checkedNodes);
            } else {
                $.mdlg.error('错误', data.message);
            }
        })
    },
    getCheckedNodes: function (currNode, checkedData) {
        if (currNode.id != 0) {
            checkedData.push(currNode.id);
        }
        var nodes = currNode.children;
        if (typeof(nodes) != 'undefined' && nodes.length > 0) {
            $.each(nodes, function (i, childernNode) {
                var checked = childernNode.checked;
                if (checked == true) {
                    iv.getCheckedNodes(childernNode, checkedData);
                }
            })
        }
    },
    compareNodes: function (currCheckedNodes, checkedNodesOld) {
        var b = true;
        if (currCheckedNodes.length == checkedNodesOld.length) {
            $.each(currCheckedNodes, function (i, v) {
                if (v.name != checkedNodesOld[i].name) {
                    b = false;
                }
            })
        } else {
            b = false;
        }
        return b;
    }
}