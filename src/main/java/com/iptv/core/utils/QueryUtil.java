package com.iptv.core.utils;

import com.iptv.core.common.KendoResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class QueryUtil {
    public static KendoResult getRecordsPaged(String statement, Map param) {
        Integer page = Integer.valueOf(param.get("page") == null ? 1 : Integer.parseInt(param.get("page").toString()));
        Integer pageSize = Integer.valueOf(param.get("pageSize") == null ? 20 : Integer.parseInt(param.get("pageSize").toString()));
        Integer offset = Integer.valueOf((page.intValue() - 1) * pageSize.intValue());

        param.put("offset", offset);
        param.put("rows", pageSize);

        List data = BaseUtil.getDao().selectList(statement, param);
        return new KendoResult(data, Integer.valueOf(param.get("total").toString()));
    }


    public static KendoResult getSelectOptions(String statement, Map param) {
        Map map = new HashMap();
        if (param.get("filter") != null) {
            Map filter = (Map) param.get("filter");
            ArrayList filters = (ArrayList) filter.get("filters");
            if (filters.size() > 0) {
                Map f = (Map) filters.get(0);
                String value = f.get("value").toString();

                if (!value.isEmpty()) {
                    map.put("text", value);
                }
            }
        }

        List data = BaseUtil.getDao().selectList(statement, map);
        return new KendoResult(data, Integer.valueOf(data.size()));
    }
}
