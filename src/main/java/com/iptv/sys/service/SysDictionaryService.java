package com.iptv.sys.service;

import com.iptv.core.common.BizException;
import com.iptv.core.common.KendoResult;
import com.iptv.core.service.BaseService;

import java.util.Map;

public abstract interface SysDictionaryService
        extends BaseService {
    public abstract KendoResult getDictionaryPaged(Map paramMap);

    public abstract Map findDictionary(Map paramMap);

    public abstract void update(Map paramMap)
            throws BizException;

    public abstract void delete(Map paramMap)
            throws BizException;
}


/* Location:              C:\Users\宋羽翔\Desktop\com.iptv.core.jar!\com\iptv\sys\service\SysDictionaryService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */