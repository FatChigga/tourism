package com.iptv.sys.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.iptv.core.common.BizException;
import com.iptv.core.common.KendoResult;
import com.iptv.core.service.impl.BaseServiceImpl;
import com.iptv.core.utils.QueryUtil;
import com.iptv.sys.service.SysRoleService;
import org.springframework.stereotype.Service;

@Service
public class SysRoleServiceImpl
        extends BaseServiceImpl
        implements SysRoleService {
    public KendoResult getRolePaged(Map map) {
        KendoResult data = QueryUtil.getRecordsPaged("role.getRolePaged", map);
        return data;
    }

    public Map findUserById(Map map) {
        Map data = (Map) getDao().selectOne("role.findRoleById", map);
        return data;
    }

    public void update(Map map) throws BizException {
        List errMsg = new ArrayList();

        if ((map.get("Code") == null) || (map.get("Code").toString().trim().isEmpty())) {
            errMsg.add("请输入编号。");
        }
        if ((map.get("Name") == null) || (map.get("Name").toString().trim().isEmpty())) {
            errMsg.add("请输入角色名称。");
        }
        if ((map.get("Level") == null) || (map.get("Level").toString().trim().isEmpty())) {
            errMsg.add("请输入角色等级。");
        }

        if ((map.get("Level") != null) && (!map.get("Level").toString().matches("^\\d+$"))) {
            errMsg.add("角色等级请输入纯数字。");
        }
        if (map.get("Enable") == null) {
            errMsg.add("请选择是否启用。");
        }
        if (map.get("IsSystem") == null) {
            errMsg.add("请选择是否是系统级。");
        }
        if ((map.get("OrderNum") == null) || (map.get("OrderNum").toString().trim().isEmpty())) {
            errMsg.add("请输入排序号。");
        }

        if ((map.get("BeforeCode") == null) || (!map.get("BeforeCode").equals(map.get("Code")))) {
            Map Code = (Map) getDao().selectOne("role.findRoleByCode", map.get("Code"));

            if (Code != null) {
                errMsg.add("编号不能重复");
            }
        }

        if ((map.get("Id") == null) || (map.get("Id").equals("0"))) {
            if (errMsg.size() > 0) {
                throw new BizException(errMsg);
            }

            getDao().insert("role.save", map);
        } else {
            if (errMsg.size() > 0) {
                throw new BizException(errMsg);
            }

            getDao().update("role.update", map);
        }
    }

    public void delete(Map map) throws BizException {
        List errmsg = new ArrayList();

        if ((map.get("Id") == null) || (((ArrayList) map.get("Id")).size() <= 0)) {
            errmsg.add("请选择角色");
        }
        if (errmsg.size() > 0) {
            throw new BizException(errmsg);
        }

        getDao().delete("sysRoleMenu.deleteRoleMenuList", map);
        getDao().delete("sysRoleMenuComponent.deleteRoleMenuComponentList", map);
        getDao().delete("role.delete", map);
    }

    public List getRoleList() {
        List list = getDao().selectList("role.roleList");
        return list;
    }
}
