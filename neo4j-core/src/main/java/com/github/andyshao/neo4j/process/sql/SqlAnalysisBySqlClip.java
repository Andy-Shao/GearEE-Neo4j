package com.github.andyshao.neo4j.process.sql;

import com.github.andyshao.neo4j.domain.Neo4jDao;
import com.github.andyshao.neo4j.domain.Neo4jSql;
import com.github.andyshao.neo4j.domain.Neo4jSqlClip;
import com.github.andyshao.neo4j.domain.SqlParam;
import com.github.andyshao.reflect.ClassOperation;
import com.github.andyshao.reflect.MethodOperation;
import com.github.andyshao.util.stream.Pair;
import com.google.common.collect.Maps;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2020/8/27
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
public class SqlAnalysisBySqlClip implements SqlAnalysis{
    private final SqlAnalysis sqlAnalysis;

    public SqlAnalysisBySqlClip(SqlAnalysis sqlAnalysis) {
        this.sqlAnalysis = sqlAnalysis;
    }

    @Override
    public Optional<Sql> parsing(Neo4jDao neo4jDao, Neo4jSql neo4jSql, Object... args) {
        if(!this.shouldProcess(neo4jDao, neo4jSql, args)) return this.sqlAnalysis.parsing(neo4jDao, neo4jSql, args);

        Object clipObj = ClassOperation.newInstance(neo4jDao.getClipClass());
        Neo4jSqlClip sqlClip = neo4jSql.getSqlClip();
        Method clipMethod = sqlClip.getDefinition();
        Object[] clipArgs = analyseClipArgs(neo4jSql.getParams(), sqlClip.getParams(), args);
        Class<?> clipReturnType = sqlClip.getDefinition().getReturnType();
        Sql query;
        if(clipReturnType.isAssignableFrom(String.class)) {
            String answer = MethodOperation.invoked(clipObj, clipMethod, clipArgs);
            query = new Sql();
            query.setSql(answer);
            query.setParameters(Maps.newHashMap());
        } else if (Sql.class.isAssignableFrom(clipReturnType)) {
            query = MethodOperation.invoked(clipObj, clipMethod, clipArgs);
        } else throw new UnsupportedOperationException("clipReturnType is not correct!");

        return Optional.of(query);
    }

    private static Object[] analyseClipArgs(List<SqlParam> sqlParams, List<SqlParam> sqlClipParams, Object[] args) {
        Object[] result = new Object[sqlClipParams.size()];
        final Map<String, Pair<Integer, SqlParam>> sqlParamMap = parsingSqlParams(sqlParams);
        sqlClipParams.forEach(sqlClipParam -> {
            if(sqlParamMap.containsKey(sqlClipParam.getParamName())) {
                Pair<Integer, SqlParam> sqlParamPair = sqlParamMap.get(sqlClipParam.getParamName());
                result[sqlParamPair.getFirst()] = args[sqlParamPair.getFirst()];
            }
        });
        return result;
    }

    private static Map<String, Pair<Integer, SqlParam>> parsingSqlParams(List<SqlParam> sqlParams) {
        HashMap<String, Pair<Integer, SqlParam>> result = Maps.newHashMap();
        for(int i=0; i<sqlParams.size(); i++) {
            SqlParam sqlParam = sqlParams.get(i);
            result.put(sqlParam.getParamName(), Pair.of(i, sqlParam));
        }
        return result;
    }

    @Override
    public boolean shouldProcess(Neo4jDao neo4jDao, Neo4jSql neo4jSql, Object... args) {
        return neo4jSql.isUseSqlClip();
    }
}
