package com.github.andyshao.neo4j.io;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import org.neo4j.driver.v1.StatementResultCursor;

import com.github.andyshao.lang.NotSupportConvertException;
import com.github.andyshao.neo4j.model.SqlMethod;
import com.github.andyshao.reflect.ConstructorOperation;
import com.github.andyshao.reflect.GenericNode;
import com.github.andyshao.reflect.MethodOperation;

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
public class JavaBeanDeSerializer implements DeSerializer{
    @Setter
    private DeSerializer next;

    @Override
    public CompletionStage<?> deSerialize(StatementResultCursor src , SqlMethod sqlMethod) throws NotSupportConvertException {
        if(!shouldProcess(sqlMethod)) return next.deSerialize(src , sqlMethod);
        Method definition = sqlMethod.getDefinition();
        Class<?> declaringClass = definition.getDeclaringClass();
        com.github.andyshao.neo4j.annotation.DeSerializer annotation = declaringClass.getAnnotation(com.github.andyshao.neo4j.annotation.DeSerializer.class);
        if(annotation != null) return ConstructorOperation.newInstance(annotation.value()).deSerialize(src , sqlMethod);
        Class<?> returnType = DeSerializers.getReturnType(sqlMethod);
        List<Method> setMethods = MethodOperation.getSetMethods(returnType);
        return src.nextAsync().thenApplyAsync(record -> {
            if(record == null) return Optional.empty();
            return Optional.ofNullable(DeSerializers.formatJavaBean(returnType , setMethods , record.get(0)));
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
