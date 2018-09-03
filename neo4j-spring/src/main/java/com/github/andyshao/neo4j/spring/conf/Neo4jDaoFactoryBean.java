package com.github.andyshao.neo4j.spring.conf;

import org.springframework.beans.factory.FactoryBean;

import com.github.andyshao.neo4j.dao.DaoContext;

import lombok.Setter;

@Setter
public class Neo4jDaoFactoryBean<T> implements FactoryBean<T>{
    private Class<T> daoInterface;
    private DaoContext daoContext;
    
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
