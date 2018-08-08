package com.github.andyshao.neo4j.model;

import java.io.Serializable;

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
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("serial")
public class MatchMethod extends SqlMethod implements Serializable{
}
