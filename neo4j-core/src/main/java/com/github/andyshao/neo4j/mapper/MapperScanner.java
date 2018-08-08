package com.github.andyshao.neo4j.mapper;

import java.util.Map;

import com.github.andyshao.neo4j.model.Neo4jDaoInfo;

/**
 * 
 * Title:<br>
 * Descript:<br>
 * Copyright: Copryright(c) Aug 3, 2018<br>
 * Encoding:UNIX UTF-8
 * @author Andy.Shao
 *
 */
public interface MapperScanner {
    Map<String , Neo4jDaoInfo> scan();
}
