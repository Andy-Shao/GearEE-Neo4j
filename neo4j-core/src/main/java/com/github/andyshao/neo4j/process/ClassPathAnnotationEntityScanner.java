package com.github.andyshao.neo4j.process;

import com.github.andyshao.neo4j.domain.Neo4jEntity;
import com.github.andyshao.neo4j.domain.analysis.Neo4jEntityAnalysis;
import com.google.common.collect.Maps;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
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
    private String[] packageRegexes;
    private Package[] pkgs;

    public ClassPathAnnotationEntityScanner(String[] packageRegexes) {
        this.packageRegexes = packageRegexes;
    }

    public ClassPathAnnotationEntityScanner(Package[] pkgs) {
        this.pkgs = pkgs;
    }

    @Override
    public Map<Class<?>, Neo4jEntity> scan() {
        if(Objects.nonNull(this.packageRegexes)) {
            return Arrays.stream(this.packageRegexes)
                .flatMap(regex -> Neo4jEntityAnalysis.analyseNeo4jEntity(regex).stream())
                .collect(Collectors.toMap(Neo4jEntity::getDefinition, it -> it));
        } else if(Objects.nonNull(this.pkgs)) {
            return Arrays.stream(this.pkgs)
                    .flatMap(pkg -> Neo4jEntityAnalysis.analyseNeo4jEntity(pkg).stream())
                    .collect(Collectors.toMap(Neo4jEntity::getDefinition, it -> it));
        }
        return Maps.newHashMap();
    }
}
