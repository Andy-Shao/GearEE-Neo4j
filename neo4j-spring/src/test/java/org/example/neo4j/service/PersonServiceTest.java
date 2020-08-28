package org.example.neo4j.service;

import org.assertj.core.api.Assertions;
import org.example.neo4j.IntegrationTest;
import org.example.neo4j.domain.Person;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2020/8/28
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
public class PersonServiceTest extends IntegrationTest {
    @Autowired
    private PersonService personService;

    @Test
    void findAdmin() {
        Mono<Person> admin = this.personService.findAdmin(null);
        Person person = admin.block();
        Assertions.assertThat(person).isNotNull();
        Assertions.assertThat(person.getId()).isEqualTo("ERHSBSYKAHV04SNIPHUPBDR");
    }
}
