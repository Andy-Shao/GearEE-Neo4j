package com.github.andyshao.neo4j.process.serializer;

import com.github.andyshao.lang.NotSupportConvertException;
import com.github.andyshao.neo4j.domain.Neo4jSql;
import org.neo4j.driver.async.ResultCursor;

import java.util.concurrent.CompletionStage;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2020/8/26
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
public interface Formatter {
    <T> CompletionStage<T> format(Neo4jSql neo4jSql, ResultCursor resultCursor, Object...args)
            throws NotSupportConvertException;
}
