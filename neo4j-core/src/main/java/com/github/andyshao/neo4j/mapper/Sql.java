package com.github.andyshao.neo4j.mapper;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * 
 * Title:<br>
 * Descript:<br>
 * Copyright: Copryright(c) Aug 27, 2018<br>
 * Encoding:UNIX UTF-8
 * @author Andy.Shao
 *
 */
@Data
@RequiredArgsConstructor
@SuppressWarnings("serial")
public class Sql implements Serializable{
    @NonNull
    private String sql;
    private final Map<String , Object> parameters = new HashMap<>();
}
