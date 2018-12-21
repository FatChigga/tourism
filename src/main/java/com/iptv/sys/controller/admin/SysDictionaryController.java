package com.iptv.sys.controller.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import com.iptv.core.common.BizException;
import com.iptv.core.common.KendoResult;
import com.iptv.core.utils.BaseUtil;
import com.iptv.sys.service.SysDictionaryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping({"/sys/dictionary"})
public class SysDictionaryController
        extends AdminBaseController {
    @Resource
    SysDictionaryService sysDictionaryService;

    @RequestMapping({"/index"})
    public ModelAndView index() {
        return view();
    }

    @RequestMapping({"/dictionaryList"})
    @ResponseBody
    public KendoResult dictionaryList(@RequestBody Map param) {
        KendoResult data = this.sysDictionaryService.getDictionaryPaged(param);
        return data;
    }

    @RequestMapping({"/getDictionary"})
    @ResponseBody
    public Map getDictionary(@RequestParam Map param) {
        Map map = this.sysDictionaryService.findDictionary(param);
        return map;
    }

    @RequestMapping({"/update"})
    @ResponseBody
    public Map update(@RequestBody Map param) {
        List errmsg = new ArrayList();
        Map map = new HashMap();
        try {
            this.sysDictionaryService.update(param);
        } catch (BizException e) {
            errmsg.addAll(e.getMessages());
        } catch (Exception ex) {
            this.log.error("错误信息-系统参数保存修改：" + ex.getMessage());
            BaseUtil.saveLog(0, "系统参数保存修改", ex.getMessage());
            errmsg.add("未知错误。");
        }

        if (errmsg.size() > 0) {
            map.put("result", Boolean.valueOf(false));
            map.put("message", BaseUtil.toHtml(errmsg));
        } else {
            map.put("result", Boolean.valueOf(true));
            map.put("message", "操作成功");
        }

        this.log.info("系统参数保存修改");
        return map;
    }

    @RequestMapping({"/delete"})
    @ResponseBody
    public Map delete(@RequestBody Map param) {
        List errmsg = new ArrayList();
        Map map = new HashMap();
        try {
            this.sysDictionaryService.delete(param);
        } catch (BizException e) {
            errmsg.addAll(e.getMessages());
        } catch (Exception ex) {
            this.log.error("错误信息-删除系统参数：" + ex.getMessage());
            BaseUtil.saveLog(0, "删除系统参数", ex.getMessage());
            errmsg.add("未知错误。");
        }

        if (errmsg.size() > 0) {
            map.put("result", Boolean.valueOf(false));
            map.put("message", errmsg);
        } else {
            map.put("result", Boolean.valueOf(true));
            map.put("message", "删除成功");
        }

        this.log.info("删除系统参数");
        return map;
    }
}
