package com.github.andyshao.neo4j.process.sql;

import com.github.andyshao.neo4j.domain.Neo4jDao;
import com.github.andyshao.neo4j.domain.Neo4jSql;
import com.github.andyshao.neo4j.domain.Pageable;
import com.github.andyshao.neo4j.domain.SqlParam;
import com.github.andyshao.util.stream.Pair;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2020/8/27
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
public interface SqlAnalysis {
    Optional<Sql> parsing(Neo4jDao neo4jDao, Neo4jSql neo4jSql, Object...args);

    static Set<String> findArguments(String query) {
        Set<String> ret = new HashSet<>();
        Pattern p = Pattern.compile("\\$[a-zA-Z\\.0-9\\_]+");
        Matcher m = p.matcher(query);
        while(m.find()) ret.add(m.group());
        return ret;
    }

    static <T> String pageable(Pageable<T> page) {
        StringBuilder query = new StringBuilder();
        int position = (page.getPageNum() - 1) * page.getPageSize();
        if(position == 0) query.append(" LIMIT ").append(page.getPageSize());
        else {
            int skip = position - 1;
            query.append(" SKIP ").append(skip);
            query.append(" LIMIT ").append(page.getPageSize());
        }
        return query.toString();
    }

    static Optional<Pair<Integer, SqlParam>> getPageableParam(Neo4jSql sqlMethod) {
        List<SqlParam> params = sqlMethod.getParams();
        for(int i = 0; i< params.size(); i++) {
            SqlParam sqlParam = params.get(i);
            if(sqlParam.getDefinition().getType().isAssignableFrom(Pageable.class)) {
                return Optional.of(Pair.of(i, sqlParam));
            }
        }
        return Optional.empty();
    }
}
