package com.iptv.core.controller;

import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;


public class BaseController {
    public Logger log = LoggerFactory.getLogger(getClass());
    @Resource
    HttpServletRequest request;

    public ModelAndView view() {
        String path = this.request.getServletPath();
        ModelAndView mv = new ModelAndView(path);

        return mv;
    }

    public ModelAndView view(String view) {
        ModelAndView mv = new ModelAndView(view);

        return mv;
    }

    public ModelAndView view(String view, String attributeName, Object attributeValue) {
        ModelAndView mv = new ModelAndView(view);
        mv.addObject(attributeName, attributeValue);

        return mv;
    }

    public ModelAndView view(Map<String, Object> data) {
        String path = this.request.getServletPath();
        ModelAndView mv = new ModelAndView(path);
        mv.addAllObjects(data);

        return mv;
    }

    public ModelAndView view(String view, Map<String, Object> data) {
        ModelAndView mv = new ModelAndView(view);
        mv.addAllObjects(data);

        return mv;
    }

    public ModelAndView redirect(String url) {
        String redirectUrl = String.format("redirect:%s", new Object[]{url});
        return new ModelAndView(redirectUrl);
    }
}
