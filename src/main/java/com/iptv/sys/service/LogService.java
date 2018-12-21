package com.iptv.sys.service;

import com.iptv.core.common.BizException;
import com.iptv.core.common.KendoResult;
import com.iptv.core.service.BaseService;

import java.util.Map;

public abstract interface LogService
        extends BaseService {
    public abstract KendoResult getLogPaged(Map paramMap);

    public abstract void deleteLogById(Map paramMap)
            throws BizException;

    public abstract void deleteLogAll()
            throws BizException;
}