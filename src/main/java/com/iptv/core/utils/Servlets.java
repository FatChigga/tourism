/**
 * Copyright (c) 2005-2012 springside.org.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.iptv.core.utils;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.TreeMap;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.iptv.core.common.Configuration;
import com.iptv.core.shiro.MyShiroRealm;
import org.apache.commons.lang3.Validate;
import org.apache.shiro.SecurityUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import org.apache.commons.lang3.StringUtils;

/** 类名: Servlets
 * 包名: com.iptv.core.common
 * 描述: Http与Servlet工具类
 * 创建人: songyuxiang
 * 创建时间: 2018/11/29 14:46
 */
public class Servlets {

    // 静态文件后缀
    private final static String[] staticFiles = StringUtils.split(Configuration.webCfg.getProperty("web.staticFile"),",");

    // 动态映射URL后缀
    private final static String urlSuffix = Configuration.webCfg.getProperty("web.urlSuffix");

    /**
     * 取得带相同前缀的Request Parameters, copy from spring WebUtils.
     *
     * 返回的结果的Parameter名已去除前缀.
     */
    @SuppressWarnings("rawtypes")
    public static Map<String, Object> getParametersStartingWith(ServletRequest request, String prefix) {
        Validate.notNull(request, "Request must not be null");
        Enumeration paramNames = request.getParameterNames();
        Map<String, Object> params = new TreeMap<String, Object>();
        String pre = prefix;
        if (pre == null) {
            pre = "";
        }
        while (paramNames != null && paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            if ("".equals(pre) || paramName.startsWith(pre)) {
                String unprefixed = paramName.substring(pre.length());
                String[] values = request.getParameterValues(paramName);
                if (values == null || values.length == 0) {
                    values = new String[]{};
                    // Do nothing, no values found at all.
                } else if (values.length > 1) {
                    params.put(unprefixed, values);
                } else {
                    params.put(unprefixed, values[0]);
                }
            }
        }
        return params;
    }

    /**
     * 组合Parameters生成Query String的Parameter部分,并在paramter name上加上prefix.
     *
     */
    public static String encodeParameterStringWithPrefix(Map<String, Object> params, String prefix) {
        StringBuilder queryStringBuilder = new StringBuilder();

        String pre = prefix;
        if (pre == null) {
            pre = "";
        }
        Iterator<Entry<String, Object>> it = params.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, Object> entry = it.next();
            queryStringBuilder.append(pre).append(entry.getKey()).append("=").append(entry.getValue());
            if (it.hasNext()) {
                queryStringBuilder.append("&");
            }
        }
        return queryStringBuilder.toString();
    }

    /**
     * 客户端对Http Basic验证的 Header进行编码.
     */
    public static String encodeHttpBasic(String userName, String password) {
        String encode = userName + ":" + password;
        return "Basic " + Encodes.encodeBase64(encode.getBytes());
    }

    /**
     * 是否是Ajax异步请求
     * @param request
     */
    public static boolean isAjaxRequest(HttpServletRequest request){

        String accept = request.getHeader("accept");
        String xRequestedWith = request.getHeader("X-Requested-With");
        MyShiroRealm.ShiroUser principal = (MyShiroRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();

        // 如果是异步请求或是手机端，则直接返回信息
        return ((accept != null && accept.indexOf("application/json") != -1
                || (xRequestedWith != null && xRequestedWith.indexOf("XMLHttpRequest") != -1)
                || (principal != null)));
    }

    /**
     * 获取当前请求对象
     * @return
     */
    public static HttpServletRequest getRequest(){
        try{
            return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        }catch(Exception e){
            return null;
        }
    }

    /**
     * 判断访问URI是否是静态文件请求
     * @throws Exception
     */
    public static boolean isStaticFile(String uri){
        if (staticFiles == null){
            try {
                throw new Exception("检测到“app.properties”中没有配置“web.staticFile”属性。配置示例：\n#静态文件后缀\n"
                        +"web.staticFile=.css,.js,.png,.jpg,.gif,.jpeg,.bmp,.ico,.swf,.psd,.htc,.crx,.xpi,.exe,.ipa,.apk");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//		if ((StringUtils.startsWith(uri, "/static/") || StringUtils.endsWithAny(uri, sfs))
//				&& !StringUtils.endsWithAny(uri, ".jsp") && !StringUtils.endsWithAny(uri, ".java")){
//			return true;
//		}
        if (StringUtils.endsWithAny(uri, staticFiles) && !StringUtils.endsWithAny(uri, urlSuffix)
                && !StringUtils.endsWithAny(uri, ".jsp") && !StringUtils.endsWithAny(uri, ".java")){
            return true;
        }
        return false;
    }
}
