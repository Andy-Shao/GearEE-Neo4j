package com.github.andyshao.neo4j.process;

import com.github.andyshao.neo4j.IntegrationTest;
import com.github.andyshao.neo4j.demo.Person;
import com.github.andyshao.neo4j.demo.PersonId;
import com.github.andyshao.neo4j.demo.dao.PersonDao;
import com.github.andyshao.neo4j.domain.Neo4jDao;
import com.github.andyshao.neo4j.domain.analysis.Neo4jDaoAnalysis;
import com.github.andyshao.neo4j.driver.CreatePersonTest;
import com.github.andyshao.neo4j.process.config.DaoConfiguration;
import com.github.andyshao.neo4j.process.dao.CGlibDaoFactory;
import com.github.andyshao.neo4j.process.dao.DaoFactory;
import com.github.andyshao.neo4j.process.serializer.FormatterResult;
import com.github.andyshao.neo4j.process.sql.SqlAnalysis;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.async.AsyncSession;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Slf4j
class CGlibDaoFactoryTest extends IntegrationTest {
    private DaoFactory daoFactory;

    @BeforeEach
    void before() {
        DaoConfiguration daoConfiguration = new DaoConfiguration();
        SqlAnalysis sqlAnalysis = daoConfiguration.sqlAnalysis();
        FormatterResult formatterResult = daoConfiguration.formatterResult(daoConfiguration.entityScanner());
        this.daoFactory = new CGlibDaoFactory(daoConfiguration.daoProcessor(sqlAnalysis, formatterResult));
    }

    @Test
    void buildDao() {
        Neo4jDao neo4jDao = Neo4jDaoAnalysis.analyseDao(PersonDao.class);
        PersonDao personDao = (PersonDao) this.daoFactory.buildDao(neo4jDao);

        try(Driver driver =
                    GraphDatabase.driver(CreatePersonTest.URL,
                            AuthTokens.basic(CreatePersonTest.USER_NAME, CreatePersonTest.PASSWORD));
        ){
            AsyncSession asyncSession = driver.session(AsyncSession.class);
            PersonId personId = new PersonId();
            personId.setId("ERHSBSYKAHV04SNIPHUPBDR");
            Mono<Person> findByPk = personDao.findByPk(personId, asyncSession.beginTransactionAsync());
            findByPk
                    .doOnNext(person -> {
                        log.info(Objects.toString(person));
                        Assertions.assertThat(person.getId()).isEqualTo("ERHSBSYKAHV04SNIPHUPBDR");
                    })
                    .block();
        }
    }
}