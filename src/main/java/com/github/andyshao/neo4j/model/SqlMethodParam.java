package com.github.andyshao.neo4j.model;

import java.io.Serializable;
import java.lang.reflect.Parameter;

import com.github.andyshao.reflect.annotation.Param;

import lombok.Data;

/**
 * 
 * Title:<br>
 * Descript:<br>
 * Copyright: Copryright(c) Aug 7, 2018<br>
 * Encoding:UNIX UTF-8
 * @author Andy.Shao
 *
 */
@Data
@SuppressWarnings("serial")
public class SqlMethodParam implements Serializable{
    private Parameter definition;
    private Param param;
    private String nativeName;
}
