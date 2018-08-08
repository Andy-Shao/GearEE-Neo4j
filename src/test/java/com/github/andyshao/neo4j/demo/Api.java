package com.github.andyshao.neo4j.demo;

import lombok.Data;
import lombok.EqualsAndHashCode;

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
@EqualsAndHashCode(callSuper = true, exclude = "others")
public class  Api extends ApiKey {
    private String others;
}
