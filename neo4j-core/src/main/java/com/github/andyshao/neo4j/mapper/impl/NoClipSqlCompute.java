package com.github.andyshao.neo4j.mapper.impl;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.github.andyshao.lang.StringOperation;
import com.github.andyshao.neo4j.mapper.Sql;
import com.github.andyshao.neo4j.mapper.SqlCompute;
import com.github.andyshao.neo4j.mapper.SqlComputes;
import com.github.andyshao.neo4j.mapper.SqlFormatter;
import com.github.andyshao.neo4j.model.MethodKey;
import com.github.andyshao.neo4j.model.Neo4jDaoInfo;
import com.github.andyshao.neo4j.model.Pageable;
import com.github.andyshao.neo4j.model.SqlMethod;
import com.github.andyshao.neo4j.model.SqlMethodParam;
import com.github.andyshao.reflect.annotation.Param;

import lombok.Setter;

/**
 * 
 * Title:<br>
 * Descript:<br>
 * Copyright: Copryright(c) Aug 17, 2018<br>
 * Encoding:UNIX UTF-8
 * @author Andy.Shao
 *
 */
public class NoClipSqlCompute implements SqlCompute {
    @Setter
    private SqlCompute next = new DoNothingSqlCompute();
    @Setter
    private SqlFormatter sqlFormatter;

    @Override
    public Optional<Sql> compute(Method method , Neo4jDaoInfo neo4jDaoInfo , Object... values) {
        final MethodKey key = new MethodKey();
        key.setMethodName(method.getName());
        key.setParameTypes(method.getParameterTypes());
        SqlMethod sqlMethod = neo4jDaoInfo.getSqlMethods().get(key);
        String sql = sqlMethod.getSql();
        if(StringOperation.isTrimEmptyOrNull(sql)) return next.compute(method , neo4jDaoInfo , values);
        
        SqlMethodParam[] sqlMethodParams = sqlMethod.getSqlMethodParams();
        Map<String , Object> params = new HashMap<>();
        Pageable<?> pageable = null;
        for(int i=0; i<sqlMethodParams.length; i++) {
            String paramKey;
            Param param = sqlMethodParams[i].getParam();
            if(param != null) paramKey = param.value();
            else paramKey = sqlMethodParams[i].getNativeName();
            Object value = values[i];
            params.put(paramKey , value);
            if(value instanceof Pageable) pageable = (Pageable<?>) value;
        }
        if(SqlComputes.isPageReturn(sqlMethod) && pageable != null) sql = sql + SqlComputes.pageable(pageable);
        return sqlFormatter.format(sql , params);
    }

}
