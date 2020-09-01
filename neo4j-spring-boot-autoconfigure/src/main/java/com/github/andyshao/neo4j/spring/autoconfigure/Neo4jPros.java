package com.github.andyshao.neo4j.spring.autoconfigure;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

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
@Setter
@ConfigurationProperties(prefix = "gear.neo4j")
public class Neo4jPros {
    private String url = "neo4j://localhost:7687";
    private AuthTokenInfo authToken = new AuthTokenInfo();

    @Getter
    @Setter
    public static class AuthTokenInfo {
        private String username = "neo4j";
        private String password = "neo4j";
        private String realm;
    }
}
