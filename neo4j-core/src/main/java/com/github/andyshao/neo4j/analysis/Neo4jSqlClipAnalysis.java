package com.github.andyshao.neo4j.analysis;

import com.github.andyshao.lang.StringOperation;
import com.github.andyshao.neo4j.Neo4jException;
import com.github.andyshao.neo4j.annotation.Neo4jDao;
import com.github.andyshao.neo4j.model.Neo4jSqlClip;
import com.github.andyshao.reflect.MethodOperation;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.time.Duration;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
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
public final class Neo4jSqlClipAnalysis {
    private static final Cache<Class<?>, Map<String, Neo4jSqlClip>> CACHE = CacheBuilder.newBuilder()
            .initialCapacity(50)
            .expireAfterWrite(Duration.ofMinutes(30))
            .build();

    public static Map<String, Neo4jSqlClip> analyseNeo4jSqlClip(Class<?> clazz) {
        Neo4jDao annotation = clazz.getAnnotation(Neo4jDao.class);
        if(Objects.isNull(annotation)) throw new IllegalArgumentException("the @Neo4jDao does not exists!");
        Class<?> clipClass = annotation.clipClass();
        if(!Neo4jSqlClip.isLegalClipClass(clipClass))
            throw new IllegalArgumentException(String.format("%s is a illegal @Neo4jSqlClip Class", clipClass.getName()));
        return Arrays.stream(MethodOperation.getAllMethods(clipClass))
                .filter(Neo4jSqlClip::isLegalMethod)
                .collect(Collectors.toMap(method -> {
                    com.github.andyshao.neo4j.annotation.Neo4jSqlClip neo4jSqlClipAnnotation =
                            method.getAnnotation(com.github.andyshao.neo4j.annotation.Neo4jSqlClip.class);
                    return neo4jSqlClipAnnotation.sqlClipName();
                }, method -> {
                    com.github.andyshao.neo4j.annotation.Neo4jSqlClip neo4jSqlClipAnnotation =
                            method.getAnnotation(com.github.andyshao.neo4j.annotation.Neo4jSqlClip.class);
                    Neo4jSqlClip result = new Neo4jSqlClip();
                    if(StringOperation.isEmptyOrNull(neo4jSqlClipAnnotation.sqlClipName()))
                        result.setSqlClipName(method.getName());
                    else result.setSqlClipName(neo4jSqlClipAnnotation.sqlClipName());
                    result.setClipClass(method.getDeclaringClass());
                    result.setDefinition(method);
                    result.setParams(SqlParamAnalysis.analyseSqlParamWithCache(method));
                    return result;
                }));
    }

    public static Map<String, Neo4jSqlClip> analyseNeo4jSqlClipWithCache(Class<?> clazz) {
        try {
            return CACHE.get(clazz, () -> analyseNeo4jSqlClip(clazz));
        } catch (ExecutionException e) {
            throw new Neo4jException(e);
        }
    }
}
