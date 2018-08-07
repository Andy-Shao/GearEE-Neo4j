package com.github.andyshao.neo4j.mapper.impl;

import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.github.andyshao.neo4j.annotation.Create;
import com.github.andyshao.neo4j.annotation.Match;
import com.github.andyshao.neo4j.annotation.Neo4jDao;
import com.github.andyshao.neo4j.annotation.SqlClip;
import com.github.andyshao.neo4j.annotation.SqlClipInject;
import com.github.andyshao.neo4j.mapper.MapperScanner;
import com.github.andyshao.neo4j.model.CreateMethod;
import com.github.andyshao.neo4j.model.MatchMethod;
import com.github.andyshao.neo4j.model.MethodKey;
import com.github.andyshao.neo4j.model.Neo4jDaoInfo;
import com.github.andyshao.neo4j.model.SqlClipMethod;
import com.github.andyshao.neo4j.model.SqlClipMethodParam;
import com.github.andyshao.neo4j.model.SqlMethod;
import com.github.andyshao.reflect.MethodOperation;
import com.github.andyshao.reflect.PackageOperation;
import com.github.andyshao.reflect.ParameterOperation;
import com.github.andyshao.reflect.annotation.Param;
import com.github.andyshao.util.stream.CollectorImpl;
import com.google.common.collect.Lists;

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
    private String packagePath;
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

    @Override
    public Map<String , Neo4jDaoInfo> scan() {
        return Stream.of(PackageOperation.getPackageClasses(Package.getPackage(packagePath)))
            .filter(clazz -> clazz.getAnnotation(Neo4jDao.class) != null)
            .<Neo4jDaoInfo>map(clazz -> {
                Neo4jDaoInfo ret = new Neo4jDaoInfo();
                Neo4jDao annotation = clazz.getAnnotation(Neo4jDao.class);
                ret.setName(annotation.name());
                ret.setDaoClass(clazz);
                ret.setClipClasses(annotation.clipClasses());
                
                final Map<String, SqlClipMethod> sqlClipMethods = 
                        Stream.of(annotation.clipClasses())
                            .<List<SqlClipMethod>>map(clipClazz -> {
                                return Stream.of(MethodOperation.superGetMethods(clipClazz))
                                        .filter(method -> method.getAnnotation(SqlClip.class) != null)
                                        .<SqlClipMethod>map(method -> {
                                            SqlClip sqlClip = method.getAnnotation(SqlClip.class);
                                            SqlClipMethod sqlClipMethod = new SqlClipMethod();
                                            sqlClipMethod.setSqlClipName(sqlClip.sqlClipName());
                                            sqlClipMethod.setDefinition(method);
                                            String[] paramNames = ParameterOperation.getMethodParamNames(method);
                                            Parameter[] parameters = method.getParameters();
                                            SqlClipMethodParam[] sqlClipMethodParams = new SqlClipMethodParam[paramNames.length];
                                            sqlClipMethod.setSqlClipMethodParams(sqlClipMethodParams);
                                            for(int i=0; i<paramNames.length; i++) {
                                                SqlClipMethodParam sqlClipMethodParam = new SqlClipMethodParam();
                                                sqlClipMethodParams[i] = sqlClipMethodParam;
                                                sqlClipMethodParam.setNativeName(paramNames[i]);
                                                sqlClipMethodParam.setDefinition(parameters[i]);
                                                sqlClipMethodParam.setParam(parameters[i].getAnnotation(Param.class));
                                                sqlClipMethodParam.setSqlClipInject(parameters[i].getAnnotation(SqlClipInject.class));
                                            }
                                            return sqlClipMethod;
                                        }).collect(Collectors.toList());
                            }).collect(collector)
                            .stream()
                            .collect(Collectors.toConcurrentMap(SqlClipMethod::getSqlClipName , it -> it));
                
                
                ConcurrentMap<MethodKey , SqlMethod> sqlMethods = Stream.of(MethodOperation.superGetMethods(clazz))
                    .filter(method -> method.getAnnotation(Create.class) != null || method.getAnnotation(Match.class) != null)
                    .<SqlMethod>map(method -> {
                        Create create = method.getAnnotation(Create.class);
                        Match match = method.getAnnotation(Match.class);
                        if(create != null) {
                            CreateMethod cm = new CreateMethod();
                            cm.setDefinition(method);
                            cm.setSql(create.sql());
                            cm.setSqlClipMethod(sqlClipMethods.get(create.sqlInject().sqlClipName()));
                            return cm;
                        } else if (match != null) {
                            MatchMethod mm = new MatchMethod();
                            mm.setDefinition(method);
                            mm.setSql(match.sql());
                            mm.setSqlClipMethod(sqlClipMethods.get(match.sqlInject().sqlClipName()));
                            return mm;
                        } else return null;
                    }).collect(Collectors.toConcurrentMap(it -> new MethodKey(it.getDefinition().getName(), it.getDefinition().getParameterTypes()) , it -> it));
                ret.setSqlMethods(sqlMethods);
                
                return ret;
            }).collect(Collectors.toConcurrentMap(Neo4jDaoInfo::getName , info -> info));
    }

}
