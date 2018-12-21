package com.iptv.core.service;

import java.util.List;
import java.util.Map;

public abstract interface SysParamService
        extends BaseService {
    public abstract void saveLog(String paramString);

    public abstract void saveLog(int paramInt, String paramString1, String paramString2);

    public abstract List getSysParam(String paramString, Boolean paramBoolean);

    public abstract Map getSysParam(String paramString1, String paramString2);
}