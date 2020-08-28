package com.github.andyshao.neo4j.domain;

import com.github.andyshao.neo4j.process.serializer.DeSerializer;
import com.github.andyshao.neo4j.process.serializer.Serializer;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2020/8/28
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
@Getter
@Setter
public class Neo4jEntityField {
    private Class<?> entityType;
    private Field definition;
    @SuppressWarnings("rawtypes")
    private Class<? extends Serializer> serializer;
    @SuppressWarnings("rawtypes")
    private Class<? extends DeSerializer> deSerializer;
}
