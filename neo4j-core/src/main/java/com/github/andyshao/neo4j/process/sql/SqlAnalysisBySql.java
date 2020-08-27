package com.github.andyshao.neo4j.process.sql;

import com.github.andyshao.lang.StringOperation;
import com.github.andyshao.neo4j.domain.Neo4jDao;
import com.github.andyshao.neo4j.domain.Neo4jSql;
import com.github.andyshao.neo4j.domain.Pageable;
import com.github.andyshao.neo4j.domain.SqlParam;
import com.github.andyshao.reflect.FieldOperation;
import com.github.andyshao.util.stream.Pair;
import com.google.common.collect.Maps;
import org.neo4j.driver.Value;
import org.neo4j.driver.Values;

import java.util.*;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2020/8/27
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
public class SqlAnalysisBySql implements SqlAnalysis {

    @Override
    public Optional<Sql> parsing(Neo4jDao neo4jDao, Neo4jSql neo4jSql, Object... args) {
        String sqlString = neo4jSql.getSql();
        final Set<String> arguments = SqlAnalysis.findArguments(sqlString);
        final Map<String, Value> valueMap = Maps.newHashMap();
        arguments.forEach(argument -> addValue(neo4jSql, valueMap, argument, args));
        Optional<Pair<Integer, SqlParam>> pageableParam = SqlAnalysis.getPageableParam(neo4jSql);
        if(pageableParam.isPresent()) {
            Integer index = pageableParam.get().getFirst();
            sqlString = sqlString + SqlAnalysis.pageable((Pageable<?>) args[index]);
        }
        Sql result = new Sql();
        result.setSql(sqlString);
        result.setParameters(valueMap);
        return Optional.of(result);
    }

    private static void addValue(Neo4jSql neo4jSql, Map<String, Value> valueMap, String argument, Object... args) {
        List<SqlParam> params = neo4jSql.getParams();
        for(int i=0; i<params.size(); i++) {
            String argumentName = getArgumentName(argument);
            SqlParam sqlParam = params.get(i);
            if(Objects.equals(sqlParam.getParamName(), argumentName)) {
                addValue(valueMap, argumentName, argument, args[i]);
                break;
            }
        }
    }

    public static String getArgumentName(String argument) {
        return splitFirst(argument).getFirst();
    }

    public static void addValue(Map<String, Value> valueMap, String key, String argument,
                                 Object arg) {
        Pair<String, String> splitFirst = splitFirst(argument);
        if(splitFirst.getSecondOps().isEmpty()) valueMap.put(key, Values.value(arg));
        else {
            String tail = splitFirst.getSecond();
            Pair<String, String> reSplitFirst = splitFirst(tail);
            String fieldName = reSplitFirst.getFirst();
            addValue(valueMap, key, reSplitFirst.getSecond(), FieldOperation.getValueByGetMethod(arg, fieldName));
        }
    }

    public static Pair<String, String> splitFirst(String argument) {
        if(StringOperation.isEmptyOrNull(argument)) return Pair.of(null, null);
        int index = argument.indexOf(SqlParam.SPLITTER);
        if(index <= 0) return Pair.of(argument, null);
        else {
            if(index == (argument.length() - 1)) return Pair.of(argument.substring(0, index), null);
            else return Pair.of(argument.substring(0, index), argument.substring(index));
        }
    }
}
