package com.github.andyshao.neo4j.spring.config;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
@Builder
public class Neo4jPros {
    @Builder.Default
    private String url = "neo4j://localhost:7687";
    @Builder.Default
    private AuthTokenInfo authToken = new AuthTokenInfo();
    
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuthTokenInfo {
        @Builder.Default
        private String username = "neo4j";
        @Builder.Default
        private String password = "neo4j";
        private String realm;
    }
}
