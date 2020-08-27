package com.github.andyshao.neo4j;

/**
 * 
 * Title:<br>
 * Descript:<br>
 * Copyright: Copryright(c) Aug 7, 2018<br>
 * Encoding:UNIX UTF-8
 * @author Andy.Shao
 *
 */
@SuppressWarnings("serial")
public class Neo4jException extends RuntimeException{
    public Neo4jException(Throwable ex) {
        super(ex);
    }
    
    public Neo4jException(String message) {
        super(message);
    }
    
    public Neo4jException(String message, Throwable ex) {
        super(message, ex);
    }
    
    public Neo4jException() {
    }
}
