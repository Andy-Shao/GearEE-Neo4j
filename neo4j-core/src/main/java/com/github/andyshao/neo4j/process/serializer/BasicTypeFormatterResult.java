package com.github.andyshao.neo4j.process.serializer;

import com.github.andyshao.neo4j.domain.Neo4jSql;
import org.neo4j.driver.Record;
import org.neo4j.driver.async.ResultCursor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2020/8/28
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
public class BasicTypeFormatterResult extends FormatterResultResponsibilityLink {
    public BasicTypeFormatterResult(FormatterResult formatterResult) {
        super(formatterResult);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <E> E decodeProcessing(CompletionStage<ResultCursor> queryTask, Neo4jSql neo4jSql, Object[] args) {
        Class<?> returnType = neo4jSql.getDefinition().getReturnType();
        Object result;
        final Mono<ResultCursor> processor = Mono.fromCompletionStage(queryTask);
        if(returnType.isAssignableFrom(Mono.class)) {
            if(neo4jSql.getReturnEntityType().isAssignableFrom(Void.class)) {
                result = processor.then();
            }
            else {
                result = processor.flatMap(resultCursor -> Mono.fromCompletionStage(resultCursor.listAsync()))
                    .map(records -> {
                        if(records.isEmpty()) return Mono.empty();
                        Record record = records.get(0);
                        return Deserializers.formatValue(neo4jSql.getReturnEntityType(), record.get(0));
                    });
            }
        } else if(returnType.isAssignableFrom(Flux.class)) {
            if(neo4jSql.getReturnEntityType().isAssignableFrom(Void.class)) {
                result = processor.then();
            }
            else {
                result = processor.flatMap(resultCursor -> Mono.fromCompletionStage(resultCursor.listAsync()))
                    .flux()
                    .flatMap(records -> {
                        return Flux
                                .<Record>create(fluxSink -> {
                                    records.forEach(fluxSink::next);
                                    fluxSink.complete();
                                })
                                .map(record ->
                                        Deserializers.formatValue(neo4jSql.getReturnEntityType(), record.get(0)));
                    });
            }
        } else throw new UnsupportedOperationException("Return type is not correct!");
        return (E) result;
    }

    @Override
    public boolean shouldProcess(CompletionStage<ResultCursor> queryTask, Neo4jSql neo4jSql, Object... args) {
        Class<?> returnEntityType = neo4jSql.getReturnEntityType();
        return Deserializers.isBaseType(returnEntityType);
    }
}
