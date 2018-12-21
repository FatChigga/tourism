package com.iptv.core.utils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import com.iptv.core.common.BizException;
import com.iptv.core.common.ServiceLocator;
import com.iptv.core.dao.BasicDao;
import com.iptv.core.service.SysParamService;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.cache.annotation.Cacheable;

public class BaseUtil {
    public BaseUtil() {
    }

    public static Object getService(String serviceName) {
        Object res = ServiceLocator.getService(serviceName);
        return res;
    }

    public static Object getService(String serviceName, Class clazz) {
        Object res = ServiceLocator.getService(serviceName, clazz);
        return res;
    }

    public static SqlSessionTemplate getDao() {
        BasicDao basicDao = (BasicDao) getService("basicDao");
        SqlSessionTemplate sqlSession = basicDao.getSqlSessionTemplate();
        return sqlSession;
    }

    public static void saveLog(int opreationType, String operation, String remark) {
        SysParamService sysParamService = (SysParamService) getService("sysParamService");
        sysParamService.saveLog(opreationType, operation, remark);
    }

    @Cacheable
    public static List getSysParam(String key) {
        SysParamService sysParamService = (SysParamService) getService("sysParamService");
        List data = sysParamService.getSysParam(key, Boolean.valueOf(false));
        return data;
    }

    @Cacheable
    public static Map getSysParam(String key, String value) {
        SysParamService sysParamService = (SysParamService) getService("sysParamService");
        Map data = sysParamService.getSysParam(key, value);
        return data;
    }

    public static String toHtml(List<String> data) {
        String html = "";

        for (int i = 0; i < data.size(); ++i) {
            String item = (String) data.get(i);
            if (i == data.size() - 1) {
                html = html + item;
            } else {
                html = html + item + "<br/>";
            }
        }

        return html;
    }

