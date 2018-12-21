package com.iptv.sys.service;

import com.iptv.core.common.BizException;
import com.iptv.core.service.BaseService;

import java.util.List;
import java.util.Map;

public abstract interface SysGroupService
        extends BaseService {
    public abstract List<Map> getALlGroupForNode();

    public abstract void save(Map paramMap)
            throws BizException;

    public abstract void delete(Map paramMap)
            throws BizException;
}
