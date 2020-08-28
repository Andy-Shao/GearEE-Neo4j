package com.github.andyshao.neo4j.domain;

import com.github.andyshao.neo4j.process.serializer.FormatterResult;
import com.github.andyshao.reflect.GenericNode;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
    private List<SqlParam> params = Lists.newArrayList();
    private GenericNode returnTypeInfo;
    private Class<? extends FormatterResult> deserializer;

    public static boolean isLegalSql(Method method) {
        int modifiers = method.getModifiers();
        com.github.andyshao.neo4j.annotation.Neo4jSql annotation =
                method.getAnnotation(com.github.andyshao.neo4j.annotation.Neo4jSql.class);
        return !Modifier.isNative(modifiers) && !method.isDefault() && !Modifier.isStatic(modifiers)
                && Objects.nonNull(annotation);
    }

    public Class<?> getReturnEntityType() {
        Class<?> returnType = this.definition.getReturnType();
        if(returnType.isAssignableFrom(Flux.class) || returnType.isAssignableFrom(Mono.class)) {
            return this.returnTypeInfo.getComponentTypes().get(0).getDeclareType();
        } else throw new UnsupportedOperationException(
                String.format("Return type %s is not correct!", returnType.getName()));
    }

    public Optional<Neo4jSqlClassify> getNeo4jSqlClassify() {
        if(this.definition.getReturnType().isAssignableFrom(Mono.class)) return Optional.of(Neo4jSqlClassify.MONO);
        if(this.definition.getReturnType().isAssignableFrom(Flux.class)) return Optional.of(Neo4jSqlClassify.FLUX);
        return Optional.empty();
    }
}
