package com.github.andyshao.neo4j.mapper;

import java.util.stream.Stream;

import com.github.andyshao.neo4j.annotation.Neo4jDao;

/**
 * 
 * Title:<br>
 * Descript:<br>
 * Copyright: Copryright(c) Aug 3, 2018<br>
 * Encoding:UNIX UTF-8
 * @author Andy.Shao
 *
 */
public final class MapperScanners {
    private MapperScanners() {}
    
    public static final boolean isMapperClass(Class<?> clazz) {
        return clazz.getAnnotation(Neo4jDao.class) != null;
    }
    
    public static final Stream<Class<?>> findMapperClass(Stream<Class<?>> stream){
        return stream.filter(clazz -> isMapperClass(clazz));
    }
}
