package org.example.neo4j;

import org.neo4j.driver.v1.AuthToken;
import org.neo4j.driver.v1.AuthTokens;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
//@EnableNeo4jDao(packageClasses = PersonDao.class)
public class Neo4jTestApplication {
	public static void main(String[] args){
		SpringApplication.run(Neo4jTestApplication.class, args);
	}
	
	@Bean
	public AuthToken neo4jAuthToken() {
        return AuthTokens.basic("neo4j" , "1303595");
    }
}
