package org.example.neo4j.config;

import com.github.andyshao.neo4j.spring.config.DefaultNeo4jConfiguration;
import com.github.andyshao.neo4j.spring.config.Neo4jPros;
import org.example.neo4j.dao.PersonDao;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.Set;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2020/8/28
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
@Configuration
public class Neo4jConfiguration extends DefaultNeo4jConfiguration {
    @Override
    protected Set<Package> scannerPackages() {
        return Collections.singleton(PersonDao.class.getPackage());
    }

    @Override
    protected Neo4jPros neo4jPros() {
        return Neo4jPros.builder()
                .authToken(Neo4jPros.AuthTokenInfo.builder()
                        .password("password")
                        .build())
                .build();
    }
}
