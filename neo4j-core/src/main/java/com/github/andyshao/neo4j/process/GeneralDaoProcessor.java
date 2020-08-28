package com.github.andyshao.neo4j.process;

import com.github.andyshao.neo4j.Neo4jException;
import com.github.andyshao.neo4j.domain.Neo4jDao;
import com.github.andyshao.neo4j.domain.Neo4jSql;
import com.github.andyshao.neo4j.process.serializer.FormatterResult;
import com.github.andyshao.neo4j.process.sql.Sql;
import com.github.andyshao.neo4j.process.sql.SqlAnalysis;
import com.github.andyshao.reflect.ClassOperation;
import org.neo4j.driver.Values;
import org.neo4j.driver.async.AsyncTransaction;
import org.neo4j.driver.async.ResultCursor;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2020/8/27
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
public class GeneralDaoProcessor implements DaoProcessor {
    private final SqlAnalysis sqlAnalysis;
    private final FormatterResult formatterResult;

    public GeneralDaoProcessor(SqlAnalysis sqlAnalysis, FormatterResult formatterResult) {
        this.sqlAnalysis = sqlAnalysis;
        this.formatterResult = formatterResult;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E> E processing(Neo4jDao neo4jDao, Neo4jSql neo4jSql, Object... args) {
        Optional<Sql> sqlOpt = this.sqlAnalysis.parsing(neo4jDao, neo4jSql, args);
        if(sqlOpt.isEmpty()) throw new Neo4jException("Can not analysing sql!");
        Sql sql = sqlOpt.get();
        CompletionStage<AsyncTransaction> transaction = (CompletionStage<AsyncTransaction>) args[args.length - 1];
        CompletionStage<ResultCursor> queryTask =
                transaction.thenCompose(tx -> tx.runAsync(sql.getSql(), Values.value(sql.getParameters())));
        Class<? extends FormatterResult> selfDeserializerClass = neo4jSql.getDeserializer();
        if(Objects.nonNull(selfDeserializerClass)) {
            FormatterResult selfFormatterResult = ClassOperation.newInstance(selfDeserializerClass);
            return selfFormatterResult.decode(queryTask, neo4jSql, args);
        } else return this.formatterResult.decode(queryTask, neo4jSql, args);
    }
}
