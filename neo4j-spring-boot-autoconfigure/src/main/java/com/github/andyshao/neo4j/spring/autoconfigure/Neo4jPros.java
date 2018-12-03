package com.github.andyshao.neo4j.spring.autoconfigure;


import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;
import lombok.Getter;

/**
 * 
 * Title:<br>
 * Descript:<br>
 * Copyright: Copryright(c) Aug 31, 2018<br>
 * Encoding:UNIX UTF-8
 * @author Andy.Shao
 *
 */
@Getter
@ConfigurationProperties(prefix = "gear.neo4j")
public class Neo4jPros {
    private String url = "bolt://localhost:7687";
    private AuthTokenInfo authToken = new AuthTokenInfo();

    @Data
    public static class AuthTokenInfo {
        private String username = "neo4j";
        private String password = "neo4j";
        private String realm;
    }
}
