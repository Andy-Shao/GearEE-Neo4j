package com.github.andyshao.neo4j.process.serializer;

import com.github.andyshao.neo4j.domain.Neo4jEntity;
import com.github.andyshao.neo4j.domain.Neo4jSql;
import com.github.andyshao.neo4j.process.EntityScanner;
import org.neo4j.driver.Record;
import org.neo4j.driver.async.ResultCursor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2020/8/28
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
public class JavaBeanFormatterResult extends FormatterResultResponsibilityLink {
    private final EntityScanner entityScanner;

    public JavaBeanFormatterResult(FormatterResult formatterResult, EntityScanner entityScanner) {
        super(formatterResult);
        this.entityScanner = entityScanner;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <E> E decodeProcessing(CompletionStage<ResultCursor> queryTask, Neo4jSql neo4jSql, Object[] args) {
        Class<?> entityType = neo4jSql.getReturnEntityType();
        Optional<Neo4jEntity> entityOpt = this.entityScanner.scan(entityType);
        if(entityOpt.isEmpty()) return null;
        final Neo4jEntity neo4jEntity = entityOpt.get();
        Object result;
        Class<?> returnType = neo4jSql.getReturnTypeInfo().getDeclareType();
        if(returnType.isAssignableFrom(Mono.class)) {
            result = Mono.fromCompletionStage(queryTask)
                    .flatMap(resultCursor -> Mono.fromCompletionStage(resultCursor.listAsync()))
                    .map(records -> {
                        Record record = records.get(0);
                        return Deserializers.formatJavaBean(entityType, neo4jEntity, record.get(0));
                    });
        } else if(returnType.isAssignableFrom(Flux.class)) {
            result = Mono.fromCompletionStage(queryTask)
                    .flatMap(resultCursor -> Mono.fromCompletionStage(resultCursor.listAsync()))
                    .flux()
                    .flatMap(records -> {
                        return Flux
                                .<Record>create(fluxSink -> {
                                    records.forEach(fluxSink::next);
                                    fluxSink.complete();
                                })
                                .map(record -> Deserializers.formatJavaBean(entityType, neo4jEntity, record.get(0)));
                    });
        } else throw new UnsupportedOperationException("Sql return type is not correct!");

        return (E) result;
    }

    @Override
    public boolean shouldProcess(CompletionStage<ResultCursor> queryTask, Neo4jSql neo4jSql, Object... args) {
        return this.entityScanner.scan(neo4jSql.getReturnEntityType()).isPresent();
    }
}
