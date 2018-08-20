package com.github.andyshao.neo4j.dao;

import com.github.andyshao.neo4j.Neo4jException;
import com.github.andyshao.neo4j.model.Neo4jDaoInfo;

/**
 * 
 * Title:<br>
 * Descript:<br>
 * Copyright: Copryright(c) Aug 17, 2018<br>
 * Encoding:UNIX UTF-8
 * @author Andy.Shao
 *
 */
public interface DaoFactory {
    Object getDao(Neo4jDaoInfo info) throws Neo4jException;
}
