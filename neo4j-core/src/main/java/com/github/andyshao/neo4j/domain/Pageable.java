package com.github.andyshao.neo4j.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 
 * Title:<br>
 * Descript:<br>
 * Copyright: Copryright(c) Aug 6, 2018<br>
 * Encoding:UNIX UTF-8
 * @author Andy.Shao
 *
 * @param <T> Data type
 */
@Data
@SuppressWarnings("serial")
public class Pageable<T> implements Serializable{
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private T data;
}
