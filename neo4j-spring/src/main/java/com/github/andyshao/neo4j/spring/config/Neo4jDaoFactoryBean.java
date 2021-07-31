package com.github.andyshao.neo4j.spring.config;

import com.github.andyshao.neo4j.domain.Neo4jDao;
import com.github.andyshao.neo4j.process.dao.DaoFactory;
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
    private Neo4jDao neo4jDao;

    @SuppressWarnings("unchecked")
    @Override
    public T getObject() throws Exception {
        return (T) this.daoFactory.buildDao(this.neo4jDao);
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
