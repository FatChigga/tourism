package com.iptv.sys.service;

import com.iptv.core.common.KendoResult;
import com.iptv.core.service.BaseService;

import java.util.Map;

public abstract interface SysLogVisitService
        extends BaseService {
    public abstract KendoResult getLogVisitPaged(Map paramMap);
}
