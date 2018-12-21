package com.iptv.sys.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.iptv.core.common.BizException;
import com.iptv.core.common.KendoResult;
import com.iptv.core.service.impl.BaseServiceImpl;
import com.iptv.core.utils.QueryUtil;
import com.iptv.sys.service.SysDictionaryService;
import org.springframework.stereotype.Service;

@Service
public class SysDictionaryServiceImpl
        extends BaseServiceImpl
        implements SysDictionaryService {
    public KendoResult getDictionaryPaged(Map map) {
        KendoResult data = QueryUtil.getRecordsPaged("sysDictionary.getDictionaryPaged", map);
        return data;
    }

    public Map findDictionary(Map map) {
        Map data = (Map) getDao().selectOne("sysDictionary.findDictionary", map);
        return data;
    }

    public void update(Map map) throws BizException {
        List errMsg = new ArrayList();

        if ((map.get("KeyCode") == null) || (map.get("KeyCode").toString().trim().isEmpty())) {
            errMsg.add("请输入参数名。");
        }
        if ((map.get("KeyValue") == null) || (map.get("KeyValue").toString().trim().isEmpty())) {
            errMsg.add("请输入参数值。");
        }
        if ((map.get("Text") == null) || (map.get("Text").toString().trim().isEmpty())) {
            errMsg.add("请输入显示文本。");
        }
        if (map.get("IsEnable") == null) {
            errMsg.add("请选择是否启用。");
        }
        if (map.get("IsVisible") == null) {
            errMsg.add("请选择是否可见。");
        }
        if ((map.get("OrderNum") == null) || (map.get("OrderNum").toString().trim().isEmpty())) {
            errMsg.add("请输入排序号。");
        }

        if ((map.get("BeforeValue") == null) || (!map.get("BeforeValue").equals(map.get("KeyValue")))) {
            Map Code = (Map) getDao().selectOne("sysDictionary.findDictionary", map);
            if (Code != null) {
                errMsg.add("参数值不能重复，请重新输入。");
            }
        }

        if ((map.get("BeforeValue") == null) || (map.get("BeforeKey") == null)) {
            if (errMsg.size() > 0) {
                throw new BizException(errMsg);
            }

            getDao().insert("sysDictionary.save", map);
        } else {
            if (errMsg.size() > 0) {
                throw new BizException(errMsg);
            }

            getDao().update("sysDictionary.update", map);
        }
    }

    public void delete(Map map) throws BizException {
        List errmsg = new ArrayList();

        if ((map.get("KeyValue") == null) || (map.get("KeyCode") == null)) {
            errmsg.add("请选择要删除的系统参数。");
        }
        if (errmsg.size() > 0) {
            throw new BizException(errmsg);
        }

        List keyList = (List) map.get("KeyCode");

        List valueList = (List) map.get("KeyValue");

        Map data = new HashMap();

        for (int i = 0; i < keyList.size(); i++) {
            data.put("KeyCode", keyList.get(i));
            data.put("KeyValue", valueList.get(i));
            getDao().delete("sysDictionary.delete", data);
        }
    }
}
