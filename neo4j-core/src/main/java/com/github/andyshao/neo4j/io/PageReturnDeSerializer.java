package com.github.andyshao.neo4j.io;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.atomic.AtomicInteger;

import org.neo4j.driver.v1.StatementResultCursor;

import com.github.andyshao.lang.NotSupportConvertException;
import com.github.andyshao.neo4j.model.PageReturn;
import com.github.andyshao.neo4j.model.Pageable;
import com.github.andyshao.neo4j.model.SqlMethod;
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
public class PageReturnDeSerializer implements DeSerializer {
    @Setter
    private DeSerializer next;

    @Override
    public CompletionStage<?> deSerialize(StatementResultCursor src , SqlMethod sqlMethod) throws NotSupportConvertException {
        if(!shouldProcess(sqlMethod)) return next.deSerialize(src , sqlMethod);
        Class<?> returnType = DeSerializers.getReturnType(sqlMethod);
        final AtomicInteger pageNum = new AtomicInteger(0);
        final AtomicInteger pageSize = new AtomicInteger(0);
        if(DeSerializers.isBaseType(returnType)) {
            return src.listAsync(record -> {
                pageNum.set(record.get(1).asInt());
                pageSize.set(record.get(2).asInt());
                return DeSerializers.formatValue(returnType , record.get(0));
            }).thenApplyAsync(records -> wrapPageReturn(pageNum , pageSize , records));
        } else {
            List<Method> setMethods = MethodOperation.getSetMethods(returnType);
            return src.listAsync(record -> {
                pageNum.set(record.get(1).asInt());
                pageSize.set(record.get(2).asInt());
                return DeSerializers.formatJavaBean(returnType , setMethods , record.get(0));
            }).thenApplyAsync(records -> wrapPageReturn(pageNum , pageSize , records));
        }
    }

    public PageReturn<Object> wrapPageReturn(final AtomicInteger pageNum , final AtomicInteger pageSize , List<Object> records) {
        PageReturn<Object> pageReturn = new PageReturn<>();
        pageReturn.setData(records);
        pageReturn.setPageNum(pageNum.get());
        pageReturn.setPageSize(pageSize.get());
        return pageReturn;
    }

    static final boolean shouldProcess(SqlMethod sqlMethod) {
        GenericNode returnTypeInfo = sqlMethod.getReturnTypeInfo();
        if(returnTypeInfo.getDeclareType().isAssignableFrom(CompletionStage.class)) {
            GenericNode node = returnTypeInfo.getComponentTypes().get(0);
            Class<?> declareType = node.getDeclareType();
            if(declareType.isAssignableFrom(PageReturn.class)) {
                for(Class<?> paramType : sqlMethod.getDefinition().getParameterTypes()) {
                    if(paramType.isAssignableFrom(Pageable.class)) return true;
                }
            }
        }
        return false;
    }
}
