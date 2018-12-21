package com.iptv.sys.controller.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.iptv.core.common.BizException;
import com.iptv.core.utils.BaseUtil;
import com.iptv.sys.service.SysMenuService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping({"/sys/menu"})
public class SysMenuController
        extends AdminBaseController {
    @Resource
    SysMenuService sysMenuService;

    @RequestMapping({"/index"})
    public ModelAndView index() {
        return view();
    }

    @RequestMapping(value = {"/menuList"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    @ResponseBody
    public List menuList() {
        List data = this.sysMenuService.getAllMenus();
        return data;
    }

    @RequestMapping(value = {"/menuNodes"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    @ResponseBody
    public List menuNodes() {
        List data = this.sysMenuService.getALlMenusForNode();
        return data;
    }

    @RequestMapping(value = {"/save"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    @ResponseBody
    public Map save(@RequestBody Map map) {
        List<String> messages = new ArrayList();
        Map res = new HashMap();
        try {
            this.sysMenuService.save(map);
        } catch (BizException biz) {
            messages.addAll(biz.getMessages());
        } catch (Exception ex) {
            this.log.error("数据库错误：" + ex.getMessage());
            messages.add("未知错误。");
        }

        if (messages.size() > 0) {
            res.put("result", Boolean.valueOf(false));
            res.put("message", BaseUtil.toHtml(messages));
        } else {
            res.put("result", Boolean.valueOf(true));
            res.put("message", "保存成功。");
        }

        this.log.info("添加或者修改系统菜单");
        return res;
    }

    @RequestMapping(value = {"/delete"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    @ResponseBody
    public Map delete(@RequestBody Map map) {
        List<String> messages = new ArrayList();
        Map res = new HashMap();
        try {
            this.sysMenuService.delete(map);
        } catch (BizException biz) {
            messages.addAll(biz.getMessages());
        } catch (Exception ex) {
            this.log.error("数据库错误：" + ex.getMessage());
            messages.add("未知错误。");
        }

        if (messages.size() > 0) {
            res.put("result", Boolean.valueOf(false));
            res.put("message", BaseUtil.toHtml(messages));
        } else {
            res.put("result", Boolean.valueOf(true));
            res.put("message", "删除成功。");
        }

        this.log.info("删除系统菜单");
        return res;
    }

    @RequestMapping({"/userMenuList"})
    @ResponseBody
    public List userMenuList(HttpServletRequest req) {
        HttpSession session = req.getSession();
        Integer userId = Integer.valueOf(session.getAttribute("userId").toString());

        List data = this.sysMenuService.getUserMenus(userId);
        return data;
    }
}
