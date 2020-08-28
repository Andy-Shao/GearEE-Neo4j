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
public interface FormatterResult {
    static FormatterResult DO_NOTHING = new FormatterResult() {
        public <E> E decode(CompletionStage<ResultCursor> queryTask, Neo4jSql neo4jSql, Object... args){
            return null;
        }
    };

    <E> E decode(CompletionStage<ResultCursor> queryTask, Neo4jSql neo4jSql, Object...args);
}
