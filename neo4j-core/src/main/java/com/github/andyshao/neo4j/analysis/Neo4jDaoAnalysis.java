package com.github.andyshao.neo4j.analysis;

import com.github.andyshao.lang.StringOperation;
import com.github.andyshao.neo4j.model.Neo4jDao;

import java.util.Objects;

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
        neo4jDao.getSqls().addAll(Neo4jSqlAnalysis.analyseSqlWithCache(clazz));
        return neo4jDao;
    }
}
