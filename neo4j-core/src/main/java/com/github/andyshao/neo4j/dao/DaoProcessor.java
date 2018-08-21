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
    <T> Optional<T> execute(DaoProcessorParam param, Class<T> retType);
    void execute(DaoProcessorParam param);
    <T> PageReturn<T> findByPage(DaoProcessorParam param, Class<T> retType);
    <T> List<T> multiRet(DaoProcessorParam param, Class<T> retType);
}
