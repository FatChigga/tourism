package com.iptv.core.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.iptv.core.service.SysParamService;
import com.iptv.core.service.impl.*;
import com.iptv.core.utils.BaseUtil;
import com.iptv.core.utils.DateUtil;
import org.springframework.stereotype.Service;

@Service
public class SysParamServiceImpl
        extends BaseServiceImpl
        implements SysParamService {
    @Resource
    private HttpSession session;
    @Resource
    private HttpServletRequest request;

    public void saveLog(String logInfo) {
        Map map = new HashMap();
        map.put("UserCode", "test");
        map.put("UserName", "test");
        map.put("IPAddress", "127.0.0.1");
        map.put("OperationType", "8");
        map.put("Operation", "test");
        map.put("CreateDate", DateUtil.getNow());
        map.put("Remark", logInfo);

        getDao().insert("sysParams.saveLog", map);
    }


    public void saveLog(int opreationType, String operation, String remark) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        HttpSession session = this.request.getSession();

        Map map = new HashMap();
        map.put("UserCode", session.getAttribute("userCode"));
        map.put("UserName", session.getAttribute("userName"));
        map.put("IPAddress", BaseUtil.getIpAddress(this.request));
        map.put("OperationType", Integer.valueOf(opreationType));
        map.put("Operation", operation);
        map.put("CreateDate", format.format(new Date()));
        map.put("Remark", remark);

        getDao().insert("sysParams.saveLog", map);
    }

    public List getSysParam(String key, Boolean isAll) {
        Map map = new HashMap();
        map.put("Key", key);
        List res = new ArrayList();

        if (isAll.booleanValue()) {
            res = getDao().selectList("sysParams.getAllSysDic", map);
        } else {
            res = getDao().selectList("sysParams.getSysDic", map);
        }

        return res;
    }

    public Map getSysParam(String key, String value) {
        Map map = new HashMap();
        map.put("Key", key);
        map.put("Value", value);

        Map res = (Map) getDao().selectOne("sysParams.getSysDic", map);
        return res;
    }
}
