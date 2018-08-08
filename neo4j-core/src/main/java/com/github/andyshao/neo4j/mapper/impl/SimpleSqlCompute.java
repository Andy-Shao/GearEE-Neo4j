package com.github.andyshao.neo4j.mapper.impl;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.github.andyshao.lang.StringOperation;
import com.github.andyshao.neo4j.mapper.SqlCompute;
import com.github.andyshao.neo4j.model.MethodKey;
import com.github.andyshao.neo4j.model.Neo4jDaoInfo;
import com.github.andyshao.neo4j.model.SqlClipMethod;
import com.github.andyshao.neo4j.model.SqlMethod;
import com.github.andyshao.reflect.ClassOperation;
import com.github.andyshao.reflect.MethodOperation;
import com.github.andyshao.reflect.annotation.Param;

/**
 * 
 * Title:<br>
 * Descript:<br>
 * Copyright: Copryright(c) Aug 7, 2018<br>
 * Encoding:UNIX UTF-8
 * @author Andy.Shao
 *
 */
public class SimpleSqlCompute implements SqlCompute {
    private final ConcurrentMap<Class<?> , Object> instaceCache = new ConcurrentHashMap<>();

    @Override
    public String compute(MethodKey methodKey , Neo4jDaoInfo neo4jDaoInfo, Object...values) {
        SqlMethod sqlMethod = neo4jDaoInfo.getSqlMethods().get(methodKey);
        if(StringOperation.isEmptyOrNull(sqlMethod.getSql())) return sqlMethod.getSql();
        
//        final Parameter[] sqlParameters = sqlMethod.getDefinition().getParameters();
        String result = null;
        SqlClipMethod sqlClipMethod = sqlMethod.getSqlClipMethod();
        Method definition = sqlClipMethod.getDefinition();
        Object instance = instaceCache.computeIfAbsent(definition.getDeclaringClass() , key -> ClassOperation.newInstance(key));
        Parameter[] parameters = definition.getParameters();
        if(parameters.length == 0) result = MethodOperation.invoked(instance , definition);
        else if(parameters[0].getAnnotation(Param.class) == null) result = MethodOperation.invoked(instance , definition, values);
        return result;
    }
} 
