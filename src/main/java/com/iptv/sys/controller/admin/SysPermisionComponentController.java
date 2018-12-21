package com.iptv.sys.controller.admin;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import com.iptv.core.common.BizException;
import com.iptv.core.common.KendoResult;
import com.iptv.core.controller.BaseController;
import com.iptv.core.utils.BaseUtil;
import com.iptv.sys.service.SysPermisionComponentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping({"/sys/permisionComponent"})
public class SysPermisionComponentController
        extends BaseController {
    @Resource
    SysPermisionComponentService sysPermisionComponentService;

    @RequestMapping({"/index"})
    public ModelAndView index() {
        return view("/sys/permisionComponent/index");
    }

    @RequestMapping({"/permisionComponentList"})
    @ResponseBody
    public KendoResult permisionList(@RequestBody Map param) {
        KendoResult data = this.sysPermisionComponentService.getPermisionComponentPaged(param);
        return data;
    }

    @RequestMapping({"/getPermisionComponent"})
    @ResponseBody
    public Map getPermision(@RequestParam Map param) {
        Map map = this.sysPermisionComponentService.getPermisionComponentById(param);
        return map;
    }

    @RequestMapping({"/savePermisionComponent"})
    @ResponseBody
    public Map savePermisionComponent(@RequestBody Map param) {
        List errmsg = new ArrayList();
        Map map = new HashMap();
        try {
            this.sysPermisionComponentService.updatePermisionComponent(param);
        } catch (BizException e) {
            errmsg.addAll(e.getMessages());
        } catch (Exception ex) {
            this.log.error("错误信息-组件权限保存修改：" + ex.getMessage());
            BaseUtil.saveLog(0, "组件权限保存修改", ex.getMessage());
            errmsg.add("未知错误。");
        }

        if (errmsg.size() > 0) {
            map.put("result", Boolean.valueOf(false));
            map.put("message", BaseUtil.toHtml(errmsg));
        } else {
            map.put("result", Boolean.valueOf(true));
            map.put("message", "操作成功");
        }

        this.log.info("保存修改组件权限");
        return map;
    }

    @RequestMapping({"/deletePermisionComponent"})
    @ResponseBody
    public Map deletePermisionComponent(@RequestBody Map param) {
        List errmsg = new ArrayList();
        Map map = new HashMap();
        try {
            this.sysPermisionComponentService.deletePermisionComponent(param);
        } catch (BizException e) {
            errmsg.addAll(e.getMessages());
        } catch (Exception ex) {
            this.log.error("错误信息-删除组件权限：" + ex.getMessage());
            BaseUtil.saveLog(0, "删除组件权限", ex.getMessage());
            errmsg.add("未知错误。");
        }

        if (errmsg.size() > 0) {
            map.put("result", Boolean.valueOf(false));
            map.put("message", errmsg);
        } else {
            map.put("result", Boolean.valueOf(true));
            map.put("message", "删除成功");
        }

        this.log.info("删除权限");
        return map;
    }
}
