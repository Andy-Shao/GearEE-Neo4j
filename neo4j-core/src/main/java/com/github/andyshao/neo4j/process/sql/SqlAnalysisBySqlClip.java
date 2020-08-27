package com.github.andyshao.neo4j.process.sql;

import com.github.andyshao.neo4j.domain.Neo4jDao;
import com.github.andyshao.neo4j.domain.Neo4jSql;

import java.util.Optional;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2020/8/27
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
public class SqlAnalysisBySqlClip implements SqlAnalysis{
    @Override
    public Optional<Sql> parsing(Neo4jDao neo4jDao, Neo4jSql neo4jSql, Object... args) {
        //TODO
        return Optional.empty();
    }
}
