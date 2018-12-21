package com.iptv.sys.controller.admin;

import java.util.Map;
import javax.annotation.Resource;

import com.iptv.core.common.KendoResult;
import com.iptv.sys.service.SysLogVisitService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping({"/sys/logVisit"})
public class SysLogVisitController
        extends AdminBaseController {
    @Resource
    private SysLogVisitService sysLogVisitService;

    @RequestMapping({"/index"})
    public ModelAndView index() {
        return view("/sys/logVisit/index");
    }

    @RequestMapping({"/logVisitList"})
    @ResponseBody
    public KendoResult logList(@RequestBody Map param) {
        KendoResult data = this.sysLogVisitService.getLogVisitPaged(param);
        return data;
    }
}
