package com.iptv.sys.controller.admin;

import com.iptv.core.shiro.MyShiroRealm;
import com.iptv.sys.service.LoginService;
import com.iptv.sys.service.SysLogLoginService;
import com.iptv.sys.service.SysMenuService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AdminLoginController
        extends AdminBaseController {
    @Resource
    private LoginService loginService;
    @Resource
    SysMenuService sysMenuService;
    @Resource
    SysLogLoginService sysLogLoginService;

    private static MyShiroRealm.ShiroUser getCurrentUser() {
        Subject subject = SecurityUtils.getSubject();
        return (MyShiroRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
    }

    @RequestMapping(value = "/login")
    public String login() {
        Subject currentUser = SecurityUtils.getSubject();
        if (currentUser.isAuthenticated() || currentUser.isRemembered()) {
            return "redirect:/admin/main";
        } else {
            return "login";
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    /*@RequestLimit(count=2,time=60000) 请求限制demo*/
    public String fail(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        Map param = new HashMap();
        param.put("IPAddress",getIpAddress(request));
        if(getCurrentUser() != null){
            param.put("Remark","登录成功");
            param.put("UserCode",getCurrentUser().getAccount());
            param.put("UserName",getCurrentUser().getRealName());
            param.put("LoginStatus","1");
            sysLogLoginService.saveLoginLog(param);
            //重定向
            return "redirect:/admin/main";
        }
        String message;
        String loginFailure = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
        if (loginFailure != null) {
            switch (loginFailure){
                case "org.apache.shiro.authc.pam.UnsupportedTokenException":
                    message = "验证码错误!";//验证码错误
                    break;
                case "org.apache.shiro.authc.UnknownAccountException":
                    message = "用户名或密码错误!";//用户名或密码错误
                    break;
                case "org.apache.shiro.authc.DisabledAccountException":
                    message = "该帐号已被禁用，请联系系统管理员";//此账号已被禁用
                    break;
                case "org.apache.shiro.authc.LockedAccountException":
                    message = "此账号密码输入错误已达5次被锁定，30分钟后解锁!";//此账号密码输入错误已达5次被锁定，30分钟后解锁
                    break;
                case "org.apache.shiro.authc.AuthenticationException":
                    message = "账号认证失败!";//账号认证失败
                    break;
                case "org.apache.shiro.authc.IncorrectCredentialsException":
                    message = "密码错误";//错误
                    break;
                default:
                    message = "鉴权失败";
                    break;
            }
            String loginAccount = request.getParameter(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM);
            if (StringUtils.isEmpty(loginAccount)){
                loginAccount="登录时账号为空";
            }
            param.put("Remark","登录失败");
            param.put("UserCode",loginAccount);
            param.put("LoginStatus","0");
            sysLogLoginService.saveLoginLog(param);
            redirectAttributes.addFlashAttribute("message", message);
        }
        return "redirect:/admin/login";
    }

    @RequestMapping(value = "/main")
    public String home(Model model){
        return "main";
    }

    @RequestMapping(value = "/logout")
    public String logout(){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "redirect:/backstage/login";
    }

    @RequestMapping({"/home"})
    public ModelAndView home(HttpServletRequest req) {
        Map res = new HashMap();

        this.log.info("访问Controller:admin/main,加载系统菜单");
        return view("sys/home/index", res);
    }

    /** 方法名称: getIpAddress
     * 参数: [request]
     * 返回类型: java.lang.String
     * 描述: 获取真实IP地址
     * 创建人: sunpenglin
     * 创建时间: 2018/8/10 11:30
     **/
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
