package com.github.andyshao.neo4j.domain;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Title: <br>
 * Description: must be interface <br>
 * Copyright: Copyright(c) 2020/8/27
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
@Getter
@Setter
public class Neo4jDao implements Serializable {
    /** Unique ID */
    private String daoId;
    private Class<?> clipClass;
    private List<Neo4jSql> sqls = Lists.newArrayList();
    private Class<?> daoClass;

    public static boolean isLegalDao(Class<?> clazz) {
        com.github.andyshao.neo4j.annotation.Neo4jDao annotation =
                clazz.getAnnotation(com.github.andyshao.neo4j.annotation.Neo4jDao.class);
        return Modifier.isInterface(clazz.getModifiers()) && Objects.nonNull(annotation);
    }

    public Optional<Neo4jSql> findNeo4jSql(String sqlName, Class<?>...argTypes) {
        return this.sqls
                .stream()
                .filter(it -> {
                    Method definition = it.getDefinition();
                    boolean isSameName = Objects.equals(sqlName, definition.getName());
                    if(isSameName) {
                        Class<?>[] parameterTypes = definition.getParameterTypes();
                        return Objects.deepEquals(parameterTypes, argTypes);
                    } else return false;
                })
                .findFirst();
    }
}
