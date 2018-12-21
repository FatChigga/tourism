/*
package com.iptv.core.shiro.dao;

import com.iptv.core.utils.Servlets;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

@Component
public class ShiroSessionDAO extends CachingSessionDAO {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ShiroRedisCache shiroRedisCache;

    @Override
    protected void doUpdate(Session session) {
        this.saveSession(session);
    }

    @Override
    protected void doDelete(Session session) {
        if (session == null || session.getId() == null) {
            logger.error("session or session id is null");
            return ;
        }
        shiroRedisCache.remove(session.getId());
    }

    @Override
    protected Serializable doCreate(Session session) {
        // 这里绑定sessionId到session，必须要有
        Serializable sessionId = generateSessionId(session);
        assignSessionId(session, sessionId);
        this.saveSession(session);
        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        Session s = null;
        HttpServletRequest request = Servlets.getRequest();
        if (request != null){
            String uri = request.getServletPath();
            // 如果是静态文件，则不获取SESSION
            if (Servlets.isStaticFile(uri)){
                return null;
            }
            s = (Session)request.getAttribute("session_"+sessionId);
        }
        if (s != null){
            return s;
        }

        Session session = null;
        try {
            session = (Session) shiroRedisCache.get(sessionId);
        } catch (Exception e) {
            logger.error("doReadSession {} {}", sessionId, request != null ? request.getRequestURI() : "", e);
        }


        if (request != null && session != null){
            request.setAttribute("session_"+sessionId, session);
        }

        return session;
    }

    private void saveSession(Session session) {
        if (session == null || session.getId() == null) {
            logger.error("session or session id is null");
            return ;
        }
        shiroRedisCache.put(session.getId(),session);
    }
}*/
