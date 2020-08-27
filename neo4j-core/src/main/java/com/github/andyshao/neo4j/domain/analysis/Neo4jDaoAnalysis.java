package com.github.andyshao.neo4j.domain.analysis;

import com.github.andyshao.lang.StringOperation;
import com.github.andyshao.neo4j.domain.Neo4jDao;
import com.github.andyshao.reflect.PackageOperation;
import com.github.andyshao.util.stream.CollectorImpl;
import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2020/8/27
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
public final class Neo4jDaoAnalysis {
    public static final Neo4jDao analyseDao(Class<?> clazz) {
        if(!Neo4jDao.isLegalDao(clazz)) throw new IllegalArgumentException();
        Neo4jDao neo4jDao = new Neo4jDao();
        com.github.andyshao.neo4j.annotation.Neo4jDao annotation =
                clazz.getAnnotation(com.github.andyshao.neo4j.annotation.Neo4jDao.class);
        if(StringOperation.isEmptyOrNull(annotation.value())) neo4jDao.setEntityId(clazz.getSimpleName());
        else neo4jDao.setEntityId(annotation.value());
        if(!Objects.equals(annotation.clipClass(), Object.class)) neo4jDao.setClipClass(annotation.clipClass());
        neo4jDao.setSqls(Neo4jSqlAnalysis.analyseSqlWithCache(clazz));
        return neo4jDao;
    }

    public static final List<Neo4jDao> analyseDaoFromOnePackage(Package pkg) {
        return Arrays.stream(PackageOperation.getPackageClasses(pkg))
                .filter(Neo4jDao::isLegalDao)
                .map(Neo4jDaoAnalysis::analyseDao)
                .collect(Collectors.toList());
    }

    public static final List<Neo4jDao> analyseDaoFromPackageRegex(String regex) {
        return Arrays.stream(PackageOperation.getPackages(regex))
                .map(Neo4jDaoAnalysis::analyseDaoFromOnePackage)
                .collect(CollectorImpl.<List<Neo4jDao>, List<Neo4jDao>>idBuilder()
                        .withSupplier(Lists::newArrayList)
                        .withAccumulator((list, target) -> list.addAll(target))
                        .withCombiner((left, right) -> {
                            left.addAll(right);
                            return left;
                        })
                        .build());
    }
}
