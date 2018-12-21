package com.iptv.sys.service;

import com.iptv.core.common.BizException;
import com.iptv.core.common.KendoResult;
import com.iptv.core.service.BaseService;

import java.util.List;
import java.util.Map;

public abstract interface SysOrganizationService
        extends BaseService {
    public abstract List<Map> getALlOrganizationsForNode();

    public abstract void save(Map paramMap)
            throws BizException;

    public abstract void delete(Map paramMap)
            throws BizException;

    public abstract KendoResult getOrgOptions(Map paramMap);
}


/* Location:              C:\Users\宋羽翔\Desktop\com.iptv.core.jar!\com\iptv\sys\service\SysOrganizationService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */