package com.github.andyshao.neo4j.mapper.impl;

import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.github.andyshao.lang.StringOperation;
import com.github.andyshao.neo4j.annotation.Create;
import com.github.andyshao.neo4j.annotation.Match;
import com.github.andyshao.neo4j.annotation.Neo4jDao;
import com.github.andyshao.neo4j.annotation.SqlClip;
import com.github.andyshao.neo4j.mapper.IllegalConfigException;
import com.github.andyshao.neo4j.mapper.MapperScanner;
import com.github.andyshao.neo4j.model.CreateMethod;
import com.github.andyshao.neo4j.model.MatchMethod;
import com.github.andyshao.neo4j.model.MethodKey;
import com.github.andyshao.neo4j.model.Neo4jDaoInfo;
import com.github.andyshao.neo4j.model.SqlClipMethod;
import com.github.andyshao.neo4j.model.SqlClipMethodParam;
import com.github.andyshao.neo4j.model.SqlMethod;
import com.github.andyshao.neo4j.model.SqlMethodParam;
import com.github.andyshao.reflect.GenericNode;
import com.github.andyshao.reflect.MethodOperation;
import com.github.andyshao.reflect.PackageOperation;
import com.github.andyshao.reflect.ParameterOperation;
import com.github.andyshao.reflect.annotation.Param;
import com.github.andyshao.util.stream.CollectorImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import lombok.Setter;

/**
 * 
 * Title:<br>
 * Descript:<br>
 * Copyright: Copryright(c) Aug 6, 2018<br>
 * Encoding:UNIX UTF-8
 * @author Andy.Shao
 *
 */
public class PackageMapperScanner implements MapperScanner{
    @Setter
    private Package packagePath;
    private static final CollectorImpl<List<SqlClipMethod> , List<SqlClipMethod> , List<SqlClipMethod>> collector =
            new CollectorImpl.Builder<List<SqlClipMethod> , List<SqlClipMethod> , List<SqlClipMethod>>()
                .withSupplier(Lists::newArrayList)
                .withAccumulator((supplier, origin) -> supplier.addAll(origin))
                .withCombiner((left, right) -> {
                    left.addAll(right);
                    return left;
                }).withFinisher(it -> it)
                .withCharacteristics(CollectorImpl.CH_CONCURRENT_ID)
                .build();

    static final void parse(Class<?> clazz, Map<Class<?> , Class<?>> daoInterfaceMapping) {
        if(!clazz.isInterface()) throw new IllegalArgumentException("Class type should be interface");
        
        Neo4jDao neo4jDao = clazz.getAnnotation(Neo4jDao.class);
        if(neo4jDao == null) return;
        
        Arrays.stream(clazz.getInterfaces()).forEach(it -> parse(it, daoInterfaceMapping));
        daoInterfaceMapping.put(neo4jDao.clipClass(), clazz);
    }
    
    @Override
    public Map<String , Neo4jDaoInfo> scan() {
        return Stream.of(PackageOperation.getPackageClasses(packagePath))
            .filter(clazz -> clazz.isInterface())
            .filter(clazz -> clazz.getAnnotation(Neo4jDao.class) != null)
            .map(clazz -> {
                Neo4jDaoInfo result = new Neo4jDaoInfo();
                result.setDaoClass(clazz);
                Neo4jDao neo4jDao = clazz.getAnnotation(Neo4jDao.class);
                if(neo4jDao.value().isEmpty()) result.setName(clazz.getSimpleName());
                else result.setName(neo4jDao.value());
                HashMap<Class<?> , Class<?>> daoInterfaceMapping = Maps.newHashMap();
                result.setDaoInterfaceMapping(daoInterfaceMapping);
                parse(clazz , daoInterfaceMapping);
                
                final Map<String, SqlClipMethod> sqlClipMethods = daoInterfaceMapping.values().stream()
                    .filter(it -> {
                        Neo4jDao anno = it.getAnnotation(Neo4jDao.class);
                        if(anno == null) return false;
                        if(anno.clipClass() != null) return true;
                        return false;
                    })
                    .<Class<?>>map(it -> it.getAnnotation(Neo4jDao.class).clipClass())
                    .map(it -> {
                        
                        return Stream.of(it.getMethods())
                            .filter(method -> method.getAnnotation(SqlClip.class) != null)
                            .<SqlClipMethod>map(method -> {
                                SqlClipMethod sqlClipMethod = new SqlClipMethod();
                                sqlClipMethod.setDefinition(method);
                                SqlClip sqlClip = method.getAnnotation(SqlClip.class);
                                sqlClipMethod.setSqlClipName(sqlClip.sqlClipName().isEmpty() ? method.getName() : sqlClip.sqlClipName());
                                
                                String[] paramNames = ParameterOperation.getMethodParamNamesByReflect(method);
                                Parameter[] parameters = method.getParameters();
                                SqlClipMethodParam[] sqlClipMethodParams = new SqlClipMethodParam[paramNames.length];
                                for(int i=0; i<paramNames.length; i++) {
                                    SqlClipMethodParam param = new SqlClipMethodParam();
                                    sqlClipMethodParams[i] = param;
                                    param.setDefinition(parameters[i]);
                                    param.setNativeName(paramNames[i]);
                                    param.setParam(parameters[i].getAnnotation(Param.class));
                                }
                                sqlClipMethod.setSqlClipMethodParams(sqlClipMethodParams);
                                return sqlClipMethod;
                            }).collect(Collectors.toList());
                    }).collect(collector)
                    .stream()
                    .collect(Collectors.toMap(SqlClipMethod::getSqlClipName , it -> it, (left, right) -> right));
                
                ConcurrentMap<MethodKey , SqlMethod> sqlMethods = Arrays.stream(clazz.getMethods())
                    .filter(it -> it.getAnnotation(Create.class) != null || it.getAnnotation(Match.class) != null)
                    .map(method -> {
                        Create create = method.getAnnotation(Create.class);
                        Match match = method.getAnnotation(Match.class);
                        SqlMethod sqlMethod = null;
                        if(create != null) {
                            CreateMethod cm = new CreateMethod();
                            cm.setDefinition(method);
                            cm.setSql(create.sql());
                            final String sqlClipName = create.sqlInject().sqlClipName();
                            if(StringOperation.isTrimEmptyOrNull(create.sql())) {
                                cm.setSqlClipMethod(sqlClipMethods.get(sqlClipName.isEmpty() ? method.getName() : sqlClipName));
                            } else cm.setSqlClipMethod(sqlClipMethods.get(sqlClipName));
                            sqlMethod = cm;
                            if(!StringOperation.isTrimEmptyOrNull(create.sql()) && !StringOperation.isTrimEmptyOrNull(sqlClipName))
                                throw new IllegalConfigException(String.format("Both of sql & sqlInject have value in %s#%s @Create configuration" , 
                                    method.getDeclaringClass(), method.getName()));
                        } else {
                            MatchMethod mm = new MatchMethod();
                            mm.setDefinition(method);
                            mm.setSql(match.sql());
                            String sqlClipName = match.sqlInject().sqlClipName();
                            if(StringOperation.isTrimEmptyOrNull(match.sql())) {
                                sqlClipName = sqlClipName.isEmpty() ? method.getName() : sqlClipName;
                                mm.setSqlClipMethod(sqlClipMethods.get(sqlClipName));
                            }else mm.setSqlClipMethod(sqlClipMethods.get(sqlClipName));
                            sqlMethod = mm;
                            if(!StringOperation.isTrimEmptyOrNull(match.sql()) && !StringOperation.isTrimEmptyOrNull(sqlClipName))
                                throw new IllegalConfigException(String.format("Both of sql & sqlInject have value in %s#%s @Match configuration" , 
                                    method.getDeclaringClass(), method.getName()));
                        }
                        
                        sqlMethod.getSqlMethodRet().setReturnTypeInfo(MethodOperation.getReturnTypeInfo(method));
                        
                        List<GenericNode> parameterTypesInfo = MethodOperation.getParameterTypesInfo(method);
                        String[] paramNames = ParameterOperation.getMethodParamNamesByReflect(method);
                        Parameter[] parameters = method.getParameters();
                        SqlMethodParam[] sqlMethodParams = new SqlMethodParam[paramNames.length];
                        for(int i=0; i<paramNames.length; i++) {
                            SqlMethodParam item = new SqlMethodParam();
                            sqlMethodParams[i] = item;
                            item.setNativeName(paramNames[i]);
                            item.setDefinition(parameters[i]);
                            item.setParam(parameters[i].getAnnotation(Param.class));
                            item.setTypeInfo(parameterTypesInfo.get(i));
                        }
                        sqlMethod.setSqlMethodParams(sqlMethodParams);
                        return sqlMethod;
                    }).collect(Collectors.toConcurrentMap(it -> new MethodKey(it.getDefinition().getName(), 
                      it.getDefinition().getParameterTypes()) , it -> it));
                result.setSqlMethods(sqlMethods);
                return result;
            }).collect(Collectors.toMap(Neo4jDaoInfo::getName , it -> it, (left, right) -> right));
    }

}
