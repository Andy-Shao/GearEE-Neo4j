package com.github.andyshao.neo4j.dao;

import java.util.List;
import java.util.Optional;

import com.github.andyshao.neo4j.model.PageReturn;

/**
 * 
 * Title:<br>
 * Descript:<br>
 * Copyright: Copryright(c) Aug 20, 2018<br>
 * Encoding:UNIX UTF-8
 * @author Andy.Shao
 *
 */
public interface DaoProcessor {
    <T> T process(DaoProcessorParam param, Class<T> retType);
    <T> Optional<T> findOne(DaoProcessor param, Class<T> retType);
    <T> PageReturn<T> findByPage(DaoProcessor param, Class<T> retType);
    <T> List<T> findList(DaoProcessor param, Class<T> retType);
}
