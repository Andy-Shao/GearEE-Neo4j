package com.github.andyshao.neo4j.mapper.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.github.andyshao.lang.StringOperation;
import com.github.andyshao.neo4j.mapper.Sql;
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

    @Override
    public Optional<Sql> format(String query , Map<String , Object> params) {
        if(params.size() < 0) return next.format(query , params);
        
        Set<String> replacements = SqlFormatter.findReplacement(query);
        Sql sql = new Sql(query);
        for(String it : replacements) {
            List<String> keys = SqlFormatter.analysisReplacement(it);
            String queryKey = it.replace('.' , '_');
            query = StringOperation.replaceAll(query , it , queryKey);
            sql.getParameters().put(queryKey.substring(1) , caculatePadding(params.get(keys.get(0)) , keys , 0));
        }
        sql.setSql(query);
        return Optional.of(sql);
    }

    @SuppressWarnings("rawtypes")
    private Object caculatePadding(Object param , List<String> exps, int index) {
        int currentIndex = index + 1;
        Object tmp = param;
        if(tmp instanceof Enum) tmp = ((Enum) tmp).name();
        if(exps.size() <= currentIndex) return tmp;
        return caculatePadding(FieldOperation.getValueByGetMethod(tmp , exps.get(currentIndex)) , exps , currentIndex);
    }
}
