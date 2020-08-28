package com.github.andyshao.neo4j.demo.dao;

import com.github.andyshao.neo4j.IntegrationTest;
import com.github.andyshao.neo4j.demo.Person;
import com.github.andyshao.neo4j.demo.PersonId;
import com.github.andyshao.neo4j.domain.Neo4jDao;
import com.github.andyshao.neo4j.domain.analysis.Neo4jDaoAnalysis;
import com.github.andyshao.neo4j.driver.CreatePersonTest;
import com.github.andyshao.neo4j.process.DaoProcessor;
import com.github.andyshao.neo4j.process.config.DaoConfiguration;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.async.AsyncSession;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2020/8/28
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
public class PersonDaoImplTest extends IntegrationTest {
    private PersonDao personDao;

    @BeforeEach
    void before() {
        DaoConfiguration configuration = new DaoConfiguration();
        Neo4jDao neo4jDao = Neo4jDaoAnalysis.analyseDao(PersonDao.class);
        DaoProcessor daoProcessor =
                configuration.daoProcessor(
                        configuration.sqlAnalysis(),
                        configuration.formatterResult(configuration.entityScanner()));
        personDao = new PersonDaoImpl(daoProcessor, neo4jDao);
    }

    @Test
    void findByPk() {
        try(Driver driver =
                    GraphDatabase.driver(CreatePersonTest.URL,
                            AuthTokens.basic(CreatePersonTest.USER_NAME, CreatePersonTest.PASSWORD));
        ){
            AsyncSession asyncSession = driver.asyncSession();
            PersonId id = new PersonId();
            id.setId("ERHSBSYKAHV04SNIPHUPBDR");
            Person person = this.personDao.findByPk(id, asyncSession.beginTransactionAsync())
                    .block();
            Assertions.assertThat(person).isNotNull();
        }
    }
}
