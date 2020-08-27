package com.github.andyshao.neo4j.process.sql;

import com.github.andyshao.neo4j.demo.PersonId;
import com.github.andyshao.neo4j.demo.dao.PersonDao;
import com.github.andyshao.neo4j.domain.Neo4jDao;
import com.github.andyshao.neo4j.domain.Neo4jSql;
import com.github.andyshao.neo4j.domain.analysis.Neo4jDaoAnalysis;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

class SqlAnalysisBySqlTest {
    private SqlAnalysis sqlAnalysis;

    @BeforeEach
    void before() {
        this.sqlAnalysis = new SqlAnalysisBySql();
    }

    @Test
    void parsing() {
        Neo4jDao personDao = Neo4jDaoAnalysis.analyseDao(PersonDao.class);
        Neo4jSql findByPk = personDao.getSqls()
                .stream()
                .filter(it -> Objects.equals(it.getDefinition().getName(), "findByPk"))
                .findFirst()
                .orElse(null);
        PersonId personId = new PersonId();
        personId.setId(UUID.randomUUID().toString());
        Optional<Sql> sql = this.sqlAnalysis.parsing(personDao, findByPk, personId, null);
        Assertions.assertThat(sql.isPresent()).isTrue();
        sql.ifPresent(it -> {
            Assertions.assertThat(it.getSql()).isEqualTo(PersonDao.FIND_BY_PK);
            Assertions.assertThat(it.getParameters().get("pk_id").asString()).isEqualTo(personId.getId());
        });
    }
}