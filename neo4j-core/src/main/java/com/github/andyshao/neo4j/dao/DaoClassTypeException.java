package com.github.andyshao.neo4j.dao;

import com.github.andyshao.neo4j.Neo4jException;

/**
 * 
 * Title:<br>
 * Descript:<br>
 * Copyright: Copryright(c) Aug 20, 2018<br>
 * Encoding:UNIX UTF-8
 * @author Andy.Shao
 *
 */
@SuppressWarnings("serial")
public class DaoClassTypeException extends Neo4jException {

    public DaoClassTypeException(Throwable ex) {
        super(ex);
    }

    public DaoClassTypeException(String message) {
        super(message);
    }

    public DaoClassTypeException(String message , Throwable ex) {
        super(message , ex);
    }

    public DaoClassTypeException() {
    }
}
