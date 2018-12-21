package com.iptv.core.dao;

import org.mybatis.spring.SqlSessionTemplate;

public abstract interface BasicDao {
    public abstract void setSqlSessionTemplate(SqlSessionTemplate paramSqlSessionTemplate);

    public abstract SqlSessionTemplate getSqlSessionTemplate();
}