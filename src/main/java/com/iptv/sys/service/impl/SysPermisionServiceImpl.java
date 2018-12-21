package com.iptv.sys.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.iptv.core.common.BizException;
import com.iptv.core.common.KendoResult;
import com.iptv.core.service.impl.BaseServiceImpl;
import com.iptv.core.utils.QueryUtil;
import com.iptv.sys.service.SysPermisionService;
import org.springframework.stereotype.Service;

@Service
public class SysPermisionServiceImpl
        extends BaseServiceImpl
        implements SysPermisionService {
    public KendoResult getPermisionPaged(Map map) {
        KendoResult data = QueryUtil.getRecordsPaged("permision.getPermisionPaged", map);
        return data;
    }

    public Map findPermisionById(Map map) {
        Map data = (Map) getDao().selectOne("permision.findPermisionById", map);
        return data;
    }

    public void update(Map map) throws BizException {
        List errMsg = new ArrayList();

        if ((map.get("Code") == null) || (map.get("Code").toString().trim().isEmpty())) {
            errMsg.add("请输入编号。");
        }
        if ((map.get("Name") == null) || (map.get("Name").toString().trim().isEmpty())) {
            errMsg.add("请输入名称。");
        }
        if ((map.get("IconCss") == null) || (map.get("IconCss").toString().trim().isEmpty())) {
            errMsg.add("请输入图标样式。");
        }
        if (map.get("IsEnable") == null) {
            errMsg.add("请选择是否启用。");
        }

        if ((map.get("BeforeCode") == null) || (!map.get("BeforeCode").equals(map.get("Code")))) {
            Map Code = (Map) getDao().selectOne("permision.findPermisionByCode", map.get("Code"));

            if (Code != null) {
                errMsg.add("编号不能重复");
            }
        }

        if ((map.get("Id") == null) || (map.get("Id").equals("0"))) {
            if (errMsg.size() > 0) {
                throw new BizException(errMsg);
            }

            getDao().insert("permision.save", map);
        } else {
            if (errMsg.size() > 0) {
                throw new BizException(errMsg);
            }

            getDao().update("permision.update", map);
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

        getDao().delete("permision.delete", map);
    }
}
