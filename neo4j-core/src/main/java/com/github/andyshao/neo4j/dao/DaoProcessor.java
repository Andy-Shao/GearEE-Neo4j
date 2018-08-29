package com.github.andyshao.neo4j.dao;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

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
    <T> CompletionStage<Optional<T>> execute(DaoProcessorParam param, Class<T> retType);
    CompletionStage<Void> execute(DaoProcessorParam param);
    <T> CompletionStage<PageReturn<T>> findByPage(DaoProcessorParam param, Class<T> retType);
    <T> CompletionStage<List<T>> multiRet(DaoProcessorParam param, Class<T> retType);
}
