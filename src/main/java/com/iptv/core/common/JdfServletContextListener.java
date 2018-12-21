/*    */
package com.iptv.core.common;
/*    */ 
/*    */

import javax.servlet.ServletContextEvent;

/*    */
/*    */ public class JdfServletContextListener implements javax.servlet.ServletContextListener
/*    */ {
    /*  7 */   private static final String env = System.getenv("JAVA_ENV");

    /*    */
/*    */ 
/*    */ 
/*    */
    public void contextDestroyed(ServletContextEvent arg0) {
    }

    /*    */
/*    */ 
/*    */
    public void contextInitialized(ServletContextEvent arg0)
/*    */ {
/* 16 */
        System.out.println("==========================================");
/* 17 */
        System.out.println("| 当前运行模式：" + env + " |");
/* 18 */
        System.out.println("==========================================");
/*    */     
/* 20 */
        if ((env != null) && (env.equals("production"))) {
/* 21 */
            System.setProperty("spring.profiles.active", "production");
/* 22 */
        } else if ((env != null) && (env.equals("test"))) {
/* 23 */
            System.setProperty("spring.profiles.active", "test");
/*    */
        } else {
/* 25 */
            System.setProperty("spring.profiles.active", "development");
/*    */
        }
/*    */
    }
/*    */
}
