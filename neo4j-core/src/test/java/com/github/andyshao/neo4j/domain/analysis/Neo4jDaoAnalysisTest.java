package com.github.andyshao.neo4j.domain.analysis;

import com.github.andyshao.neo4j.demo.dao.PersonDao;
import com.github.andyshao.neo4j.demo.dao.PersonDaoClips;
import com.github.andyshao.neo4j.domain.Neo4jDao;
import com.github.andyshao.neo4j.domain.Neo4jSql;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;

class Neo4jDaoAnalysisTest {

    @Test
    void analyseDao() {
        Neo4jDao neo4jDao = Neo4jDaoAnalysis.analyseDao(PersonDao.class);
        testPersonDao(neo4jDao);
    }

    static void testPersonDao(Neo4jDao neo4jDao) {
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

    @Test
    void analyseDaoFromPackageRegex() {
        List<Neo4jDao> neo4jDaoList = Neo4jDaoAnalysis.analyseDaoFromPackageRegex("com.github.andyshao.neo4j.demo.*");
        Assertions.assertThat(neo4jDaoList.size()).isGreaterThan(0);
        Neo4jDao personDao = neo4jDaoList.stream()
                .filter(it -> Objects.equals(it.getEntityId(), "PersonDao"))
                .findFirst()
                .orElse(null);
        testPersonDao(personDao);
    }

    @Test
    void analyseDaoFromOnePackage() {
        List<Neo4jDao> neo4jDaoList = Neo4jDaoAnalysis.analyseDaoFromOnePackage(PersonDao.class.getPackage());
        Assertions.assertThat(neo4jDaoList.size()).isGreaterThan(0);
        Neo4jDao personDao = neo4jDaoList.stream()
                .filter(it -> Objects.equals(it.getEntityId(), "PersonDao"))
                .findFirst()
                .orElse(null);
        testPersonDao(personDao);
    }
}