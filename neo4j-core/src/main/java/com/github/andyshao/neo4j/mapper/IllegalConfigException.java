package com.github.andyshao.neo4j.mapper;

import com.github.andyshao.neo4j.Neo4jException;

/**
 * 
 * Title:<br>
 * Descript:<br>
 * Copyright: Copryright(c) Aug 16, 2018<br>
 * Encoding:UNIX UTF-8
 * @author Andy.Shao
 *
 */
@SuppressWarnings("serial")
public class IllegalConfigException extends Neo4jException {

    public IllegalConfigException(Throwable ex) {
        super(ex);
    }

    public IllegalConfigException(String message) {
        super(message);
    }

    public IllegalConfigException(String message , Throwable ex) {
        super(message , ex);
    }

    public IllegalConfigException() {
    }
}
