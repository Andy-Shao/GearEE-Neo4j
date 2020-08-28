package com.github.andyshao.neo4j.process.serializer;

import com.github.andyshao.neo4j.domain.Neo4jSql;
import com.github.andyshao.reflect.ClassOperation;
import org.neo4j.driver.async.ResultCursor;

import java.util.Objects;
import java.util.concurrent.CompletionStage;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2020/8/28
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
public class SelfFormatterResult extends FormatterResultResponsibilityLink {
    public SelfFormatterResult(FormatterResult formatterResult) {
        super(formatterResult);
    }

    @Override
    protected <E> E decodeProcessing(CompletionStage<ResultCursor> queryTask, Neo4jSql neo4jSql, Object[] args) {
        Class<? extends FormatterResult> formatterResultClass = neo4jSql.getDeserializer();
        if(Objects.isNull(formatterResultClass)) return null;
        FormatterResult formatterResult = ClassOperation.newInstance(formatterResultClass);
        return formatterResult.decode(queryTask, neo4jSql, args);
    }

    @Override
    public boolean shouldProcess(CompletionStage<ResultCursor> queryTask, Neo4jSql neo4jSql, Object... args) {
        return Objects.nonNull(neo4jSql.getDeserializer());
    }
}
