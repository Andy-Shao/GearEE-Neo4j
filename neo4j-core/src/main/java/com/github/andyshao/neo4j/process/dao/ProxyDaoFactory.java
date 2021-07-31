package com.github.andyshao.neo4j.process.dao;

import com.github.andyshao.neo4j.Neo4jException;
import com.github.andyshao.neo4j.domain.Neo4jDao;
import com.github.andyshao.neo4j.domain.Neo4jSql;
import com.github.andyshao.neo4j.process.DaoProcessor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2021/8/1
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
@Deprecated
public class ProxyDaoFactory implements DaoFactory{
    private final DaoProcessor daoProcessor;

    public ProxyDaoFactory(DaoProcessor daoProcessor) {
        this.daoProcessor = daoProcessor;
    }

    @Override
    public Object buildDao(Neo4jDao neo4jDao) throws Neo4jException {
        final ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        final Class<?>[] interfaces = neo4jDao.getDaoClass().getInterfaces();
        return Proxy.newProxyInstance(contextClassLoader, interfaces, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                final String sqlName = method.getName();
                final Neo4jSql neo4jSql = neo4jDao.findNeo4jSql(sqlName, method.getParameterTypes()).orElseThrow();
                return ProxyDaoFactory.this.daoProcessor.processing(neo4jDao, neo4jSql, args);
            }
        });
    }
}
