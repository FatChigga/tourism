package com.iptv.sys.controller.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import com.iptv.core.common.BizException;
import com.iptv.core.utils.BaseUtil;
import com.iptv.sys.service.SysOrganizationService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping({"/sys/organization"})
public class SysOrganizationController
        extends AdminBaseController {
    @Resource
    SysOrganizationService sysOrganizationService;

    @RequestMapping({"/index"})
    public ModelAndView index() {
        return view();
    }

    @RequestMapping(value = {"/organizationNodes"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    @ResponseBody
    public List organizationNodes() {
        List data = this.sysOrganizationService.getALlOrganizationsForNode();
        return data;
    }

    @RequestMapping(value = {"/save"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    @ResponseBody
    public Map save(@RequestBody Map map) {
        List<String> messages = new ArrayList();
        Map res = new HashMap();
        try {
            this.sysOrganizationService.save(map);
        } catch (BizException biz) {
            messages.addAll(biz.getMessages());
        } catch (Exception ex) {
            this.log.error("数据库错误：" + ex.getMessage());
            BaseUtil.saveLog(0, "添加或者修改机构", ex.getMessage());
            messages.add("未知错误。");
        }

        if (messages.size() > 0) {
            res.put("result", Boolean.valueOf(false));
            res.put("message", BaseUtil.toHtml(messages));
        } else {
            res.put("result", Boolean.valueOf(true));
            res.put("message", "保存成功。");
        }

        this.log.info("添加或者修改机构");
        return res;
    }

    @RequestMapping(value = {"/delete"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    @ResponseBody
    public Map delete(@RequestBody Map map) {
        List<String> messages = new ArrayList();
        Map res = new HashMap();
        try {
            this.sysOrganizationService.delete(map);
        } catch (BizException biz) {
            messages.addAll(biz.getMessages());
        } catch (Exception ex) {
            this.log.error("数据库错误：" + ex.getMessage());
            BaseUtil.saveLog(0, "删除机构", ex.getMessage());
            messages.add("未知错误。");
        }

        if (messages.size() > 0) {
            res.put("result", Boolean.valueOf(false));
            res.put("message", BaseUtil.toHtml(messages));
        } else {
            res.put("result", Boolean.valueOf(true));
            res.put("message", "删除成功。");
        }

        this.log.info("删除机构");
        return res;
    }
}