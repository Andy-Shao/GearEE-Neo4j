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
public class SqlClipMethod implements Serializable{
    private String sqlClipName;
    private Method definition;
    private SqlClipMethodParam[] sqlClipMethodParams;
}
