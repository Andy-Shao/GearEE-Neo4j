package com.github.andyshao.neo4j.analysis;

import com.github.andyshao.neo4j.Neo4jException;
import com.github.andyshao.neo4j.model.SqlParam;
import com.github.andyshao.reflect.MethodOperation;
import com.github.andyshao.reflect.annotation.Param;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2020/8/27
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
public final class SqlParamAnalysis {
    private static final Cache<Method, List<SqlParam>> CACHE = CacheBuilder.newBuilder()
            .initialCapacity(200)
            .expireAfterWrite(Duration.ofMinutes(30))
            .build();

    public static List<SqlParam> analyseSqlParam(Method method) {
        return Arrays.stream(method.getParameters())
                .map(parameter -> {
                    SqlParam sqlParam = new SqlParam();
                    sqlParam.setDefinition(parameter);
                    sqlParam.setNativeName(parameter.getName());
                    Param annotation = parameter.getAnnotation(Param.class);
                    sqlParam.setParam(annotation);
                    sqlParam.setReturnTypeInfo(MethodOperation.getReturnTypeInfo(method));
                    return sqlParam;
                })
                .collect(Collectors.toList());
    }

    public static List<SqlParam> analyseSqlParamWithCache(Method method) {
        try {
            return CACHE.get(method, () -> analyseSqlParam(method));
        } catch (ExecutionException e) {
            throw new Neo4jException(e);
        }
    }
}
