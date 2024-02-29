package com.github.andyshao.neo4j.process.dao;

import com.github.andyshao.lang.ArrayWrapper;
import com.github.andyshao.neo4j.Neo4jException;
import com.github.andyshao.neo4j.domain.Neo4jDao;
import com.github.andyshao.neo4j.domain.Neo4jSql;
import com.github.andyshao.neo4j.process.DaoProcessor;
import com.github.andyshao.reflect.ArrayOperation;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.InvocationHandler;

import java.lang.reflect.Method;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2021/7/31
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
public class CGlibDaoFactory implements DaoFactory {
    private final DaoProcessor daoProcessor;

    public CGlibDaoFactory(DaoProcessor daoProcessor) {
        this.daoProcessor = daoProcessor;
    }

    @Override
    public Object buildDao(final Neo4jDao neo4jDao) throws Neo4jException {
        final Class<?> daoClass = neo4jDao.getDaoClass();
        Enhancer enhancer = new Enhancer();
        enhancer.setClassLoader(Thread.currentThread().getContextClassLoader());
        enhancer.setSuperclass(daoClass);
        final Class<?>[] interfaces = daoClass.getInterfaces();
        if(!ArrayOperation.isEmptyOrNull(ArrayWrapper.wrap(interfaces))) enhancer.setInterfaces(interfaces);
        enhancer.setCallback(new InvocationHandler() {
            @Override
            public Object invoke(Object o, Method method, Object[] args) throws Throwable {
                final String sqlName = method.getName();
                final Neo4jSql neo4jSql = neo4jDao.findNeo4jSql(sqlName, method.getParameterTypes()).orElseThrow();
                return CGlibDaoFactory.this.daoProcessor.processing(neo4jDao, neo4jSql, args);
            }
        });
        return enhancer.create();
    }
}
