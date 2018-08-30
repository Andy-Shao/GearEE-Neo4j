package com.github.andyshao.neo4j.dao;

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
    <T> T process(DaoProcessorParam param);
}
