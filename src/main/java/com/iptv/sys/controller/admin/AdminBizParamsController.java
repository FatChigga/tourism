package com.iptv.sys.controller.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import com.iptv.core.common.BizException;
import com.iptv.core.common.Configuration;
import com.iptv.core.common.KendoResult;
import com.iptv.core.utils.BaseUtil;
import com.iptv.sys.service.SysBizParamsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping({"/sys/bizParams"})
public class AdminBizParamsController
        extends AdminBaseController {
    @Resource
    SysBizParamsService bizParamsService;

    @RequestMapping({"/index"})
    public ModelAndView index() {
        Map map = new HashMap();
        map.put("staticUrl", Configuration.webCfg.getProperty("static.url"));
        return view("/sys/bizParams/index", map);
    }

    @RequestMapping({"/bizParamsList"})
    @ResponseBody
    public KendoResult bizParamsList(@RequestBody Map param) {
        KendoResult data = this.bizParamsService.getBizParamsPaged(param);
        return data;
    }

    @RequestMapping({"/getParam"})
    @ResponseBody
    public Map getParam(@RequestBody Map param) {
        Map map = this.bizParamsService.findParam(param);
        return map;
    }

    @RequestMapping({"/update"})
    @ResponseBody
    public Map update(@RequestBody Map param) {
        List errmsg = new ArrayList();
        Map map = new HashMap();
        try {
            this.bizParamsService.update(param);
        } catch (BizException e) {
            errmsg.addAll(e.getMessages());
        } catch (Exception ex) {
            this.log.error("错误信息-业务参数保存修改：" + ex.getMessage());
            BaseUtil.saveLog(0, "业务参数保存修改", ex.getMessage());
            errmsg.add("未知错误。");
        }

        if (errmsg.size() > 0) {
            map.put("result", Boolean.valueOf(false));
            map.put("message", BaseUtil.toHtml(errmsg));
        } else {
            map.put("result", Boolean.valueOf(true));
            map.put("message", "操作成功");
        }

        this.log.info("业务参数保存修改");
        return map;
    }

    @RequestMapping({"/delete"})
    @ResponseBody
    public Map delete(@RequestBody Map param) {
        List errmsg = new ArrayList();
        Map map = new HashMap();
        try {
            this.bizParamsService.delete(param);
        } catch (BizException e) {
            errmsg.addAll(e.getMessages());
        } catch (Exception ex) {
            this.log.error("错误信息-删除业务参数：" + ex.getMessage());
            BaseUtil.saveLog(0, "删除业务参数", ex.getMessage());
            errmsg.add("未知错误。");
        }

        if (errmsg.size() > 0) {
            map.put("result", Boolean.valueOf(false));
            map.put("message", errmsg);
        } else {
            map.put("result", Boolean.valueOf(true));
            map.put("message", "删除成功");
        }

        this.log.info("删除业务参数");
        return map;
    }

    @RequestMapping(value = {"/bizParamsNodes"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    @ResponseBody
    public List bizParamsNodes() {
        List data = this.bizParamsService.getAllBizParamsForNode();
        return data;
    }
}