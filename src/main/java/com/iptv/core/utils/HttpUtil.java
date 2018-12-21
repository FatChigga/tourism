package com.iptv.core.utils;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.util.URIUtil;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class HttpUtil {
    public HttpUtil() {
    }

    public static String sendGet(String url, String queryString) {
        String response = null;
        HttpClient client = new HttpClient();
        GetMethod method = new GetMethod(url);

        try {
            if (queryString.length() > 0) {
                method.setQueryString(URIUtil.encodeQuery(queryString));
            }

            client.executeMethod(method);
            if (method.getStatusCode() == 200) {
                response = method.getResponseBodyAsString();
            }
        } catch (URIException var10) {
            ;
        } catch (IOException var11) {
            ;
        } finally {
            method.releaseConnection();
        }

        return response;
    }

    public static String doPost(String url, Map<String, String> params) {
        String response = null;
        HttpClient client = new HttpClient();
        PostMethod method = new PostMethod(url);
        Iterator it = params.entrySet().iterator();

        while (it.hasNext()) {
            ;
        }

        if (params != null) {
            HttpMethodParams p = new HttpMethodParams();
            Iterator var7 = params.entrySet().iterator();

            while (var7.hasNext()) {
                Entry<String, String> entry = (Entry) var7.next();
                p.setParameter((String) entry.getKey(), entry.getValue());
            }

            method.setParams(p);
        }

        try {
            client.executeMethod(method);
            if (method.getStatusCode() == 200) {
                response = method.getResponseBodyAsString();
            }
        } catch (IOException var10) {
            ;
        } finally {
            method.releaseConnection();
        }

        return response;
    }
}
