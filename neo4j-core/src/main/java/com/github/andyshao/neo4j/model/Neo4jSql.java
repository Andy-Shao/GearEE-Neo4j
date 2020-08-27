package com.github.andyshao.neo4j.model;

import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Objects;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2020/8/27
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
@Getter
@Setter
public class Neo4jSql implements Serializable {
    /** Must be interface. */
    private Class<?> daoClass;
    private Method definition;
    private String sql;
    private boolean isUseSqlClip;
    private Neo4jSqlClip sqlClip;
    @Getter(AccessLevel.PRIVATE)
    private List<SqlParam> params = Lists.newArrayList();

    public static boolean isLegalSql(Method method) {
        int modifiers = method.getModifiers();
        com.github.andyshao.neo4j.annotation.Neo4jSql annotation =
                method.getAnnotation(com.github.andyshao.neo4j.annotation.Neo4jSql.class);
        return !Modifier.isNative(modifiers) && !method.isDefault() && !Modifier.isStatic(modifiers)
                && Objects.nonNull(annotation);
    }
}
