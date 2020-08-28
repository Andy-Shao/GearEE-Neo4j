package com.github.andyshao.neo4j.process.serializer;

import com.github.andyshao.neo4j.domain.Neo4jSql;
import org.neo4j.driver.async.ResultCursor;

import java.util.concurrent.CompletionStage;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2020/8/28
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
public abstract class FormatterResultResponsibilityLink implements FormatterResult {
    private final FormatterResult formatterResult;

    public FormatterResultResponsibilityLink(FormatterResult formatterResult) {
        this.formatterResult = formatterResult;
    }

    @Override
    public <E> E decode(CompletionStage<ResultCursor> queryTask, Neo4jSql neo4jSql, Object... args) {
        if(!shouldProcess(queryTask, neo4jSql, args)) return this.formatterResult.decode(queryTask, neo4jSql, args);
        return decodeProcessing(queryTask, neo4jSql, args);
    }

    protected abstract <E> E decodeProcessing(CompletionStage<ResultCursor> queryTask, Neo4jSql neo4jSql, Object[] args);

    public abstract boolean shouldProcess(CompletionStage<ResultCursor> queryTask, Neo4jSql neo4jSql, Object... args);
}
