package com.github.andyshao.neo4j.process;

import com.github.andyshao.neo4j.Neo4jException;
import com.github.andyshao.neo4j.domain.Neo4jDao;
import com.github.andyshao.neo4j.domain.Neo4jSql;
import com.github.andyshao.neo4j.process.serializer.Formatter;
import com.github.andyshao.neo4j.process.sql.Sql;
import com.github.andyshao.neo4j.process.sql.SqlAnalysis;
import org.neo4j.driver.Values;
import org.neo4j.driver.async.AsyncTransaction;

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
    private final Formatter formatter;

    public GeneralDaoProcessor(SqlAnalysis sqlAnalysis, Formatter formatter) {
        this.sqlAnalysis = sqlAnalysis;
        this.formatter = formatter;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E> CompletionStage<E> processing(Neo4jDao neo4jDao, Neo4jSql neo4jSql, AsyncTransaction transaction,
                                             Object... args) {
        Optional<Sql> sqlOpt = this.sqlAnalysis.parsing(neo4jDao, neo4jSql, args);
        if(sqlOpt.isEmpty()) throw new Neo4jException("Can not analysing sql!");
        Sql sql = sqlOpt.get();
        return transaction.runAsync(sql.getSql(), Values.value(sql.getParameters()))
                .thenComposeAsync(resultCursor -> formatter.format(neo4jSql, resultCursor));
    }
}
