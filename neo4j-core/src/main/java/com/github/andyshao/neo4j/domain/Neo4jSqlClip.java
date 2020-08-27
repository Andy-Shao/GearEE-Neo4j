package com.github.andyshao.neo4j.domain;

import com.github.andyshao.neo4j.process.sql.Sql;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

/**
 * Title: <br>
 * Description: Method mus be static<br>
 * Copyright: Copyright(c) 2020/8/27
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
@Getter
@Setter
public class Neo4jSqlClip implements Serializable {
    private String sqlClipName;
    private Method definition;
    private List<SqlParam> params = Lists.newArrayList();
    private Class<?> clipClass;

    public static boolean isLegalMethod(Method method) {
        Class<?> returnType = method.getReturnType();
        return Modifier.isStatic(method.getModifiers())
                && (Sql.class.isAssignableFrom(returnType) || returnType.isAssignableFrom(String.class));
    }

    public static boolean isLegalClipClass(Class<?> clazz) {
        int modifiers = clazz.getModifiers();
        return !Modifier.isInterface(modifiers) && !Modifier.isAbstract(modifiers);
    }
}
