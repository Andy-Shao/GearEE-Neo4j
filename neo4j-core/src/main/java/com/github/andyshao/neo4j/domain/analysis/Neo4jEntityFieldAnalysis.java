package com.github.andyshao.neo4j.domain.analysis;

import com.github.andyshao.neo4j.annotation.Deserializer;
import com.github.andyshao.neo4j.annotation.Serializer;
import com.github.andyshao.neo4j.domain.Neo4jEntityField;
import com.github.andyshao.reflect.FieldOperation;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2020/8/28
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
public final class Neo4jEntityFieldAnalysis {
    public static final List<Neo4jEntityField> analyse(Class<?> clazz) {
        return FieldOperation.superGetAllFieldForSet(clazz)
                .stream()
                .map(field -> {
                    Neo4jEntityField entityField = new Neo4jEntityField();
                    entityField.setEntityType(clazz);
                    entityField.setDefinition(field);
                    addDeSerializer(entityField);
                    addSerializer(entityField);
                    return entityField;
                })
                .collect(Collectors.toList());
    }

    private static void addSerializer(Neo4jEntityField entityField) {
        Serializer serializer = entityField.getDefinition().getAnnotation(Serializer.class);
        if(Objects.nonNull(serializer)) entityField.setSerializer(serializer.value());
    }

    private static void addDeSerializer(Neo4jEntityField entityField) {
        Deserializer deSerializer = entityField.getDefinition().getAnnotation(Deserializer.class);
        if(Objects.nonNull(deSerializer)) entityField.setDeserializer(deSerializer.value());
    }
}
