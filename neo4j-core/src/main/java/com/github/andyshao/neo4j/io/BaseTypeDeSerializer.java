package com.github.andyshao.neo4j.io;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import org.neo4j.driver.v1.StatementResultCursor;

import com.github.andyshao.lang.NotSupportConvertException;
import com.github.andyshao.neo4j.model.SqlMethod;
import com.github.andyshao.reflect.GenericNode;

import lombok.Setter;

/**
 * 
 * Title:<br>
 * Descript:<br>
 * Copyright: Copryright(c) Aug 28, 2018<br>
 * Encoding:UNIX UTF-8
 * @author Andy.Shao
 *
 */
public class BaseTypeDeSerializer implements DeSerializer {
    @Setter
    private DeSerializer next;

    @Override
    public CompletionStage<?> deSerialize(StatementResultCursor src , SqlMethod sqlMethod) throws NotSupportConvertException {
        GenericNode returnTypeInfo = sqlMethod.getSqlMethodRet().getReturnTypeInfo();
        if(!shouldProcess(returnTypeInfo)) return next.deSerialize(src , sqlMethod);
        Class<?> returnType = getReturnType(returnTypeInfo);
        return src.nextAsync().thenApplyAsync(record -> Optional.ofNullable(DeSerializers.formatValue(returnType , record.get(0))));
    }
    
    static final boolean shouldProcess(GenericNode node){
        List<GenericNode> nodes = node.getComponentTypes();
        if(nodes.size() != 1) return false;
        GenericNode tmp = nodes.get(0);
        Class<?> declareType = tmp.getDeclareType();
        if(!declareType.isAssignableFrom(Optional.class)) return false;
        tmp = tmp.getComponentTypes().get(0);
        declareType = tmp.getDeclareType();
        return DeSerializers.isBaseType(declareType);
    }
    
    public static final Class<?> getReturnType(GenericNode node){
        List<GenericNode> nodes = node.getComponentTypes();
        GenericNode tmp = nodes.get(0);
        Class<?> declareType = tmp.getDeclareType();
        tmp = tmp.getComponentTypes().get(0);
        declareType = tmp.getDeclareType();
        return declareType;
    }
}
