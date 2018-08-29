package com.github.andyshao.neo4j.io;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import org.neo4j.driver.v1.StatementResultCursor;
import org.neo4j.driver.v1.types.Entity;

import com.github.andyshao.lang.NotSupportConvertException;
import com.github.andyshao.lang.StringOperation;
import com.github.andyshao.neo4j.model.SqlMethod;
import com.github.andyshao.reflect.ConstructorOperation;
import com.github.andyshao.reflect.GenericNode;
import com.github.andyshao.reflect.MethodOperation;

import lombok.RequiredArgsConstructor;

/**
 * 
 * Title:<br>
 * Descript:<br>
 * Copyright: Copryright(c) Aug 29, 2018<br>
 * Encoding:UNIX UTF-8
 * @author Andy.Shao
 *
 */
@RequiredArgsConstructor
public class JavaBeanDeSerializer implements DeSerializer{
    private final DeSerializer next;

    @Override
    public CompletionStage<?> deSerialize(StatementResultCursor src , SqlMethod sqlMethod) throws NotSupportConvertException {
        if(!shouldProcess(sqlMethod)) return next.deSerialize(src , sqlMethod);
        Method definition = sqlMethod.getDefinition();
        Class<?> declaringClass = definition.getDeclaringClass();
        com.github.andyshao.neo4j.annotation.DeSerializer annotation = declaringClass.getAnnotation(com.github.andyshao.neo4j.annotation.DeSerializer.class);
        if(annotation != null) return ConstructorOperation.newInstance(annotation.value()).deSerialize(src , sqlMethod);
        Class<?> returnType = DeSerializers.getReturnType(sqlMethod);
        List<Method> setMethods = MethodOperation.getSetMethods(returnType);
        return src.singleAsync().thenApplyAsync(record -> {
            Object tmp = ConstructorOperation.newInstance(returnType);
            for(Method setMethod : setMethods) {
                String key = setMethod.getName();
                key = StringOperation.replaceFirst(key , "set" , "");
                key = key.substring(0 , 1).toLowerCase() + key.substring(1);
                Entity entity = record.get(0).asEntity();
                Object value = DeSerializers.formatValue(String.class , entity.get(key));
                if(value != null) MethodOperation.invoked(tmp , setMethod , value);
            }
            return Optional.of(tmp);
        });
    }
    
    static final boolean shouldProcess(SqlMethod sqlMethod) {
        GenericNode returnTypeInfo = sqlMethod.getReturnTypeInfo();
        if(returnTypeInfo.getDeclareType().isAssignableFrom(CompletionStage.class)) {
            GenericNode node = returnTypeInfo.getComponentTypes().get(0);
            Class<?> declareType = node.getDeclareType();
            if(declareType.isAssignableFrom(Optional.class)) {
                node = node.getComponentTypes().get(0);
                declareType = node.getDeclareType();
                return !DeSerializers.isBaseType(declareType);
            }
        }
        return false;
    }
}
