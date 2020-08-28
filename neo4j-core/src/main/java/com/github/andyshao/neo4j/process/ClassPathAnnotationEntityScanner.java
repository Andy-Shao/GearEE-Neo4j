package com.github.andyshao.neo4j.process;

import com.github.andyshao.neo4j.domain.Neo4jEntity;
import com.github.andyshao.neo4j.domain.analysis.Neo4jEntityAnalysis;

import java.util.Optional;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2020/8/28
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
public class ClassPathAnnotationEntityScanner implements EntityScanner {

    @Override
    public Optional<Neo4jEntity> scan(Class<?> entityType) {
        return Neo4jEntityAnalysis.analyseNeo4jEntity(entityType);
    }
}
