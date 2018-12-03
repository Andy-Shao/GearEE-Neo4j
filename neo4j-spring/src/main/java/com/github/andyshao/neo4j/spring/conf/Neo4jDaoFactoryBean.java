package com.github.andyshao.neo4j.spring.conf;

import org.springframework.beans.factory.FactoryBean;

import com.github.andyshao.neo4j.dao.DaoContext;

import lombok.Setter;

/**
 * 
 * Title:<br>
 * Descript:<br>
 * Copyright: Copryright(c) Dec 3, 2018<br>
 * Encoding:UNIX UTF-8
 * @author Andy.Shao
 *
 * @param <T> bean type
 */
@Setter
public class Neo4jDaoFactoryBean<T> implements FactoryBean<T>{
    private DaoContext daoContext;
    private Class<T> daoInterface;
    
    @Override
    public T getObject() throws Exception {
        return daoContext.getDao(daoInterface);
    }

    @Override
    public Class<?> getObjectType() {
        return daoInterface;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
