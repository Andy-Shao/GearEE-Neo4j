package com.github.andyshao.neo4j.process;

import com.github.andyshao.neo4j.IntegrationTest;
import com.github.andyshao.neo4j.demo.Person;
import com.github.andyshao.neo4j.demo.PersonId;
import com.github.andyshao.neo4j.demo.dao.PersonDao;
import com.github.andyshao.neo4j.domain.Neo4jDao;
import com.github.andyshao.neo4j.domain.Neo4jSql;
import com.github.andyshao.neo4j.domain.analysis.Neo4jDaoAnalysis;
import com.github.andyshao.neo4j.driver.CreatePersonTest;
import com.github.andyshao.neo4j.process.config.DaoConfiguration;
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
import org.neo4j.driver.async.AsyncTransaction;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

@Slf4j
class GeneralDaoProcessorTest extends IntegrationTest {
    private DaoProcessor daoProcessor;

    @BeforeEach
    void before() {
        DaoConfiguration configuration = new DaoConfiguration();
        SqlAnalysis sqlAnalysis = configuration.sqlAnalysis();
        EntityScanner entityScanner = configuration.entityScanner();
        FormatterResult formatterResult = configuration.formatterResult(entityScanner);
        this.daoProcessor = configuration.daoProcessor(sqlAnalysis, formatterResult);
    }

    @Test
    void processing() {
        Neo4jDao personDao = Neo4jDaoAnalysis.analyseDao(PersonDao.class);
        Optional<Neo4jSql> findByPkOpt = personDao.getSqls()
                .stream()
                .filter(it -> Objects.equals(it.getDefinition().getName(), "findByPk"))
                .findFirst();
        Optional<Neo4jSql> findNameByPk = personDao.getSqls()
                .stream()
                .filter(it -> Objects.equals(it.getDefinition().getName(), "findNameByPk"))
                .findFirst();
        try(Driver driver =
                    GraphDatabase.driver(CreatePersonTest.URL,
                            AuthTokens.basic(CreatePersonTest.USER_NAME, CreatePersonTest.PASSWORD));
        ){
            AsyncSession asyncSession = driver.asyncSession();
            CompletionStage<AsyncTransaction> completionStage = asyncSession.beginTransactionAsync();
            Mono<Person> result;
            if (findByPkOpt.isPresent()) {
                PersonId personId = new PersonId();
                personId.setId("ERHSBSYKAHV04SNIPHUPBDR");
                result = this.daoProcessor.processing(personDao, findByPkOpt.get(), personId, completionStage);
            } else result = Mono.empty();
            Person person = result.block();
            log.info(Objects.toString(person));
            Assertions.assertThat(person.getId()).isEqualTo("ERHSBSYKAHV04SNIPHUPBDR");

            Mono<String> name;
            if(findNameByPk.isPresent()) {
                PersonId personId = new PersonId();
                personId.setId("ERHSBSYKAHV04SNIPHUPBDR");
                name = this.daoProcessor.processing(personDao, findNameByPk.get(), personId, completionStage);
            } else name = Mono.empty();
            String personName = name.block();
            log.info(Objects.toString(personName));
            Assertions.assertThat(personName).isEqualTo("ShaoWeiChuang");
        }
    }
}