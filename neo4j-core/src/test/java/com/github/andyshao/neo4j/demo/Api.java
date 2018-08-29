package com.github.andyshao.neo4j.demo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

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
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class  Api extends ApiKey {
    @EqualsAndHashCode.Exclude
    private String others;
}
