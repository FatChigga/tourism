package com.iptv.sys.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.iptv.core.common.BizException;
import com.iptv.core.service.impl.BaseServiceImpl;
import com.iptv.sys.service.SysOrganizationMenuService;
import org.springframework.stereotype.Service;

@Service
public class SysOrganizationMenuServiceImpl
        extends BaseServiceImpl
        implements SysOrganizationMenuService {
    public List<Map> getAllOrganizationMenuForNode() {
        List nodes = new ArrayList();
        List roleData = getDao().selectList("sysOrgMenu.getOrganizationNodes");
        List menuData = getDao().selectList("sysOrgMenu.getMenuRootNodes");
        getNodes(menuData);

        Map roleRoot = new HashMap();
        roleRoot.put("id", Integer.valueOf(0));
        roleRoot.put("name", "机构列表");
        roleRoot.put("open", Boolean.valueOf(true));
        roleRoot.put("children", roleData);
        nodes.add(roleRoot);

        Map menuRoot = new HashMap();
        menuRoot.put("id", Integer.valueOf(0));
        menuRoot.put("name", "机构权限");
        menuRoot.put("open", Boolean.valueOf(true));
        menuRoot.put("children", menuData);
        nodes.add(menuRoot);

        return nodes;
    }

    public void getNodes(List<Map> list) {
        for (Object obj : list) {
            Map map = (Map) obj;
            Map data = new HashMap();
            data.put("parentId", map.get("id"));
            List nodes = getDao().selectList("sysOrgMenu.getMenuNodesForParentId", data);

            if (!nodes.isEmpty()) {
                map.put("children", nodes);
                getNodes(nodes);
            } else {
                data.put("open", Boolean.valueOf(false));
                Map gchild = new HashMap();
                gchild.put("id", "prm" + map.get("id"));
                gchild.put("open", Integer.valueOf(1));
                gchild.put("parentId", map.get("id"));
                gchild.put("name", "功能权限");
                gchild.put("nocheck", Boolean.valueOf(false));
                gchild.put("title", "");
                gchild.put("icon", "javascript:void(0)");

                String permisions = getPermisionCheckboxList();
                Map child1 = new HashMap();
                child1.put("id", "prm" + map.get("id"));
                child1.put("open", Integer.valueOf(1));
                child1.put("parentId", map.get("id"));
                child1.put("name", permisions);
                child1.put("nocheck", Boolean.valueOf(true));
                child1.put("title", "");
                child1.put("icon", "javascript:void(0)");

                List children1 = new ArrayList();
                children1.add(child1);
                gchild.put("children", children1);

                Map cchild = new HashMap();
                cchild.put("id", "crm" + map.get("id"));
                cchild.put("open", Integer.valueOf(1));
                cchild.put("parentId", map.get("id"));
                cchild.put("name", "组件权限");
                cchild.put("nocheck", Boolean.valueOf(false));
                cchild.put("title", "");
                cchild.put("icon", "javascript:void(0)");

                String cpermisions = getComponentPermisionCheckboxList();
                Map child2 = new HashMap();
                child2.put("id", "crm" + map.get("id"));
                child2.put("open", Integer.valueOf(1));
                child2.put("parentId", map.get("id"));
                child2.put("name", cpermisions);
                child2.put("nocheck", Boolean.valueOf(true));
                child2.put("title", "");
                child2.put("icon", "javascript:void(0)");

                List children2 = new ArrayList();
                children2.add(child2);
                cchild.put("children", children2);

                List children = new ArrayList();
                children.add(gchild);
                children.add(cchild);

                map.put("isLast", Boolean.valueOf(true));
                map.put("children", children);
            }
        }
    }

    private String getPermisionCheckboxList() {
        StringBuilder builder = new StringBuilder();
        builder.append("<div style='display:inline-block'>");
        List<Map> data = getDao().selectList("permision.getAllPermision");
        List<Map> componentData = getDao().selectList("permisionComponent.getAllPermision");

        for (Map item : data) {
            String in = String.format(
                    "<input type='checkbox' name='chk-%s' value='%s' class='js-chk' style='margin:0'> %s &nbsp;&nbsp;", new Object[]{
                            item.get("Code"), item.get("Id"), item.get("Name")});
            builder.append(in);
        }
        builder.append("</div>");
        return builder.toString();
    }

    private String getComponentPermisionCheckboxList() {
        StringBuilder builder = new StringBuilder();
        builder.append("<div style='display:inline-block'>");
        List<Map> componentData = getDao().selectList("permisionComponent.getAllPermision");

        for (Map item : componentData) {
            String in = String.format(
                    "<input type='checkbox' name='chk-%s' value='%s' class='js-component-chk' style='margin:0'> %s &nbsp;&nbsp;", new Object[]{
                            item.get("Code"), item.get("Id"), item.get("Name")});
            builder.append(in);
        }
        builder.append("</div>");

        return builder.toString();
    }

    public void doSave(Map map) throws BizException {
        List errMsg = new ArrayList();
        Map data = new HashMap();

        if (map.get("organizationId") == null) {
            errMsg.add("请选择机构");
        }

        if (errMsg.size() > 0) {
            throw new BizException(errMsg);
        }

        getDao().delete("sysOrgMenu.deleteMenuList", map);
        getDao().delete("sysOrgMenuComponent.deleteMenuList", map);

        List<Map> list = (List) map.get("param");
        data.put("organizationId", map.get("organizationId"));

        for (Map item : list) {
            if ((!item.get("id").toString().startsWith("c")) && (!item.get("id").toString().startsWith("p"))) {

                data.put("menuId", item.get("id"));


                if ((item.get("permision") != null) && (((ArrayList) item.get("permision")).size() > 0)) {
                    List pList = (ArrayList) item.get("permision");
                    for (Object p : pList) {
                        if (p.toString().indexOf("[") > 0) {
                            data.put("permisionId", p.toString().substring(0, p.toString().indexOf("[")));
                            getDao().insert("sysOrgMenuComponent.saveMenu", data);
                        } else {
                            data.put("permisionId", p);
                            getDao().insert("sysOrgMenu.saveMenu", data);
                        }
                    }
                } else {
                    data.put("permisionId", null);
                    getDao().insert("sysOrgMenu.saveMenu", data);
                }
            }
        }
    }

    public List menuList(Map map) {
        Map data = new HashMap();
        data.put("organizationId", map.get("id"));

        List list = getDao().selectList("sysOrgMenu.getMenuList", data);
        return list;
    }
}
