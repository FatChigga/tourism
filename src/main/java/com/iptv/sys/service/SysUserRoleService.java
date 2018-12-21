package com.iptv.sys.service;

import com.iptv.core.common.BizException;
import com.iptv.core.service.BaseService;

import java.util.List;
import java.util.Map;

public abstract interface SysUserRoleService
        extends BaseService {
    public abstract List<Map> getAllUserRoleForNode();

    public abstract void doSave(Map paramMap)
            throws BizException;

    public abstract List roleList(Map paramMap);

    public List roleListByUserId(String userId);
}
