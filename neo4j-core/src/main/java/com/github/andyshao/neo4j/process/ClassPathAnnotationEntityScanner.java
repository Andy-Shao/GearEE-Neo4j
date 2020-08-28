package com.github.andyshao.neo4j.process;

import com.github.andyshao.neo4j.domain.Neo4jEntity;
import com.github.andyshao.neo4j.domain.analysis.Neo4jEntityAnalysis;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2020/8/28
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
public class ClassPathAnnotationEntityScanner implements EntityScanner {
    private final String[] packageRegexes;

    public ClassPathAnnotationEntityScanner(String[] packageRegexes) {
        this.packageRegexes = packageRegexes;
    }

    @Override
    public Map<Class<?>, Neo4jEntity> scan() {
        return Arrays.stream(this.packageRegexes)
                .flatMap(regex -> Neo4jEntityAnalysis.analyseNeo4jEntity(regex).stream())
                .collect(Collectors.toMap(Neo4jEntity::getDefinition, it -> it));
    }
}
