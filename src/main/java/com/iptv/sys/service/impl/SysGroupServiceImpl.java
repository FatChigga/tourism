package com.iptv.sys.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.iptv.core.common.BizException;
import com.iptv.core.service.impl.BaseServiceImpl;
import com.iptv.sys.service.SysGroupService;
import org.springframework.stereotype.Service;

@Service
public class SysGroupServiceImpl
        extends BaseServiceImpl
        implements SysGroupService {
    public List<Map> getALlGroupForNode() {
        List nodes = new ArrayList();
        List data = getDao().selectList("sysGroup.getRootNodes");
        getNodes(data);

        Map root = new HashMap();
        root.put("id", Integer.valueOf(0));
        root.put("name", "用户分组");
        root.put("open", Boolean.valueOf(true));
        root.put("children", data);
        nodes.add(root);

        return nodes;
    }

    private void getNodes(List<Map> list) {
        for (Object item : list) {
            Map node = (Map) item;
            Map map = new HashMap();
            map.put("parentId", node.get("id"));
            List children = getDao().selectList("sysGroup.getNodesByParentID", map);

            if (!children.isEmpty()) {
                node.put("children", children);
                getNodes(children);
            }
        }
    }

    public void save(Map map) throws BizException {
        List errMsg = new ArrayList();

        if (map.get("Code") == null) {
            errMsg.add("请输入分组编码。");
        }
        if (map.get("Name") == null) {
            errMsg.add("请输入分组名称。");
        }
        if (map.get("Level") == null) {
            errMsg.add("请输入分组层级。");
        }
        if ((map.get("Level") != null) && (!map.get("Level").toString().matches("^\\d+$"))) {
            errMsg.add("分组层级请输入纯数字。");
        }
        if (map.get("Enable") == null) {
            errMsg.add("请选择是否启用。");
        }
        if (errMsg.size() > 0) {
            throw new BizException(errMsg);
        }

        if (map.get("ParentId") != null) {
            Map parentCode = (Map) getDao().selectOne("sysGroup.getParentCode", map.get("ParentId"));
            map.put("ParentCode", parentCode.get("Code"));
        }

        if ((map.get("Id") == null) || (map.get("Id").equals(Integer.valueOf(0)))) {
            getDao().insert("sysGroup.saveGroup", map);
        } else {
            getDao().update("sysGroup.updateGroup", map);
        }
    }

    public void delete(Map map) throws BizException {
        List errMsg = new ArrayList();

        if (map.get("Id") == null) {
            errMsg.add("请选择要删除的分组。");
        }

        if (errMsg.size() > 0) {
            throw new BizException(errMsg);
        }

        List list = new ArrayList();
        List a = new ArrayList();
        map.put("id", map.get("Id"));
        a.add(map);
        list.add(map);
        getDeleteNodes(list, a);

        getDao().delete("sysGroupMenu.deleteGroupMenuList", a);
        getDao().delete("sysGroupMenuComponent.deleteGroupMenuComponentList", a);
        getDao().delete("sysGroup.deleteGroup", a);
    }

    private void getDeleteNodes(List<Map> list, List a) {
        for (Map item : list) {
            Map map = new HashMap();
            map.put("parentId", item.get("id"));
            List children = getDao().selectList("sysGroup.getDeleteNodesByParentID", map);

            if ((!children.isEmpty()) && (children.size() > 0)) {
                a.addAll(children);
                getDeleteNodes(children, a);
            }
        }
    }
}