    public static String getIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }

        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if (ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")) {
                InetAddress inet = null;

                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException var4) {
                    var4.printStackTrace();
                }

                ipAddress = inet.getHostAddress();
            }
        }

        if (ipAddress != null && ipAddress.length() > 15 && ipAddress.indexOf(",") > 0) {
            ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
        }

        return ipAddress;
    }

    public static Map getRequestParamsMap(String queryString, String encode) {
        Map paramsMap = new HashMap();
        if (queryString != null && queryString.length() > 0) {
            int lastAmpersandIndex = 0;

            int ampersandIndex;
            do {
                ampersandIndex = queryString.indexOf(38, lastAmpersandIndex) + 1;
                String subStr;
                if (ampersandIndex > 0) {
                    subStr = queryString.substring(lastAmpersandIndex, ampersandIndex - 1);
                    lastAmpersandIndex = ampersandIndex;
                } else {
                    subStr = queryString.substring(lastAmpersandIndex);
                }

                String[] paramPair = subStr.split("=");
                String param = paramPair[0];
                String value = paramPair.length == 1 ? "" : paramPair[1];

                try {
                    value = URLDecoder.decode(value, encode);
                } catch (UnsupportedEncodingException var12) {
                    ;
                }

                String[] newValues;
                if (paramsMap.containsKey(param)) {
                    String[] values = (String[]) paramsMap.get(param);
                    int len = values.length;
                    newValues = new String[len + 1];
                    System.arraycopy(values, 0, newValues, 0, len);
                    newValues[len] = value;
                } else {
                    newValues = new String[]{value};
                }

                paramsMap.put(param, newValues);
            } while (ampersandIndex > 0);
        }

        return paramsMap;
    }

    public static Object invokeMethod(String serviceName, String methodName) throws BizException, Exception {
        Object service = getService(serviceName);
        Class clazz = service.getClass();
        Method method = clazz.getDeclaredMethod(methodName, new Class[0]);
        Object res = method.invoke(service, new Object[0]);
        return res;
    }

    public static Object invokeMethod(String serviceName, String methodName, Object params) throws BizException, Exception {
        Object service = getService(serviceName);
        Class clazz = service.getClass();

        Method method;
        Object res;
        try {
            method = clazz.getDeclaredMethod(methodName, new Class[]{Map.class});
            res = method.invoke(service, new Object[]{params});
            return res;
        } catch (Exception var7) {
            method = clazz.getDeclaredMethod(methodName, new Class[0]);
            res = method.invoke(service, new Object[0]);
            return res;
        }
    }

    public static Object invokeMethodWithArgs(String serviceName, String methodName, Object params) throws BizException, Exception {
        Object service = getService(serviceName);
        Class clazz = service.getClass();

        try {
            Method method;
            Object res;
            int value;
            if (params instanceof Integer) {
                value = ((Integer) params).intValue();
                method = clazz.getDeclaredMethod(methodName, new Class[]{Integer.class});
                res = method.invoke(service, new Object[]{Integer.valueOf(value)});
                return res;
            } else if (params instanceof BigInteger) {
                value = ((BigInteger) params).intValue();
                method = clazz.getDeclaredMethod(methodName, new Class[]{BigInteger.class});
                res = method.invoke(service, new Object[]{Integer.valueOf(value)});
                return res;
            } else if (params instanceof String) {
                String s = (String) params;
                method = clazz.getDeclaredMethod(methodName, new Class[]{String.class});
                res = method.invoke(service, new Object[]{s});
                return res;
            } else {
                if (params instanceof Double) {
                    double d = ((Double) params).doubleValue();
                    method = clazz.getDeclaredMethod(methodName, new Class[]{Double.class});
                    res = method.invoke(service, new Object[]{Double.valueOf(d)});
                    return res;
                } else if (params instanceof Float) {
                    float f = ((Float) params).floatValue();
                    method = clazz.getDeclaredMethod(methodName, new Class[]{Float.class});
                    res = method.invoke(service, new Object[]{Float.valueOf(f)});
                    return res;
                } else if (params instanceof Long) {
                    long l = ((Long) params).longValue();
                    method = clazz.getDeclaredMethod(methodName, new Class[]{Long.class});
                    res = method.invoke(service, new Object[]{Long.valueOf(l)});
                    return res;
                } else if (params instanceof Boolean) {
                    boolean b = ((Boolean) params).booleanValue();
                    method = clazz.getDeclaredMethod(methodName, new Class[]{Boolean.class});
                    res = method.invoke(service, new Object[]{Boolean.valueOf(b)});
                    return res;
                } else if (params instanceof Date) {
                    Date d = (Date) params;
                    method = clazz.getDeclaredMethod(methodName, new Class[]{Date.class});
                    res = method.invoke(service, new Object[]{d});
                    return res;
                } else if (params instanceof Timestamp) {
                    Timestamp d = (Timestamp) params;
                    method = clazz.getDeclaredMethod(methodName, new Class[]{Timestamp.class});
                    res = method.invoke(service, new Object[]{d});
                    return res;
                } else if (params instanceof BigDecimal) {
                    BigDecimal d = (BigDecimal) params;
                    method = clazz.getDeclaredMethod(methodName, new Class[]{BigDecimal.class});
                    res = method.invoke(service, new Object[]{d});
                    return res;
                } else {
                    return null;
                }
            }
        } catch (Exception var9) {
            throw new BizException("调用的方法名不存在或参数类型不匹配");
        }
    }

    public static Object invokeMethod(String serviceName, String methodName, Object... args) throws BizException, Exception {
        Class[] argsClass = new Class[args.length];
        int i = 0;

        for (int j = args.length; i < j; ++i) {
            argsClass[i] = args[i].getClass();
        }

        Object service = getService(serviceName);
        Class clazz = service.getClass();
        Method method = clazz.getDeclaredMethod(methodName, argsClass);
        Object res = method.invoke(service, args);
        return res;
    }
}
