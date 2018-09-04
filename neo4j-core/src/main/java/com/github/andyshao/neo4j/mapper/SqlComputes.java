package com.github.andyshao.neo4j.mapper;

import com.github.andyshao.neo4j.model.PageReturn;
import com.github.andyshao.neo4j.model.Pageable;
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
public final class SqlComputes {
    private SqlComputes() {}

    public static <T> String pageable(Pageable<T> page) {
        StringBuilder query = new StringBuilder();
        int position = (page.getPageNum() - 1) * page.getPageSize();
        query.append(", ").append(page.getPageNum()).append(" AS pageNum");
        query.append(", ").append(page.getPageSize()).append(" AS pageSize");
        if(position == 0) query.append(" LIMIT ").append(page.getPageSize());
        else {
            int skip = position - 1;
            query.append(" SKIP ").append(skip);
            query.append(" LIMIT ").append(page.getPageSize());
        }
        return query.toString();
    }

    public static boolean isPageReturn(SqlMethod sqlMethod) {
        GenericNode returnTypeInfo = sqlMethod.getSqlMethodRet().getReturnTypeInfo();
        if(returnTypeInfo.isGeneiric()) {
            GenericNode pageReturn = returnTypeInfo.getComponentTypes().get(0);
            if(pageReturn.getDeclareType().isAssignableFrom(PageReturn.class)) {
                return true;
            }
        }
        return false;
    }

    public static boolean includePageable(SqlMethod sqlMethod) {
        for(Class<?> type : sqlMethod.getDefinition().getParameterTypes()) {
            if(type.isAssignableFrom(Pageable.class)) return true;
        }
        return false;
    }
}
