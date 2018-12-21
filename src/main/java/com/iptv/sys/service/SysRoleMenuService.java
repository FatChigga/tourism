package com.iptv.sys.service;

import com.iptv.core.common.BizException;
import com.iptv.core.service.BaseService;

import java.util.List;
import java.util.Map;

public abstract interface SysRoleMenuService
        extends BaseService {
    public abstract List<Map> getAllRoleMenuForNode();

    public abstract void doSave(Map paramMap)
            throws BizException;

    public abstract List permissionList(Map paramMap);
}
