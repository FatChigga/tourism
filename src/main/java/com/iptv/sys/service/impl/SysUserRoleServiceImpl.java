package com.iptv.sys.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.iptv.core.common.BizException;
import com.iptv.core.service.impl.BaseServiceImpl;
import com.iptv.sys.service.SysUserRoleService;
import org.springframework.stereotype.Service;

@Service
public class SysUserRoleServiceImpl
        extends BaseServiceImpl
        implements SysUserRoleService {
    public List<Map> getAllUserRoleForNode() {
        List nodes = new ArrayList();
        List roledata = getDao().selectList("sysUserRole.getRoleNodes");
        List userdata = getDao().selectList("sysUserRole.getUserNodes");

        Map roleRoot = new HashMap();
        roleRoot.put("id", Integer.valueOf(0));
        roleRoot.put("name", "用户角色");
        roleRoot.put("open", Boolean.valueOf(true));
        roleRoot.put("children", roledata);
        nodes.add(roleRoot);

        Map userRoot = new HashMap();
        userRoot.put("id", Integer.valueOf(0));
        userRoot.put("name", "用户列表");
        userRoot.put("open", Boolean.valueOf(true));
        userRoot.put("children", userdata);
        nodes.add(userRoot);

        return nodes;
    }

    public void doSave(Map map) throws BizException {
        List errMsg = new ArrayList();
        Map data = new HashMap();

        if (map.get("userId") == null) {
            errMsg.add("请选择用户");
        }

        if (errMsg.size() > 0) {
            throw new BizException(errMsg);
        }

        getDao().delete("sysUserRole.deleteRoleList", map);

        List list = (List) map.get("param");
        data.put("UserId", map.get("userId"));

        for (Object roleId : list) {
            data.put("RoleId", roleId.toString());
            getDao().insert("sysUserRole.saveRole", data);
        }
    }

    public List roleList(Map map) {
        Map data = new HashMap();
        data.put("UserId", map.get("id"));

        List list = getDao().selectList("sysUserRole.getRoleList", data);
        return list;
    }

    public List roleListByUserId(String userId){
        List list = getDao().selectList("sysUserRole.getRoleList", userId);
        return list;
    }
}
