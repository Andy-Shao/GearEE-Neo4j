package com.github.andyshao.neo4j.model;

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
 * @param <T>
 */
@Data
@SuppressWarnings("serial")
public class Pageable<T extends Serializable> implements Serializable{
    private Integer pageNum = 0;
    private Integer pageSize = 10;
    private T data;
}
