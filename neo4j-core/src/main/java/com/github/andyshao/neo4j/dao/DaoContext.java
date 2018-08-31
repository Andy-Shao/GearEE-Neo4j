package com.github.andyshao.neo4j.dao;

import java.util.Map;

/**
 * 
 * Title:<br>
 * Descript:<br>
 * Copyright: Copryright(c) Aug 30, 2018<br>
 * Encoding:UNIX UTF-8
 * @author Andy.Shao
 *
 */
public interface DaoContext {
    Object getDao(String daoName);
    <T> T getDao(Class<T> clazz);
    Map<String, Object> getDaos();
}
