package com.github.andyshao.neo4j.mapper.impl;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.github.andyshao.lang.StringOperation;
import com.github.andyshao.neo4j.mapper.IllegalConfigException;
import com.github.andyshao.neo4j.mapper.NoParamCanMatchException;
import com.github.andyshao.neo4j.mapper.SqlCompute;
import com.github.andyshao.neo4j.model.MethodKey;
import com.github.andyshao.neo4j.model.Neo4jDaoInfo;
import com.github.andyshao.neo4j.model.SqlClipMethod;
import com.github.andyshao.neo4j.model.SqlClipMethodParam;
import com.github.andyshao.neo4j.model.SqlMethod;
import com.github.andyshao.neo4j.model.SqlMethodParam;
import com.github.andyshao.reflect.ClassOperation;
import com.github.andyshao.reflect.MethodOperation;
import com.github.andyshao.reflect.annotation.Param;

/**
 * 
 * Title:<br>
 * Descript:<br>
 * Copyright: Copryright(c) Aug 16, 2018<br>
 * Encoding:UNIX UTF-8
 * @author Andy.Shao
 *
 */
public class SimpleSqlCompute implements SqlCompute{
    private final ConcurrentMap<Class<?> , Object> clipsCache = new ConcurrentHashMap<>();
    
    @Override
    public String compute(Method method , Neo4jDaoInfo neo4jDaoInfo , Object... values) {
        final MethodKey key = new MethodKey();
        key.setMethodName(method.getName());
        key.setParameTypes(method.getParameterTypes());
        SqlMethod sqlMethod = neo4jDaoInfo.getSqlMethods().get(key);
        if(!StringOperation.isTrimEmptyOrNull(sqlMethod.getSql())) {
            if(key.getParameTypes().length == 0) return sqlMethod.getSql();
            if(key.getParameTypes().length == 1);
            //TODO
        } else {
            SqlClipMethod sqlClipMethod = sqlMethod.getSqlClipMethod();
            Method clipMethod = sqlClipMethod.getDefinition();
            Class<?>[] clipArgTypes = clipMethod.getParameterTypes();
            Object clips = clipsCache.computeIfAbsent(clipMethod.getDeclaringClass(), ClassOperation::newInstance);
            if(clipArgTypes.length == 0) return MethodOperation.invoked(clips , clipMethod).toString();
            if(clipArgTypes.length == 1) {
                Class<?> argType = clipArgTypes[0];
                for(Object value : values) {
                    if(argType.isAssignableFrom(value.getClass())) 
                        return MethodOperation.invoked(clips , clipMethod , value).toString();
                }
                throw new NoParamCanMatchException(String.format("Cannot match the parameter from %s#%s to %s#%s" , 
                    method.getDeclaringClass().getName(), method.getName(), 
                    clipMethod.getDeclaringClass().getName(), clipMethod.getName()));
            } else {
                SqlClipMethodParam[] sqlClipMethodParams = sqlClipMethod.getSqlClipMethodParams();
                SqlMethodParam[] sqlMethodParams = sqlMethod.getSqlMethodParams();
                int[] index = new int[sqlClipMethodParams.length];
                for(int i=0; i<index.length; i++) {
                    Param param = sqlClipMethodParams[i].getParam();
                    if(param == null) throw new IllegalConfigException(
                        String.format("If parameter number big than one then all of them should configure @Param in %s#%s" , 
                        method.getDeclaringClass().getName(), method.getName()));
                }
                //TODO
            }
        }
        // TODO Auto-generated method stub
        return null;
    }
}
