package com.github.andyshao.neo4j.spring.config;

import com.github.andyshao.neo4j.domain.analysis.Neo4jDaoAnalysis;
import com.github.andyshao.neo4j.process.DaoFactory;
import lombok.Setter;
import org.springframework.beans.factory.FactoryBean;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2020/8/28
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
@Setter
public class Neo4jDaoFactoryBean<T> implements FactoryBean<T> {
    private Class<T> daoInterface;
    private DaoFactory daoFactory;

    @SuppressWarnings("unchecked")
    @Override
    public T getObject() throws Exception {
        return (T) this.daoFactory.buildDao(Neo4jDaoAnalysis.analyseDao(this.daoInterface));
    }

    @Override
    public Class<?> getObjectType() {
        return this.daoInterface;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
