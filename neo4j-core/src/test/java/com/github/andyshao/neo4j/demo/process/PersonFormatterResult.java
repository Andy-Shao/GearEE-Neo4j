package com.github.andyshao.neo4j.demo.process;

import com.github.andyshao.neo4j.demo.Person;
import com.github.andyshao.neo4j.domain.Neo4jSql;
import com.github.andyshao.neo4j.process.serializer.FormatterResult;
import org.neo4j.driver.async.ResultCursor;

import java.util.concurrent.CompletionStage;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2021/7/26
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
public class PersonFormatterResult implements FormatterResult {
    @SuppressWarnings("unchecked")
    @Override
    public <E> E decode(CompletionStage<ResultCursor> queryTask, Neo4jSql neo4jSql, Object... args) {
        return (E) new Person();
    }
}
