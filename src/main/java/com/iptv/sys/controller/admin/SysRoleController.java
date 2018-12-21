package com.iptv.sys.controller.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import com.iptv.core.common.BizException;
import com.iptv.core.common.KendoResult;
import com.iptv.core.utils.BaseUtil;
import com.iptv.sys.service.SysRoleService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping({"/sys/role"})
public class SysRoleController
        extends AdminBaseController {
    @Resource
    SysRoleService sysRoleService;

    @RequestMapping({"/index"})
    public ModelAndView index() {
        return view();
    }

    @RequestMapping({"/roleList"})
    @ResponseBody
    public KendoResult roleList(@RequestBody Map param) {
        KendoResult data = this.sysRoleService.getRolePaged(param);
        return data;
    }

    @RequestMapping({"/getRole"})
    @ResponseBody
    public Map getRole(@RequestParam Map param) {
        Map map = this.sysRoleService.findUserById(param);
        return map;
    }

    @RequestMapping({"/save"})
    @ResponseBody
    public Map save(@RequestBody Map param) {
        List errmsg = new ArrayList();
        Map map = new HashMap();
        try {
            this.sysRoleService.update(param);
        } catch (BizException e) {
            errmsg.addAll(e.getMessages());
        } catch (Exception ex) {
            this.log.error("错误信息-角色保存修改：" + ex.getMessage());
            BaseUtil.saveLog(0, "角色保存修改", ex.getMessage());
            errmsg.add("未知错误。");
        }

        if (errmsg.size() > 0) {
            map.put("result", Boolean.valueOf(false));
            map.put("message", BaseUtil.toHtml(errmsg));
        } else {
            map.put("result", Boolean.valueOf(true));
            map.put("message", "操作成功");
        }

        this.log.info("保存修改角色");
        return map;
    }

    @RequestMapping({"/delete"})
    @ResponseBody
    public Map delete(@RequestBody Map param) {
        List errmsg = new ArrayList();
        Map map = new HashMap();
        try {
            this.sysRoleService.delete(param);
        } catch (BizException e) {
            errmsg.addAll(e.getMessages());
        } catch (Exception ex) {
            this.log.error("错误信息-删除角色：" + ex.getMessage());
            BaseUtil.saveLog(0, "删除角色", ex.getMessage());
            errmsg.add("未知错误。");
        }

        if (errmsg.size() > 0) {
            map.put("result", Boolean.valueOf(false));
            map.put("message", errmsg);
        } else {
            map.put("result", Boolean.valueOf(true));
            map.put("message", "删除成功");
        }

        this.log.info("删除角色");
        return map;
    }
}
