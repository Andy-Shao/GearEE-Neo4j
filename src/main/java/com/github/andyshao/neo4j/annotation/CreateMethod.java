package com.github.andyshao.neo4j.annotation;

import java.io.Serializable;

import com.github.andyshao.neo4j.model.SqlMethod;

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
public class CreateMethod extends SqlMethod implements Serializable{
}
