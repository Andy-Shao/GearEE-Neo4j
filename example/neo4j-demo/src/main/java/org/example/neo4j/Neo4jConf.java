package org.example.neo4j;

import java.util.Arrays;
import java.util.List;

import org.example.neo4j.dao.PersonDao;
import org.springframework.context.annotation.Configuration;

import com.github.andyshao.neo4j.spring.conf.DefaultNeo4jConf;
import com.github.andyshao.neo4j.spring.conf.Neo4jPros;
import com.github.andyshao.neo4j.spring.conf.Neo4jPros.AuthTokenInfo;

@Configuration
public class Neo4jConf extends DefaultNeo4jConf {

    @Override
    protected Neo4jPros neo4jPros() {
        return Neo4jPros.builder()
                .authToken(AuthTokenInfo.builder()
                    .password("1303595")
                    .build())
                .build();
    }

    @Override
    protected List<Package> scannerPackages() {
        return Arrays.asList(PersonDao.class.getPackage());
    }
}
