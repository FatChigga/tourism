package com.iptv.core.common;

import java.util.LinkedHashMap;
import java.util.Map;

public enum UserStatusEnum{

    /*已激活*/
    ACTIVATE("激活", "ACTIVATE"),
    /*未激活*/
    NOTACTIVE("未激活", "NOTACTIVE"),
    /*已停用*/
    DISABLE("已停用", "DISABLE");

    private String nodeName;//环节名称

    private String nodeCode;//环节编码

    //  已停用

    private UserStatusEnum(String nodeName, String nodeCode){
        this.nodeName = nodeName;
        this.nodeCode = nodeCode;
    }

    //获取所有状态的map
    public static Map<String, String> getAllStatusMap() {
        Map<String, String> allStatusMap = new LinkedHashMap<String, String>();
        UserStatusEnum[] values = UserStatusEnum.values();
        for (UserStatusEnum las : values) {
            String nodeName = las.getNodeName();
            String nodeCode = las.getNodeCode();
            allStatusMap.put(nodeCode, nodeName);
        }
        return allStatusMap;
    }


    public String getNodeName() {
        return nodeName;
    }

    public String getNodeCode() {
        return nodeCode;
    }

}
