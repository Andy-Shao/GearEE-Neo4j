package com.github.andyshao.neo4j.demo;

import java.io.Serializable;

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
public class ApiKey implements Serializable {
    private String systemAlias;
    private String apiName;
}
