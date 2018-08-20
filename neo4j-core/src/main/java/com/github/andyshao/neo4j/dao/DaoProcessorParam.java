package com.github.andyshao.neo4j.dao;

import java.io.Serializable;

import lombok.Data;

/**
 * 
 * Title:<br>
 * Descript:<br>
 * Copyright: Copryright(c) Aug 20, 2018<br>
 * Encoding:UNIX UTF-8
 * @author Andy.Shao
 *
 */
@Data
@SuppressWarnings("serial")
public class DaoProcessorParam implements Serializable{
    private Class<?> targetClass;
    private String methodName;
    private Class<?>[] argTypes;
    private String daoName;
    private Object[] args;
}
