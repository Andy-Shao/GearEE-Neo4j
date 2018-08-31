package com.github.andyshao.neo4j.dao.impl;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.github.andyshao.neo4j.dao.DaoContext;
import com.github.andyshao.neo4j.dao.DaoFactory;
import com.github.andyshao.neo4j.model.Neo4jDaoInfo;

import lombok.Setter;

/**
 * 
 * Title:<br>
 * Descript:<br>
 * Copyright: Copryright(c) Aug 30, 2018<br>
 * Encoding:UNIX UTF-8
 * @author Andy.Shao
 *
 */
public abstract class AbstractDaoContext implements DaoContext {
    @Setter
    protected DaoFactory daoFactory;
    private final ConcurrentMap<Neo4jDaoInfo , Object> daoCache = new ConcurrentHashMap<>();
    protected abstract Neo4jDaoInfo findByName(String daoName);
    protected abstract Neo4jDaoInfo findByClass(Class<?> clazz);

    @Override
    public Object getDao(String daoName) {
        Neo4jDaoInfo neo4jDaoInfo = findByName(daoName);
        if(neo4jDaoInfo == null) return null;
        
        return daoCache.computeIfAbsent(neo4jDaoInfo , key -> daoFactory.buildDao(key));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getDao(Class<T> clazz) {
        Neo4jDaoInfo neo4jDaoInfo = findByClass(clazz);
        if(neo4jDaoInfo == null) return null;
        
        return (T) daoCache.computeIfAbsent(neo4jDaoInfo , key -> daoFactory.buildDao(key));
    }
}
