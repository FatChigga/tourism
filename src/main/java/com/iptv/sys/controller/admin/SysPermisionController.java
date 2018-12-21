package com.iptv.sys.controller.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import com.iptv.core.common.BizException;
import com.iptv.core.common.KendoResult;
import com.iptv.core.utils.BaseUtil;
import com.iptv.sys.service.SysPermisionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping({"/sys/permision"})
public class SysPermisionController
        extends AdminBaseController {
    @Resource
    SysPermisionService sysPermisionService;

    @RequestMapping({"/index"})
    public ModelAndView index() {
        return view();
    }

    @RequestMapping({"/permisionList"})
    @ResponseBody
    public KendoResult permisionList(@RequestBody Map param) {
        KendoResult data = this.sysPermisionService.getPermisionPaged(param);
        return data;
    }

    @RequestMapping({"/getPermision"})
    @ResponseBody
    public Map getPermision(@RequestParam Map param) {
        Map map = this.sysPermisionService.findPermisionById(param);
        return map;
    }

    @RequestMapping({"/save"})
    @ResponseBody
    public Map save(@RequestBody Map param) {
        List errmsg = new ArrayList();
        Map map = new HashMap();
        try {
            this.sysPermisionService.update(param);
        } catch (BizException e) {
            errmsg.addAll(e.getMessages());
        } catch (Exception ex) {
            this.log.error("错误信息-权限保存修改：" + ex.getMessage());
            BaseUtil.saveLog(0, "权限保存修改", ex.getMessage());
            errmsg.add("未知错误。");
        }

        if (errmsg.size() > 0) {
            map.put("result", Boolean.valueOf(false));
            map.put("message", BaseUtil.toHtml(errmsg));
        } else {
            map.put("result", Boolean.valueOf(true));
            map.put("message", "操作成功");
        }

        this.log.info("保存修改权限");
        return map;
    }

    @RequestMapping({"/delete"})
    @ResponseBody
    public Map delete(@RequestBody Map param) {
        List errmsg = new ArrayList();
        Map map = new HashMap();
        try {
            this.sysPermisionService.delete(param);
        } catch (BizException e) {
            errmsg.addAll(e.getMessages());
        } catch (Exception ex) {
            this.log.error("错误信息-删除权限：" + ex.getMessage());
            BaseUtil.saveLog(0, "删除权限", ex.getMessage());
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

