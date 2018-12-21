package com.iptv.sys.service;

import com.iptv.core.common.BizException;
import com.iptv.core.common.KendoResult;
import com.iptv.core.service.BaseService;

import java.util.Map;

public abstract interface SysPermisionComponentService
        extends BaseService {
    public abstract KendoResult getPermisionComponentPaged(Map paramMap);

    public abstract Map getPermisionComponentById(Map paramMap);

    public abstract void updatePermisionComponent(Map paramMap)
            throws BizException;

    public abstract void deletePermisionComponent(Map paramMap)
            throws BizException;
}


/* Location:              C:\Users\宋羽翔\Desktop\com.iptv.core.jar!\com\iptv\sys\service\SysPermisionComponentService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */