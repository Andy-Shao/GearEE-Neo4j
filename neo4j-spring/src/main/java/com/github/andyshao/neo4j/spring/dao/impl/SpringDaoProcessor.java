package com.github.andyshao.neo4j.spring.dao.impl;

import java.util.List;
import java.util.Optional;

import com.github.andyshao.neo4j.dao.DaoProcessor;
import com.github.andyshao.neo4j.dao.DaoProcessorParam;
import com.github.andyshao.neo4j.model.PageReturn;

/**
 * 
 * Title:<br>
 * Descript:<br>
 * Copyright: Copryright(c) Aug 20, 2018<br>
 * Encoding:UNIX UTF-8
 * @author Andy.Shao
 *
 */
public class SpringDaoProcessor implements DaoProcessor {

    @Override
    public <T> T process(DaoProcessorParam param , Class<T> retType) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> Optional<T> findOne(DaoProcessor param , Class<T> retType) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> PageReturn<T> findByPage(DaoProcessor param , Class<T> retType) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> List<T> findList(DaoProcessor param , Class<T> retType) {
        // TODO Auto-generated method stub
        return null;
    }

}
