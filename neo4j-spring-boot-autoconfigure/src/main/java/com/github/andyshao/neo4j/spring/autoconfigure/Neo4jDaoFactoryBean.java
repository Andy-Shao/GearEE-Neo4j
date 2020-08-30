package com.github.andyshao.neo4j.spring.autoconfigure;

import com.github.andyshao.neo4j.process.DaoFactory;
import com.github.andyshao.neo4j.process.DaoScanner;
import org.springframework.beans.factory.FactoryBean;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2020/8/30
 * Encoding: UNIX UTF-8
 * @param <T> bean type
 *
 * @author Andy.Shao
 */
public class Neo4jDaoFactoryBean<T> implements FactoryBean<T> {
    private Class<T> daoInterface;
    private DaoFactory daoFactory;
    private DaoScanner daoScanner;

    @SuppressWarnings("unchecked")
    @Override
    public T getObject() throws Exception {
        return (T) this.daoFactory.buildDao(this.daoScanner.scan(this.daoInterface));
    }

    @Override
    public Class<?> getObjectType() {
        return this.daoInterface;
    }
}
