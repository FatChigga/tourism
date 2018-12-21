package com.iptv.core.service.impl;

import com.iptv.core.dao.BasicDao;
import com.iptv.core.service.BaseService;
import com.iptv.core.utils.BaseUtil;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class BaseServiceImpl
        implements BaseService {
    public SqlSessionTemplate getDao() {
        BasicDao basicDao = (BasicDao) BaseUtil.getService("basicDao");
        SqlSessionTemplate sqlSession = basicDao.getSqlSessionTemplate();

        return sqlSession;
    }

    public Logger log = LoggerFactory.getLogger(getClass());
}