package com.github.andyshao.neo4j.process;

import com.github.andyshao.neo4j.domain.analysis.Neo4jDaoAnalysis;
import com.github.andyshao.neo4j.domain.Neo4jDao;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2020/8/27
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
public class ClassPathDaoScanner implements DaoScanner{
    private final String packageRegex;

    public ClassPathDaoScanner(String packageRegex) {
        this.packageRegex = packageRegex;
    }

    @Override
    public Map<String, Neo4jDao> scan() {
        return Neo4jDaoAnalysis.analyseDaoFromPackageRegex(this.packageRegex)
                .stream()
                .collect(Collectors.toMap(Neo4jDao::getEntityId, it -> it));
    }
}
