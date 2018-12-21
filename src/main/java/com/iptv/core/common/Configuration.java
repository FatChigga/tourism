package com.iptv.core.common;

import com.iptv.core.utils.JsonUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Properties;


public class Configuration {
    public static final Properties webCfg = new Properties();
    private static final String env = System.getenv("JAVA_ENV");
    private static String webserviceUrl;

    static {
        try {
            InputStreamReader inWebCfg = null;

            if ((env != null) && (env.equals("production"))) {
                inWebCfg = new InputStreamReader(
                        Configuration.class.getResourceAsStream("/production/webconfig.properties"), "UTF-8");

                Map jsonCfg = from("/production/webconfig.json");

                webCfg.load(inWebCfg);

                if (jsonCfg != null) {
                    webCfg.putAll(jsonCfg);
                }
            } else if ((env != null) && (env.equals("test"))) {
                inWebCfg = new InputStreamReader(Configuration.class.getResourceAsStream("/test/webconfig.properties"),
                        "UTF-8");

                Map jsonCfg = from("/test/webconfig.json");

                webCfg.load(inWebCfg);

                if (jsonCfg != null) {
                    webCfg.putAll(jsonCfg);
                }
            } else {
                inWebCfg = new InputStreamReader(
                        Configuration.class.getResourceAsStream("/webconfig.properties"), "UTF-8");

                Map jsonCfg = from("/webconfig.json");

                webCfg.load(inWebCfg);

                if (jsonCfg != null) {
                    webCfg.putAll(jsonCfg);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map from(String fileName) {
        fileName = fileName.startsWith("/") ? fileName : String.format("/%s", new Object[]{fileName});
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(
                    Configuration.class.getResourceAsStream(fileName), "UTF-8");

            if (fileName.endsWith(".properties")) {
                Properties prop = new Properties();
                prop.load(inputStreamReader);
                return prop;
            }
            if (fileName.endsWith(".json")) {
                BufferedReader in = new BufferedReader(inputStreamReader);
                StringBuffer sb = new StringBuffer();
                String line = "";
                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }

                return (Map) JsonUtil.getObject(sb.toString());
            }
        } catch (Exception e) {
            return null;
        }

        return null;
    }
}

