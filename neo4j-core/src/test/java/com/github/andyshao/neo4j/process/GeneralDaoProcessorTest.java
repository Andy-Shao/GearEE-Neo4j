package com.github.andyshao.neo4j.process;

import com.github.andyshao.neo4j.IntegrationTest;
import com.github.andyshao.neo4j.demo.Person;
import com.github.andyshao.neo4j.demo.PersonId;
import com.github.andyshao.neo4j.demo.dao.PersonDao;
import com.github.andyshao.neo4j.domain.Neo4jDao;
import com.github.andyshao.neo4j.domain.Neo4jSql;
import com.github.andyshao.neo4j.domain.analysis.Neo4jDaoAnalysis;
import com.github.andyshao.neo4j.driver.CreatePersonTest;
import com.github.andyshao.neo4j.process.serializer.FormatterResult;
import com.github.andyshao.neo4j.process.serializer.JavaBeanFormatterResult;
import com.github.andyshao.neo4j.process.sql.SqlAnalysis;
import com.github.andyshao.neo4j.process.sql.SqlAnalysisBySql;
import com.github.andyshao.neo4j.process.sql.SqlAnalysisBySqlClip;
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
import java.util.Optional;

@Slf4j
class GeneralDaoProcessorTest extends IntegrationTest {
    private DaoProcessor daoProcessor;

    @BeforeEach
    void before() {
        SqlAnalysis sqlAnalysis = new SqlAnalysisBySql(SqlAnalysis.DO_NOTHING);
        sqlAnalysis = new SqlAnalysisBySqlClip(sqlAnalysis);
        EntityScanner entityScanner = new ClassPathAnnotationEntityScanner();
        FormatterResult formatterResult = new JavaBeanFormatterResult(FormatterResult.DO_NOTHING, entityScanner);
        this.daoProcessor = new GeneralDaoProcessor(sqlAnalysis, formatterResult);
    }

    @Test
    void processing() {
        Neo4jDao personDao = Neo4jDaoAnalysis.analyseDao(PersonDao.class);
        Optional<Neo4jSql> findByPkOpt = personDao.getSqls()
                .stream()
                .filter(it -> Objects.equals(it.getDefinition().getName(), "findByPk"))
                .findFirst();
        try(Driver driver =
                    GraphDatabase.driver(CreatePersonTest.URL,
                            AuthTokens.basic(CreatePersonTest.USER_NAME, CreatePersonTest.PASSWORD));
        ){
            AsyncSession asyncSession = driver.asyncSession();
            Mono<Person> result = Mono.fromCompletionStage(asyncSession.beginTransactionAsync())
                    .flatMap(tx -> {
                        if (findByPkOpt.isPresent()) {
                            PersonId personId = new PersonId();
                            personId.setId("ERHSBSYKAHV04SNIPHUPBDR");
                            return this.daoProcessor.processing(personDao, findByPkOpt.get(), tx, personId);
                        } else return Mono.empty();
                    });
            Person person = result.block();
            log.info(Objects.toString(person));
            Assertions.assertThat(person.getId()).isEqualTo("ERHSBSYKAHV04SNIPHUPBDR");
        }
    }
}