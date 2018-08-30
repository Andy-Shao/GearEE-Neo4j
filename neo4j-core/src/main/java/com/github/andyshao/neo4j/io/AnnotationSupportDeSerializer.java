package com.github.andyshao.neo4j.io;

import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.neo4j.driver.v1.StatementResultCursor;

import com.github.andyshao.lang.NotSupportConvertException;
import com.github.andyshao.neo4j.model.SqlMethod;
import com.github.andyshao.reflect.ConstructorOperation;

import lombok.Setter;

/**
 * 
 * Title:<br>
 * Descript:<br>
 * Copyright: Copryright(c) Aug 29, 2018<br>
 * Encoding:UNIX UTF-8
 * @author Andy.Shao
 *
 */
public class AnnotationSupportDeSerializer implements DeSerializer{
    @Setter
    private DeSerializer next;
    private final ConcurrentMap<com.github.andyshao.neo4j.annotation.DeSerializer , DeSerializer> cache = new ConcurrentHashMap<>();

    @Override
    public CompletionStage<?> deSerialize(StatementResultCursor src , SqlMethod sqlMethod) throws NotSupportConvertException {
        com.github.andyshao.neo4j.annotation.DeSerializer annotation = sqlMethod.getDefinition().getAnnotation(com.github.andyshao.neo4j.annotation.DeSerializer.class);
        if(annotation == null) return next.deSerialize(src , sqlMethod); 
        DeSerializer deS = cache.computeIfAbsent(annotation , key -> ConstructorOperation.newInstance(key.value()));
        return deS.deSerialize(src , sqlMethod);
    }

}
