package com.github.andyshao.neo4j.analysis;

import com.github.andyshao.neo4j.demo.dao.PersonDao;
import com.github.andyshao.neo4j.demo.dao.PersonDaoClips;
import com.github.andyshao.neo4j.model.Neo4jDao;
import com.github.andyshao.neo4j.model.Neo4jSql;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;

class Neo4jDaoAnalysisTest {

    @Test
    void analyseDao() {
        Neo4jDao neo4jDao = Neo4jDaoAnalysis.analyseDao(PersonDao.class);
        Assertions.assertThat(neo4jDao).isNotNull();
        Assertions.assertThat(neo4jDao.getEntityId()).isEqualTo("PersonDao");
        Assertions.assertThat(neo4jDao.getClipClass()).isEqualTo(PersonDaoClips.class);
        List<Neo4jSql> sqls = neo4jDao.getSqls();
        Assertions.assertThat(sqls.size()).isGreaterThan(0);
        Neo4jSql findByPk = sqls.stream()
                .filter(it -> Objects.equals(it.getDefinition().getName(), "findByPk"))
                .findFirst()
                .orElseGet(() -> null);
        Assertions.assertThat(findByPk).isNotNull();
        Assertions.assertThat(findByPk.getSqlClip()).isNull();
        Assertions.assertThat(findByPk.isUseSqlClip()).isFalse();
    }
}