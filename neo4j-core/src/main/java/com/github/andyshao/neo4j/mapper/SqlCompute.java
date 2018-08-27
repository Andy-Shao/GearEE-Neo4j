package com.github.andyshao.neo4j.mapper;

import java.lang.reflect.Method;
import java.util.Optional;

import com.github.andyshao.neo4j.model.Neo4jDaoInfo;
import com.github.andyshao.neo4j.model.PageReturn;
import com.github.andyshao.neo4j.model.Pageable;
import com.github.andyshao.reflect.GenericNode;
import com.github.andyshao.reflect.MethodOperation;

/**
 * 
 * Title:<br>
 * Descript:<br>
 * Copyright: Copryright(c) Aug 7, 2018<br>
 * Encoding:UNIX UTF-8
 * @author Andy.Shao
 *
 */
public interface SqlCompute {
    Optional<Sql> compute(Method method, Neo4jDaoInfo neo4jDaoInfo, Object...values);
    
    public static <T> String pageable(Pageable<T> page) {
        StringBuilder query = new StringBuilder();
        int position = (page.getPageNum() - 1) * page.getPageSize();
        if(position == 0) query.append(" LIMIT ").append(page.getPageSize());
        else {
            int skip = position - 1;
            query.append(" SKIP ").append(skip);
            query.append(" LIMIT ").append(page.getPageSize());
        }
        return query.toString();
    }
    
    public static boolean isPageReturn(Method method) {
        GenericNode returnTypeInfo = MethodOperation.getReturnTypeInfo(method);
        if(returnTypeInfo.isGeneiric()) {
            GenericNode pageReturn = returnTypeInfo.getComponentTypes().get(0);
            if(pageReturn.getDeclareType().isAssignableFrom(PageReturn.class)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean includePageable(Method method) {
        for(Class<?> type : method.getParameterTypes()) {
            if(type.isAssignableFrom(Pageable.class)) return true;
        }
        return false;
    }
}
