package com.iptv.sys.service;

import com.iptv.core.common.BizException;
import com.iptv.core.service.BaseService;

import java.util.Map;

public abstract interface LoginService
        extends BaseService {
    public abstract Map Login(Map paramMap)
            throws BizException;
}
