package com.iptv.sys.service;

import com.iptv.core.common.BizException;
import com.iptv.core.service.BaseService;

import java.util.List;
import java.util.Map;

public abstract interface SysGroupMenuService
        extends BaseService {
    public abstract List<Map> getAllGroupMenuForNode();

    public abstract void doSave(Map paramMap)
            throws BizException;

    public abstract List menuList(Map paramMap);
}
