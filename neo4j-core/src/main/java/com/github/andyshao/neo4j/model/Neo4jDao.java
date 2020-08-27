package com.github.andyshao.neo4j.model;

import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Objects;

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
    private String entityId;
    private Class<?> clipClass;
    @Setter(AccessLevel.PRIVATE)
    private List<Neo4jSql> sqls = Lists.newArrayList();

    public static boolean isLegalDao(Class<?> clazz) {
        com.github.andyshao.neo4j.annotation.Neo4jDao annotation =
                clazz.getAnnotation(com.github.andyshao.neo4j.annotation.Neo4jDao.class);
        return Modifier.isInterface(clazz.getModifiers()) && Objects.nonNull(annotation);
    }
}
