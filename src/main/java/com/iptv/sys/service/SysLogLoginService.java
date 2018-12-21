package com.iptv.sys.service;

import com.iptv.core.common.KendoResult;
import com.iptv.core.service.BaseService;

import java.util.List;
import java.util.Map;

public abstract interface SysLogLoginService
        extends BaseService {
    public abstract KendoResult getLogLoginPaged(Map paramMap);

    public abstract List getLogLoginLatest();

    public int saveLoginLog(Map param);
}
