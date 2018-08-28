package com.github.andyshao.neo4j.io;

import java.util.concurrent.CompletionStage;

import com.github.andyshao.neo4j.mapper.IllegalConfigException;
import com.github.andyshao.neo4j.model.SqlMethod;
import com.github.andyshao.reflect.GenericNode;

/**
 * 
 * Title:<br>
 * Descript:<br>
 * Copyright: Copryright(c) Aug 28, 2018<br>
 * Encoding:UNIX UTF-8
 * @author Andy.Shao
 *
 */
public final class DeSerializers {
    private DeSerializers() {}
    
    public static final void checkReturnType(SqlMethod sqlMethod) {
        GenericNode returnTypeInfo = sqlMethod.getReturnTypeInfo();
        if(!returnTypeInfo.getDeclareType().isAssignableFrom(CompletionStage.class)) {
            throw new IllegalConfigException(String.format("the return type of %s.%s should be %s in first level" , 
                sqlMethod.getDefinition().getDeclaringClass().getName(), 
                sqlMethod.getDefinition().getName(),
                CompletionStage.class));
        }
//        GenericNode node = returnTypeInfo.getComponentTypes().get(0);
//        if(!(node.getDeclareType().isAssignableFrom(Optional.class) || node.getDeclareType().isAssignableFrom(List.class) 
//                || node.getDeclareType().isAssignableFrom(PageReturn.class))) {
//            throw new IllegalConfigException(String.format("the return type of %s.%s should be %s,%s or %s in second level" , 
//                sqlMethod.getDefinition().getDeclaringClass().getName(), 
//                sqlMethod.getDefinition().getName(),
//                Optional.class,
//                List.class,
//                PageReturn.class));
//        }
//        
//        node = node.getComponentTypes().get(0);
//        if(node.getDeclareType() == null) {
//            throw new IllegalConfigException(String.format("the return type of %s.%s should be a class definition" , 
//                sqlMethod.getDefinition().getDeclaringClass().getName(), 
//                sqlMethod.getDefinition().getName()));
//        }
    }
}
