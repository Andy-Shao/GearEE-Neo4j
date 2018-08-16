package com.github.andyshao.neo4j.mapper;

import java.lang.reflect.Method;

import com.github.andyshao.neo4j.model.Neo4jDaoInfo;

/**
 * 
 * Title:<br>
 * Descript:<br>
 * Copyright: Copryright(c) Aug 7, 2018<br>
 * Encoding:UNIX UTF-8
 * @author Andy.Shao
 *
 */
public interface SqlCompute {
    String compute(Method method, Neo4jDaoInfo neo4jDaoInfo, Object...values);
}
