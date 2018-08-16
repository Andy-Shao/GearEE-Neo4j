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
public class NoParamCanMatchException extends Neo4jException {

    public NoParamCanMatchException(Throwable ex) {
        super(ex);
    }

    public NoParamCanMatchException(String message) {
        super(message);
    }

    public NoParamCanMatchException(String message , Throwable ex) {
        super(message , ex);
    }

    public NoParamCanMatchException() {
    }

}
