package com.github.andyshao.neo4j.mapper.impl;

import java.util.Map;
import java.util.Optional;

import com.github.andyshao.neo4j.mapper.SqlFormatter;

import lombok.Setter;

/**
 * 
 * Title:<br>
 * Descript:<br>
 * Copyright: Copryright(c) Aug 17, 2018<br>
 * Encoding:UNIX UTF-8
 * @author Andy.Shao
 *
 */
public class OneParamSqlFormatter implements SqlFormatter{
    @Setter
    private SqlFormatter next = new DoNothingSqlFormatter();

    @Override
    public Optional<String> format(String query , Map<String , Object> params) {
        if(params.size() == 1) {
          //TODO
          return Optional.empty();
        } else return next.format(query , params);
    }

}
