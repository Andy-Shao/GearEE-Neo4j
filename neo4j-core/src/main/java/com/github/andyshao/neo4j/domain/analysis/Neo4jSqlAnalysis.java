package com.github.andyshao.neo4j.domain.analysis;

import com.github.andyshao.lang.StringOperation;
import com.github.andyshao.neo4j.Neo4jException;
import com.github.andyshao.neo4j.annotation.FormatterResult;
import com.github.andyshao.neo4j.domain.Neo4jSql;
import com.github.andyshao.neo4j.domain.Neo4jSqlClip;
import com.github.andyshao.reflect.MethodOperation;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
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
public final class Neo4jSqlAnalysis {
    private static final Cache<Class<?>, List<Neo4jSql>> CACHE = CacheBuilder.newBuilder()
            .expireAfterWrite(Duration.ofMinutes(30))
            .initialCapacity(50)
            .build();

    public static List<Neo4jSql> analyseSqlWithCache(Class<?> clazz) {
        try {
            return CACHE.get(clazz, () -> analyseSql(clazz));
        } catch (ExecutionException e) {
            throw new Neo4jException(e);
        }
    }

    public static List<Neo4jSql> analyseSql(Class<?> clazz) {
        return Arrays.stream(MethodOperation.getAllMethods(clazz))
                .filter(Neo4jSql::isLegalSql)
                .map(Neo4jSqlAnalysis::analyseSql)
                .collect(Collectors.toList());
    }

    private static Neo4jSql analyseSql(Method method) {
        com.github.andyshao.neo4j.annotation.Neo4jSql annotation =
                method.getAnnotation(com.github.andyshao.neo4j.annotation.Neo4jSql.class);
        Neo4jSql neo4jSql = new Neo4jSql();
        if(!StringOperation.isEmptyOrNull(annotation.sql())) neo4jSql.setSql(annotation.sql());
        neo4jSql.setUseSqlClip(annotation.isUseSqlClip());
        if(annotation.isUseSqlClip()) {
            String sqlClipName = annotation.sqlClipName();
            if(StringOperation.isEmptyOrNull(sqlClipName)) sqlClipName = method.getName();
            Map<String, Neo4jSqlClip> sqlClipMap =
                    Neo4jSqlClipAnalysis.analyseNeo4jSqlClipWithCache(method.getDeclaringClass());
            Neo4jSqlClip sqlClip = sqlClipMap.get(sqlClipName);
            if(Objects.isNull(sqlClip))
                throw new Neo4jException(String.format("%s Neo4jSqlClip does not exists!", annotation.sqlClipName()));
            neo4jSql.setSqlClip(sqlClip);
        }
        neo4jSql.setDaoClass(method.getDeclaringClass());
        neo4jSql.setDefinition(method);
        neo4jSql.setParams(SqlParamAnalysis.analyseSqlParamWithCache(method));
        neo4jSql.setReturnTypeInfo(MethodOperation.getReturnTypeInfoByNative(method));
        FormatterResult formatterResult = method.getAnnotation(FormatterResult.class);
        if(Objects.nonNull(formatterResult)) neo4jSql.setDeserializer(formatterResult.value());
        return neo4jSql;
    }
}
