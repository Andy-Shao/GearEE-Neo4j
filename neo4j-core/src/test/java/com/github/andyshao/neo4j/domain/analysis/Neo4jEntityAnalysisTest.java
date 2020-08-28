package com.github.andyshao.neo4j.domain.analysis;

import com.github.andyshao.neo4j.demo.Person;
import com.github.andyshao.neo4j.domain.Neo4jEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

class Neo4jEntityAnalysisTest {

    @Test
    void analyseNeo4jEntity() {
        List<Neo4jEntity> neo4jEntities =
                Neo4jEntityAnalysis.analyseNeo4jEntity(Person.class.getPackage());
        Assertions.assertThat(neo4jEntities.size()).isGreaterThan(0);
        Optional<Neo4jEntity> personOpt = neo4jEntities.stream()
                .filter(neo4jEntity -> neo4jEntity.getDefinition().isAssignableFrom(Person.class))
                .findFirst();
        Assertions.assertThat(personOpt.isPresent()).isTrue();
    }
}