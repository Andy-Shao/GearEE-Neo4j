package com.github.andyshao.neo4j.spring.dao.impl;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

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
    public <T> CompletionStage<Optional<T>> execute(DaoProcessorParam param , Class<T> retType) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CompletionStage<Void> execute(DaoProcessorParam param) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> CompletionStage<PageReturn<T>> findByPage(DaoProcessorParam param , Class<T> retType) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> CompletionStage<List<T>> multiRet(DaoProcessorParam param , Class<T> retType) {
        // TODO Auto-generated method stub
        return null;
    }

}
