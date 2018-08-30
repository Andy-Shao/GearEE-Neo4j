package com.github.andyshao.neo4j.dao;

import com.github.andyshao.neo4j.model.Neo4jDaoInfo;

/**
 * 
 * Title:<br>
 * Descript:<br>
 * Copyright: Copryright(c) Aug 20, 2018<br>
 * Encoding:UNIX UTF-8
 * @author Andy.Shao
 *
 */
public interface DaoProcessor {
    <T> T process(DaoProcessorParam param, Neo4jDaoInfo neo4jDaoInfo);
}
