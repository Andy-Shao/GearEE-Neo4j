package com.github.andyshao.neo4j.process;

import com.github.andyshao.neo4j.domain.Neo4jDao;
import com.github.andyshao.neo4j.domain.analysis.Neo4jDaoAnalysis;

import java.util.Arrays;
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
public class ClassPathAnnotationDaoScanner implements DaoScanner{
    private final String[] packageRegexes;

    public ClassPathAnnotationDaoScanner(String... packageRegexes) {
        this.packageRegexes = packageRegexes;
    }

    @Override
    public Map<String, Neo4jDao> scan() {
        return Arrays.stream(this.packageRegexes)
                .flatMap(packageRegex -> Neo4jDaoAnalysis.analyseDaoFromPackageRegex(packageRegex).stream())
                .collect(Collectors.toMap(Neo4jDao::getEntityId, it -> it));
    }
}
