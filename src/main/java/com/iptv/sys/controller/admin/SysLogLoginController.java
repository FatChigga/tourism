package com.iptv.sys.controller.admin;

import java.util.Map;
import javax.annotation.Resource;

import com.iptv.core.common.KendoResult;
import com.iptv.sys.service.SysLogLoginService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping({"/sys/logLogin"})
public class SysLogLoginController
        extends AdminBaseController {
    @Resource
    SysLogLoginService sysLogLoginService;

    @RequestMapping({"/index"})
    public ModelAndView index() {
        return view();
    }

    @RequestMapping({"/logLoginList"})
    @ResponseBody
    public KendoResult dictionaryList(@RequestBody Map param) {
        KendoResult data = this.sysLogLoginService.getLogLoginPaged(param);
        return data;
    }
}
