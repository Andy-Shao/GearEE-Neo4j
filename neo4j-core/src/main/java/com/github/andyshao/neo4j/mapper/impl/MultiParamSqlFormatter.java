package com.github.andyshao.neo4j.mapper.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.github.andyshao.lang.StringOperation;
import com.github.andyshao.neo4j.io.Serializer;
import com.github.andyshao.neo4j.mapper.SqlFormatter;
import com.github.andyshao.reflect.FieldOperation;

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
public class MultiParamSqlFormatter implements SqlFormatter {
    @Setter
    private SqlFormatter next = new DoNothingSqlFormatter();
    @Setter
    private Serializer serializer;

    @Override
    public Optional<String> format(String query , Map<String , Object> params) {
        if(params.size() < 0) return next.format(query , params);
        
        Set<String> replacements = SqlFormatter.findReplacement(query);
        String sql = query;
        for(String it : replacements) {
            List<String> keys = SqlFormatter.analysisReplacement(it);
            sql = StringOperation.replaceAll(sql , it , caculatePadding(params.get(keys.get(0)), keys, 0));
        }
        return Optional.of(sql);
    }

    private String caculatePadding(Object param , List<String> exps, int index) {
        int currentIndex = index + 1;
        if(exps.size() <= currentIndex) return serializer.serialize(param);
        return caculatePadding(FieldOperation.getValueByGetMethod(param , exps.get(currentIndex)) , exps , currentIndex);
    }
}
