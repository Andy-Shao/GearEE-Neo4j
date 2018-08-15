package com.github.andyshao.neo4j.model;

import java.io.Serializable;

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
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CreateMethod extends SqlMethod implements Serializable{
}
