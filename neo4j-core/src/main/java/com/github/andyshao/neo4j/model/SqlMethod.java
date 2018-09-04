package com.github.andyshao.neo4j.model;

import java.io.Serializable;
import java.lang.reflect.Method;

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
public class SqlMethod implements Serializable{
    private String sql;
    private Method definition;
    private SqlClipMethod sqlClipMethod;
    private SqlMethodParam[] sqlMethodParams;
    private final SqlMethodRet sqlMethodRet = new SqlMethodRet();
}