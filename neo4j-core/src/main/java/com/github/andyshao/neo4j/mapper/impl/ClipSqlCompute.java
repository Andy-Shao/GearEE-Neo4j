package com.github.andyshao.neo4j.mapper.impl;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.github.andyshao.neo4j.mapper.IllegalConfigException;
import com.github.andyshao.neo4j.mapper.NoParamCanMatchException;
import com.github.andyshao.neo4j.mapper.Sql;
import com.github.andyshao.neo4j.mapper.SqlCompute;
import com.github.andyshao.neo4j.mapper.SqlComputes;
import com.github.andyshao.neo4j.mapper.SqlFormatter;
import com.github.andyshao.neo4j.model.MethodKey;
import com.github.andyshao.neo4j.model.Neo4jDaoInfo;
import com.github.andyshao.neo4j.model.Pageable;
import com.github.andyshao.neo4j.model.SqlClipMethod;
import com.github.andyshao.neo4j.model.SqlClipMethodParam;
import com.github.andyshao.neo4j.model.SqlMethod;
import com.github.andyshao.neo4j.model.SqlMethodParam;
import com.github.andyshao.reflect.ClassOperation;
import com.github.andyshao.reflect.MethodOperation;
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
public class ClipSqlCompute implements SqlCompute {
    @Setter
    private SqlCompute next = new DoNothingSqlCompute();
    @Setter
    private SqlFormatter sqlFormatter;
    private final ConcurrentMap<Class<?> , Object> clipsCache = new ConcurrentHashMap<>();

    @Override
    public Optional<Sql> compute(Method method , Neo4jDaoInfo neo4jDaoInfo , Object... values) {
        final MethodKey key = new MethodKey();
        key.setMethodName(method.getName());
        key.setParameTypes(method.getParameterTypes());
        SqlMethod sqlMethod = neo4jDaoInfo.getSqlMethods().get(key);
        if(sqlMethod.getSqlClipMethod() == null) return next.compute(method , neo4jDaoInfo , values);
        
        SqlClipMethod sqlClipMethod = sqlMethod.getSqlClipMethod();
        Method clipMethod = sqlClipMethod.getDefinition();
        Class<?>[] clipArgTypes = clipMethod.getParameterTypes();
        Object clips = clipsCache.computeIfAbsent(clipMethod.getDeclaringClass(), ClassOperation::newInstance);
        SqlClipMethodParam[] sqlClipMethodParams = sqlClipMethod.getSqlClipMethodParams();
        final Map<String , Object> params = new HashMap<>();
        Pageable<?> pageable = null;
        for(int i=0; i<sqlClipMethodParams.length; i++) {
            String paramKey;
            Param param = sqlClipMethodParams[i].getParam();
            if(param != null) paramKey = param.value();
            else paramKey = sqlClipMethodParams[i].getNativeName();
            Object value = values[i];
            params.put(paramKey , value);
            if(value instanceof Pageable) pageable = (Pageable<?>) value;
        }
        if(clipArgTypes.length == 0) return Optional.of(new Sql(MethodOperation.invoked(clips , clipMethod).toString()));
        if(clipArgTypes.length == 1) {
            Class<?> argType = clipArgTypes[0];
            for(Object value : values) {
                if(argType.isAssignableFrom(value.getClass())) {
                    String originSql = MethodOperation.invoked(clips , clipMethod , value).toString();
                    if(SqlComputes.isPageReturn(sqlMethod) && pageable != null) originSql = originSql + SqlComputes.pageable(pageable);
                    return sqlFormatter.format(originSql , params);
                }
            }
            throw new NoParamCanMatchException(String.format("Cannot match the parameter from %s#%s to %s#%s" , 
                method.getDeclaringClass().getName(), method.getName(), 
                clipMethod.getDeclaringClass().getName(), clipMethod.getName()));
        } else {
            SqlMethodParam[] sqlMethodParams = sqlMethod.getSqlMethodParams();
            final Object[] clipValues = new Object[sqlClipMethodParams.length];
            MATCH_PARAM:for(int i=0; i<clipValues.length; i++) {
                Param param = sqlClipMethodParams[i].getParam();
                if(param == null) throw new IllegalConfigException(
                    String.format("If parameter number big than one then all of them should configure @Param in %s#%s" , 
                        clipMethod.getDeclaringClass().getName(), clipMethod.getName()));
                Param mParam = sqlMethodParams[i].getParam();
                if(mParam == null && sqlMethodParams.length > 1) throw new IllegalConfigException(
                    String.format("If parameter number big than one then all of them should configure @Param in %s#%s", 
                        method.getDeclaringClass().getName(), method.getName()));
                if(mParam == null && sqlMethodParams.length == 1) {
                    if(Objects.equals(param.value(), sqlMethodParams[i].getNativeName())) clipValues[i] = values[0];
                }
                for(int j=0; j<sqlMethodParams.length; j++) {
                    if(Objects.equals(param.value(), mParam.value())) {
                        clipValues[i] = values[j];
                        continue MATCH_PARAM;
                    }
                }
                if(clipValues[i] == null) throw new NoParamCanMatchException(String.format("Cannot match the parameter %s in %s#%s", 
                    param.value(), clipMethod.getDeclaringClass().getName(), clipMethod.getName()));
            }
            String originSql = MethodOperation.invoked(clips, clipMethod, clipValues).toString();
            if(SqlComputes.isPageReturn(sqlMethod) && pageable != null) originSql = originSql + SqlComputes.pageable(pageable);
            return sqlFormatter.format(originSql , params);
        }
    }

}
