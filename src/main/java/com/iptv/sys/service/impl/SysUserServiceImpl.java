package com.iptv.sys.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.iptv.core.common.BizException;
import com.iptv.core.common.Configuration;
import com.iptv.core.common.KendoResult;
import com.iptv.core.service.impl.BaseServiceImpl;
import com.iptv.core.utils.DateUtil;
import com.iptv.core.utils.EncryptUtil;
import com.iptv.core.utils.JsonUtil;
import com.iptv.core.utils.QueryUtil;
import com.iptv.sys.service.SysUserService;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl
        extends BaseServiceImpl implements SysUserService {

    public KendoResult getUserPaged(Map map) {
        KendoResult data = QueryUtil.getRecordsPaged("user.getUserPaged", map);
        return data;
    }

    public Map findUserById(Map map) {
        Map data = (Map) getDao().selectOne("user.findUserById", map);
        return data;
    }

    public void save(Map map) throws Exception {
        List errMsg = new ArrayList();

        if ((map.get("Code") == null) || (map.get("Code").toString().trim().isEmpty())) {
            errMsg.add("请输入用户编号。");
        }
        if ((map.get("UserName") == null) || (map.get("UserName").toString().trim().isEmpty())) {
            errMsg.add("请输入真实姓名。");
        }
        if ((map.get("LoginName") == null) || (map.get("LoginName").toString().trim().isEmpty())) {
            errMsg.add("请输入登录名。");
        }
        if ((map.get("Password") == null) || (map.get("Password").toString().trim().isEmpty())) {
            errMsg.add("请输入密码。");
        }
        if ((map.get("ConfrimPassword") == null) || (map.get("ConfrimPassword").toString().trim().isEmpty())) {
            errMsg.add("请再次输入密码。");
        }
        if ((map.get("Password") != null) && (map.get("ConfrimPassword") != null) &&
                (!map.get("Password").equals(map.get("ConfrimPassword")))) {
            errMsg.add("您两次输入的密码不一致，请重新输入。");
        }
        if ((map.get("Password") != null) && (map.get("Password").toString().length() < 6)) {
            errMsg.add("密码长度不能少于6位。");
        }
        if ((map.get("ConfrimPassword") != null) && (map.get("ConfrimPassword").toString().length() < 6)) {
            errMsg.add("确认密码长度不能少于6位。");
        }
        if ((map.get("OrganizationId") != null) && (!map.get("OrganizationId").toString().matches("^\\d+$"))) {
            errMsg.add("所属部门必须数字。");
        }
        if (map.get("OrganizationId") == null) {
            errMsg.add("请输入所属部门。");
        }
        if (map.get("Gender") == null) {
            errMsg.add("请输入性别。");
        }
        if ((map.get("Tel") == null) || (map.get("Tel").toString().trim().isEmpty())) {
            errMsg.add("请输入电话。");
        }

        String regex = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

        if (map.get("Email") == null) {
            errMsg.add("请输入邮箱。");
        } else if (!map.get("Email").toString().matches(regex)) {
            errMsg.add("邮箱格式不正确。");
        }
        map.put("UpdateTime", DateUtil.getNow());
        if ((map.get("BeforeLoginName") == null) || (!map.get("BeforeLoginName").equals(map.get("LoginName")))) {
            Map loginname = (Map) getDao().selectOne("user.findUserByLoginName", map.get("LoginName"));
            if (loginname != null) {
                errMsg.add("您输入的登录名已存在,请重新输入。");
            }
        }

        if ((map.get("Id") == null) || (map.get("Id").equals("0"))) {
            if (errMsg.size() > 0) {
                throw new BizException(errMsg);
            }

            map.put("CreateTime", DateUtil.getNow());
            map.put("Password", EncryptUtil.encryptBySHA256(map.get("Password").toString(),map.get("LoginName").toString()));
            getDao().insert("user.save", map);
        } else {
            if (errMsg.size() > 0) {
                throw new BizException(errMsg);
            }

            Map confrim = (Map) getDao().selectOne("user.findUserById", map.get("Id"));
            map.put("Password", EncryptUtil.encryptBySHA256(map.get("Password").toString(),map.get("LoginName").toString()));

            if (map.get("Password").equals(confrim.get("Password"))) {
                getDao().update("user.updateNoPasswordModify", map);
            } else {
                getDao().update("user.update", map);
            }
        }
    }

    public void delete(Map map) throws BizException {
        List errmsg = new ArrayList();
        ArrayList ids = (ArrayList) map.get("Id");

        if ((map.get("Id") == null) || (ids.size() <= 0)) {
            errmsg.add("请选择用户。");
        }
        if (ids.contains(Integer.valueOf(1))) {
            errmsg.add("您选择的用户中含有超级管理员，超级管理员不允许被删除。");
        }

        if (errmsg.size() > 0) {
            throw new BizException(errmsg);
        }

        getDao().delete("sysUserRole.deleteUserRoleList", map);
        getDao().delete("sysUserMenu.deleteUserMenuList", map);
        getDao().delete("sysUserGroup.deleteUserGroupList", map);
        getDao().delete("sysUserMenuComponent.deleteRoleMenuComponentList", map);
        getDao().delete("user.delete", map);
    }

    public void passwordModiy(Map map) throws Exception {
        List errmsg = new ArrayList();

        String api = Configuration.webCfg.getProperty("cfg.webservice.api");

        if (map.get("Id") == null) {
            errmsg.add("请您先登录。");
        }
        if (map.get("BeforePassword") == null) {
            errmsg.add("请输入旧密码。");
        }
        if ((map.get("Password") == null) || (map.get("Password").toString().trim().isEmpty())) {
            errmsg.add("请输入新密码。");
        }
        if ((map.get("ConfirmPassword") == null) || (map.get("ConfirmPassword").toString().trim().isEmpty())) {
            errmsg.add("请再次输入新密码。");
        }
        if ((map.get("Password") != null) && (map.get("ConfirmPassword") != null) &&
                (!map.get("Password").equals(map.get("ConfirmPassword")))) {
            errmsg.add("您两次输入的新密码不一致，请重新输入。");
        }
        if (map.get("Password") != null) {
            map.put("Password", EncryptUtil.encryptBySHA256(map.get("Password").toString(),map.get("LoginName").toString()));
        }
        if (map.get("BeforePassword") != null) {
            map.put("BeforePassword", EncryptUtil.encryptBySHA256(map.get("BeforePassword").toString(),map.get("LoginName").toString()));
        }

        List data = getDao().selectList("user.checkBeforePassword", map);

        if (data.size() <= 0) {
            errmsg.add("您输入的旧密码不正确，请重新输入。");
        }

        if ((api == null) || (api.isEmpty())) {
            errmsg.add("密码修改不成功。");
        } else {
            Map user = (Map) getDao().selectOne("user.findUserById", map.get("Id"));

            Map userMap = new HashMap();
            userMap.put("Code", "A00102");
            userMap.put("DataType", "xml");
            userMap.put("LoginName", user.get("LoginName"));

            Map passmodifyMap = new HashMap();
            passmodifyMap.put("Code", "A00106");
            passmodifyMap.put("DataType", "json");
            passmodifyMap.put("Password", map.get("Password"));
            String result =null;

            if ((result != null) && (!result.equals("null")) &&
                    (((Map) JsonUtil.getObject(result)).get("ResultCode") != null)) {
                errmsg.add("密码修改不成功。");
            }
        }

        if (errmsg.size() > 0) {
            throw new BizException(errmsg);
        }

        getDao().update("user.updatePasswordModify", map);
    }

    public KendoResult findAllUser(Map map) {
        KendoResult data = QueryUtil.getSelectOptions("user.findAllUser", map);
        return data;
    }

    public Map getUserByAccount(String account) {
        return getDao().selectOne("user.findUserByLoginName",account);
    }

    public void loginUpdateUser(Map param){
        getDao().update("user.loginUpdateUser",param);
    }
}

