package com.github.andyshao.neo4j.model;

import java.io.Serializable;
import java.util.Map;

import com.google.common.collect.Maps;

import lombok.Data;

/**
 * 
 * Title:<br>
 * Descript:<br>
 * Copyright: Copryright(c) Aug 6, 2018<br>
 * Encoding:UNIX UTF-8
 * @author Andy.Shao
 *
 */
@Data
@SuppressWarnings("serial")
public class Neo4jDaoInfo implements Serializable{
    private String name;
    private Class<?> daoClass;
    private Map<Class<?>, Class<?>> daoInterfaceMapping = Maps.newHashMap(); 
    private Map<MethodKey , SqlMethod> sqlMethods = Maps.newConcurrentMap();
}
