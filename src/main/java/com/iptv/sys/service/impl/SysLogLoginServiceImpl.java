package com.iptv.sys.service.impl;

import com.iptv.core.common.KendoResult;
import com.iptv.core.service.impl.BaseServiceImpl;
import com.iptv.core.utils.QueryUtil;
import com.iptv.sys.service.SysLogLoginService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SysLogLoginServiceImpl
        extends BaseServiceImpl
        implements SysLogLoginService {
    public KendoResult getLogLoginPaged(Map map) {
        KendoResult data = QueryUtil.getRecordsPaged("sysLogLogin.getLogLoginPaged", map);
        return data;
    }

    public List getLogLoginLatest() {
        List list = getDao().selectList("sysLogLogin.getLogLoginLatest");
        return list;
    }

    public int saveLoginLog(Map param){
        int i = getDao().insert("sysLogLogin.doLogLogin",param);
        return i;
    }
}
