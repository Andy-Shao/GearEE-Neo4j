package com.github.andyshao.neo4j.process.sql;

import com.github.andyshao.lang.StringOperation;
import com.github.andyshao.neo4j.domain.*;
import com.github.andyshao.neo4j.process.serializer.Serializer;
import com.github.andyshao.reflect.ClassOperation;
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
    private final SqlAnalysis sqlAnalysis;

    public SqlAnalysisBySql(SqlAnalysis sqlAnalysis) {
        this.sqlAnalysis = sqlAnalysis;
    }

    @Override
    public Optional<Sql> parsing(Neo4jDao neo4jDao, Neo4jSql neo4jSql, Object... args) {
        if(!this.shouldProcess(neo4jDao, neo4jSql, args)) return this.sqlAnalysis.parsing(neo4jDao, neo4jSql, args);

        String sqlString = neo4jSql.getSql();
        final Set<String> arguments = SqlAnalysis.findArguments(sqlString);
        final Map<String, Value> valueMap = Maps.newHashMap();
        arguments.forEach(argument -> computeValue(neo4jDao.getNeo4jEntity(), neo4jSql, valueMap, argument, args));
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

    @Override
    public boolean shouldProcess(Neo4jDao neo4jDao, Neo4jSql neo4jSql, Object... args) {
        return !neo4jSql.isUseSqlClip();
    }

    private static void computeValue(Neo4jEntity entity, Neo4jSql neo4jSql, Map<String, Value> valueMap, String argument, Object... args) {
        List<SqlParam> params = neo4jSql.getParams();
        for(int i=0; i<params.size(); i++) {
            String key = argument.substring(1);
            SqlParam sqlParam = params.get(i);
            if(Objects.equals(sqlParam.getParamName(), getArgumentName(argument))) {
                valueMap.put(key, computeValue(entity, neo4jSql, valueMap, argument, args[i]));
                break;
            }
        }
    }

    private static String getArgumentName(String argument) {
        return splitFirst(argument).getFirst().substring(1);
    }

    private static Value computeValue(Neo4jEntity neo4jEntity, Neo4jSql neo4jSql, Map<String, Value> valueMap, String argument, Object arg) {
        Pair<String, String> splitFirst = splitFirst(argument);
        if(splitFirst.getSecondOps().isEmpty()) return value(arg);
        else {
            String tail = splitFirst.getSecond();
            Pair<String, String> reSplitFirst = splitFirst(tail);
            String fieldName = reSplitFirst.getFirst();
            return computeValue(neo4jEntity, neo4jSql, valueMap, reSplitFirst.getSecond(), formatArg(neo4jEntity, arg, fieldName));
        }
    }

    @SuppressWarnings("rawtypes")
    private static Value value(Object arg) {
        Object realArg = arg;
        if(arg instanceof Enum) realArg = ((Enum) arg).name();
        return Values.value(realArg);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static Object formatArg(Neo4jEntity neo4jEntity, Object arg, final String fieldName) {
        final Object originArg = FieldOperation.getValueByGetMethod(arg, fieldName);
        return neo4jEntity.getFields()
                .stream()
                .filter(f -> Objects.equals(f.getDefinition().getName(), fieldName))
                .filter(f -> Objects.nonNull(f.getSerializer()))
                .<Object>map(f -> {
                    Serializer serializer = ClassOperation.newInstance(f.getSerializer());
                    return serializer.encode(originArg);
                })
                .findFirst()
                .orElse(originArg);
    }

    private static Pair<String, String> splitFirst(String argument) {
        if(StringOperation.isEmptyOrNull(argument)) return Pair.of(null, null);
        int index = argument.indexOf(SqlParam.SPLITTER);
        if(index <= 0) return Pair.of(argument, null);
        else {
            if(index == (argument.length() - 1)) return Pair.of(argument.substring(0, index), null);
            else return Pair.of(argument.substring(0, index), argument.substring(index + 1));
        }
    }
}
