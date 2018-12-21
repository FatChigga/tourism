package com.iptv.core.common;
import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ import org.springframework.web.servlet.HandlerInterceptor;
/*    */ import org.springframework.web.servlet.ModelAndView;

/*    */ public class CORSInterceptor
/*    */ implements HandlerInterceptor
/*    */ {
    /*    */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
/*    */     throws Exception
/*    */ {
/* 15 */
        response.addHeader("Access-Control-Allow-Origin", "*");
/*    */     
/* 17 */
        return true;
/*    */
    }

    /*    */
/*    */
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
/*    */     throws Exception
/*    */ {
    }

    /*    */
/*    */
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
/*    */     throws Exception
/*    */ {
    }
/*    */
}


/* Location:              C:\Users\宋羽翔\Desktop\com.jdarc.core.jar!\com\iptv\core\common\CORSInterceptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */