package com.github.andyshao.neo4j.process.dao;

import com.github.andyshao.neo4j.Neo4jException;
import com.github.andyshao.neo4j.domain.Neo4jDao;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2020/8/28
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
public interface DaoFactory {
    Object buildDao(Neo4jDao neo4jDao) throws Neo4jException;
}
