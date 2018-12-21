package com.iptv.core.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;

public abstract interface ApiService {
    @WebMethod
    @WebResult(name = "Result")
    public abstract String cgi(@WebParam(name = "params") String paramString);
}