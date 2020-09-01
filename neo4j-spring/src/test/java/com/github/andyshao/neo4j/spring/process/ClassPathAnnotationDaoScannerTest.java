package com.github.andyshao.neo4j.spring.process;

import com.github.andyshao.neo4j.domain.Neo4jDao;
import com.github.andyshao.neo4j.process.DaoScanner;
import org.assertj.core.api.Assertions;
import org.example.neo4j.dao.PersonDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ClassPathAnnotationDaoScannerTest {
    private DaoScanner daoScanner;

    @BeforeEach
    public void before() {
        this.daoScanner = new ClassPathAnnotationDaoScanner(PersonDao.class.getPackageName());
    }

    @Test
    void scan() {
        Map<String, Neo4jDao> neo4jDaoMap = this.daoScanner.scan();
        Assertions.assertThat(neo4jDaoMap).isNotEmpty();
        Assertions.assertThat(neo4jDaoMap.containsKey("personDao")).isTrue();
    }
}