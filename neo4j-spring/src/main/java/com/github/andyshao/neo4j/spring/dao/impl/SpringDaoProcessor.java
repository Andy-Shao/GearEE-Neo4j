package com.github.andyshao.neo4j.spring.dao.impl;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResultCursor;
import org.neo4j.driver.v1.Transaction;

import com.github.andyshao.neo4j.Neo4jException;
import com.github.andyshao.neo4j.dao.DaoProcessor;
import com.github.andyshao.neo4j.dao.DaoProcessorParam;
import com.github.andyshao.neo4j.io.DeSerializer;
import com.github.andyshao.neo4j.mapper.Sql;
import com.github.andyshao.neo4j.mapper.SqlCompute;
import com.github.andyshao.neo4j.model.MethodKey;
import com.github.andyshao.neo4j.model.Neo4jDaoInfo;
import com.github.andyshao.neo4j.model.SqlMethod;

import lombok.RequiredArgsConstructor;

/**
 * 
 * Title:<br>
 * Descript:<br>
 * Copyright: Copryright(c) Sep 10, 2018<br>
 * Encoding:UNIX UTF-8
 * @author Andy.Shao
 *
 */
@RequiredArgsConstructor
public class SpringDaoProcessor implements DaoProcessor{
    private final SqlCompute sqlCompute;
    private final DeSerializer deSerializer;
    private final Driver driver;

    @Override
    @SuppressWarnings("unchecked")
    public <T> T process(DaoProcessorParam param, Neo4jDaoInfo neo4jDaoInfo) {
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
        @SuppressWarnings("resource")
        final Session session = tx == null ? driver.session() : null;
        final Transaction transaction = session == null ? tx : session.beginTransaction();
        CompletionStage<StatementResultCursor> runAsync = transaction.runAsync(sql.getSql(), sql.getParameters());
        if(session != null) runAsync.thenAcceptAsync(src -> transaction.commitAsync().thenAcceptAsync(v -> session.closeAsync()));
        Object obj = runAsync.thenComposeAsync(src -> deSerializer.deSerialize(src , sqlMethod));
        return (T) obj;
    }
}
