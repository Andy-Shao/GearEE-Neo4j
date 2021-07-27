package org.example.neo4j.service;

import org.assertj.core.api.Assertions;
import org.example.neo4j.IntegrationTest;
import org.example.neo4j.domain.Gender;
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
public class AdmirationServiceTest extends IntegrationTest {
    @Autowired
    private AdmirationService admirationService;

    @Test
    void findAdmin() {
        Mono<Person> admin = this.admirationService.findAdmin(null);
        Person person = admin.block();
        Assertions.assertThat(person).isNotNull();
        Assertions.assertThat(person.getId()).isEqualTo("ERHSBSYKAHV04SNIPHUPBDR");
    }

    @Test
    void save() {
        final Person person = new Person();
        person.setId("ERHSBSYKAHV04SNIPHUPBDR");
        person.setAge(32);
        person.setName("Weichuang.Shao");
        person.setGender(Gender.MALE);
        final Mono<Person> saved = this.admirationService.save(person, null);
        Assertions.assertThat(saved).isNotNull();
        saved.block();
    }

    @Test
    void saveOrUpdate() {
        final Person person = new Person();
        person.setId("ERHSBSYKAHV04SNIPHUPBDR");
        person.setAge(32);
        person.setName("Andy.Shao");
        person.setGender(Gender.MALE);
        final Mono<Person> saved = this.admirationService.saveOrUpdate(person, null);
        Assertions.assertThat(saved).isNotNull();
        saved.block();
    }
}
