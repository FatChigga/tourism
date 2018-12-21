package com.iptv.sys.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.iptv.core.common.BizException;
import com.iptv.core.common.KendoResult;
import com.iptv.core.service.impl.BaseServiceImpl;
import com.iptv.sys.service.SysBizParamsService;
import org.springframework.stereotype.Service;


@Service
public class SysBizParamsServiceImpl
        extends BaseServiceImpl
        implements SysBizParamsService {

    public KendoResult getBizParamsPaged(Map map) {
        Map filter = (Map) map.get("filter");
        List filters = (List) filter.get("filters");
        for (int i = 0; i < filters.size(); i++) {
            Map param = (Map) filters.get(i);
            map.putAll(param);
        }
        KendoResult data = new KendoResult();
        List list = getDao().selectList("sysBizParams.getBizParamsList", map);
        int total = ((Integer) getDao().selectOne("sysBizParams.getBizParamsTotal", map)).intValue();
        int pageNum = ((Integer) getDao().selectOne("sysBizParams.getBizParamsPageNum", map)).intValue();
        data.setData(list);
        data.setPageNum(pageNum);
        data.setTotal(Integer.valueOf(total));
        return data;
    }

    public Map findParam(Map map) {
        Map data = null;
        if (map.get("sign").equals("left")) {
            getDao().selectOne("sysBizParams.getParam", map);
        } else {
            data = (Map) getDao().selectOne("sysBizParams.findParam", map);
        }
        return data;
    }

    public void update(Map map) throws BizException {
        List errMsg = new ArrayList();

        if ((map.get("KeyName") == null) || (map.get("KeyName").toString().trim().isEmpty())) {
            errMsg.add("请输入参数名称。");
        }
        if ((map.get("KeyCode") == null) || (map.get("KeyCode").toString().trim().isEmpty())) {
            errMsg.add("请输入参数编码。");
        }
        if (!map.get("sign").equals("left")) {
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
                Map Code = (Map) getDao().selectOne("sysBizParams.findExistParam", map);
                if (Code != null) {
                    errMsg.add("参数值不能重复，请重新输入。");
                }
            }
        }

        if ((map.get("BeforeValue") == null) && (map.get("BeforeKey") == null)) {
            if (map.get("sign").equals("left")) {
                if (map.get("KeyName") != null) {
                    map.put("KeyName", map.get("KeyName").toString().trim());
                }
                int num = ((Integer) getDao().selectOne("sysBizParams.getExistName", map)).intValue();
                if (num > 0) {
                    errMsg.add("参数名称已存在，请重新输入。");
                }

                if (map.get("KeyCode") != null) {
                    map.put("KeyCode", map.get("KeyCode").toString().trim());
                }
                int count = ((Integer) getDao().selectOne("sysBizParams.getExistCount", map)).intValue();
                if (count > 0) {
                    errMsg.add("参数编码已存在,请重新输入。");
                }
            }

            if (errMsg.size() > 0) {
                throw new BizException(errMsg);
            }

            getDao().insert("sysBizParams.save", map);
        } else if (map.get("sign").equals("left")) {
            if (!map.get("BeforeKey").equals(map.get("KeyName"))) {
                if (map.get("KeyName") != null) {
                    map.put("KeyName", map.get("KeyName").toString().trim());
                }
                int num = ((Integer) getDao().selectOne("sysBizParams.getExistName", map)).intValue();
                if (num > 0) {
                    errMsg.add("参数名称已存在，请重新输入。");
                }
            }

            if (!map.get("BeforeValue").equals(map.get("KeyCode"))) {
                if (map.get("KeyCode") != null) {
                    map.put("KeyCode", map.get("KeyCode").toString().trim());
                }
                int count = ((Integer) getDao().selectOne("sysBizParams.getExistCount", map)).intValue();
                if (count > 0) {
                    errMsg.add("参数编码已存在,请重新输入。");
                }
            }

            if (errMsg.size() > 0) {
                throw new BizException(errMsg);
            }
            map.put("oldKeyCode", map.get("BeforeValue"));
            getDao().update("sysBizParams.updateBizParams", map);
        } else {
            if (errMsg.size() > 0) {
                throw new BizException(errMsg);
            }
            getDao().update("sysBizParams.update", map);
        }
    }

    public void delete(Map map)
            throws BizException {
        List errmsg = new ArrayList();

        if (map.get("sign").equals("left")) {
            if ((map.get("KeyName") == null) || (map.get("KeyCode") == null)) {
                errmsg.add("请选择要删除的业务参数。");
            }
            if (errmsg.size() > 0) {
                throw new BizException(errmsg);
            }
            getDao().delete("sysBizParams.deleteBizParams", map);
        } else {
            if (map.get("Id") == null) {
                errmsg.add("请选择要删除的业务参数。");
            }
            if (errmsg.size() > 0) {
                throw new BizException(errmsg);
            }
            getDao().delete("sysBizParams.delete", map);
        }
    }


    public List getBizParam(String key) {
        Map map = new HashMap();
        map.put("Key", key);

        List res = new ArrayList();
        res = getDao().selectList("sysBizParams.getBizDic", map);

        return res;
    }

    public List<Map> getAllBizParamsForNode() {
        List nodes = new ArrayList();
        List data = getDao().selectList("sysBizParams.getRootNodes");

        Map root = new HashMap();
        root.put("id", Integer.valueOf(0));
        root.put("name", "业务参数");
        root.put("url", null);
        root.put("open", Boolean.valueOf(true));
        root.put("children", data);
        nodes.add(root);

        return nodes;
    }
}
