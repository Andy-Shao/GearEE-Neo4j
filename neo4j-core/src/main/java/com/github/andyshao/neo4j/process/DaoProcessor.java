package com.github.andyshao.neo4j.process;

import com.github.andyshao.neo4j.domain.Neo4jDao;
import com.github.andyshao.neo4j.domain.Neo4jSql;
import org.neo4j.driver.async.AsyncTransaction;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2020/8/27
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
public interface DaoProcessor {
    <E> E processing(Neo4jDao neo4jDao, Neo4jSql neo4jSql, AsyncTransaction transaction, Object...args);
}
