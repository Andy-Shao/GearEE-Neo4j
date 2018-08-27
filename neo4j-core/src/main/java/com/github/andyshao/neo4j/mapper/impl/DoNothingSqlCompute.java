package com.github.andyshao.neo4j.mapper.impl;

import java.lang.reflect.Method;
import java.util.Optional;

import com.github.andyshao.neo4j.mapper.Sql;
import com.github.andyshao.neo4j.mapper.SqlCompute;
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
public class DoNothingSqlCompute implements SqlCompute {

    @Override
    public Optional<Sql> compute(Method method , Neo4jDaoInfo neo4jDaoInfo , Object... values) {
        return Optional.empty();
    }

}
