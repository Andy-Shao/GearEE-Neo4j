package com.github.andyshao.neo4j.process.sql;

import com.github.andyshao.neo4j.demo.Gender;
import com.github.andyshao.neo4j.demo.Person;
import com.github.andyshao.neo4j.demo.dao.PersonDao;
import com.github.andyshao.neo4j.domain.Neo4jDao;
import com.github.andyshao.neo4j.domain.Neo4jSql;
import com.github.andyshao.neo4j.domain.analysis.Neo4jDaoAnalysis;
import com.google.common.collect.Lists;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

class SqlAnalysisBySqlClipTest {
    private static final String SAVE_BY_LIST_SQL =
            "CREATE ( n0:Person { id: $p_id0, name: $p_name0, age: $p_age0, gender: $p_gender0} ), " +
                    "( n1:Person { id: $p_id1, name: $p_name1, age: $p_age1, gender: $p_gender1} ) RETURN n0, n1 ";
    private SqlAnalysis sqlAnalysis;

    @BeforeEach
    void before() {
        this.sqlAnalysis = new SqlAnalysisBySqlClip(SqlAnalysis.DO_NOTHING);
    }

    @Test
    void parsing() {
        Neo4jDao personDao = Neo4jDaoAnalysis.analyseDao(PersonDao.class);
        Neo4jSql saveByList = personDao.getSqls()
                .stream()
                .filter(it -> Objects.equals(it.getDefinition().getName(), "saveByList"))
                .findFirst()
                .orElse(null);
        List<Person> persons = prepareData();
        Optional<Sql> sqlOpt = this.sqlAnalysis.parsing(personDao, saveByList, persons, null);
        Assertions.assertThat(sqlOpt.isPresent()).isTrue();
        Assertions.assertThat(sqlOpt.get().getSql()).isEqualTo(SAVE_BY_LIST_SQL);
        Assertions.assertThat(sqlOpt.get().getParameters().size()).isEqualTo(8);
    }

    private static List<Person> prepareData() {
        ArrayList<Person> ret = Lists.newArrayList();
        Person person = new Person();
        person.setId(UUID.randomUUID().toString());
        person.setName("Andy.Shao");
        person.setGender(Gender.FEMALE);
        person.setAge(30);
        ret.add(person);
        person = new Person();
        person.setId(UUID.randomUUID().toString());
        person.setName("Shao Wei Chuang");
        person.setGender(Gender.MALE);
        person.setAge(30);
        ret.add(person);
        return ret;
    }
}