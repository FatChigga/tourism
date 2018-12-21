package com.iptv.core.utils;

import java.net.URLDecoder;

public class UrlUtil {
    public static String encode(String url) {
        return java.net.URLEncoder.encode(url);
    }

    public static String decode(String url) {
        return URLDecoder.decode(url);
    }
}