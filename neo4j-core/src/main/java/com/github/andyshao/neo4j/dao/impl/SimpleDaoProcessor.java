package com.github.andyshao.neo4j.dao.impl;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import org.neo4j.driver.v1.Transaction;

import com.github.andyshao.neo4j.Neo4jException;
import com.github.andyshao.neo4j.dao.DaoProcessor;
import com.github.andyshao.neo4j.dao.DaoProcessorParam;
import com.github.andyshao.neo4j.io.DeSerializer;
import com.github.andyshao.neo4j.mapper.Sql;
import com.github.andyshao.neo4j.mapper.SqlCompute;
import com.github.andyshao.neo4j.model.MethodKey;
import com.github.andyshao.neo4j.model.Neo4jDaoInfo;
import com.github.andyshao.neo4j.model.PageReturn;
import com.github.andyshao.neo4j.model.SqlMethod;

import lombok.RequiredArgsConstructor;

/**
 * 
 * Title:<br>
 * Descript:<br>
 * Copyright: Copryright(c) Aug 29, 2018<br>
 * Encoding:UNIX UTF-8
 * @author Andy.Shao
 *
 */
@RequiredArgsConstructor
public class SimpleDaoProcessor implements DaoProcessor{
    private final Neo4jDaoInfo neo4jDaoInfo;
    private final SqlCompute sqlCompute;
    private final DeSerializer deSerializer;

    @Override
    @SuppressWarnings("unchecked")
    public <T> CompletionStage<Optional<T>> execute(DaoProcessorParam param , Class<T> retType) {
        MethodKey methodKey = new MethodKey();
        methodKey.setMethodName(param.getMethodName());
        methodKey.setParameTypes(param.getArgTypes());
        SqlMethod sqlMethod = neo4jDaoInfo.getSqlMethods().get(methodKey);
        Optional<Sql> query = sqlCompute.compute(sqlMethod.getDefinition() , neo4jDaoInfo , param.getArgs());
        if(!query.isPresent()) throw new Neo4jException("No Query find out");
        Sql sql = query.get();
        Transaction tx = null;
        for(Object arg : param.getArgs()) {
            if(arg instanceof Transaction) tx = (Transaction) arg;
        }
        Object obj = tx.runAsync(sql.getSql(), sql.getParameters())
                .thenComposeAsync(src -> deSerializer.deSerialize(src , sqlMethod));
        return (CompletionStage<Optional<T>>) obj;
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
