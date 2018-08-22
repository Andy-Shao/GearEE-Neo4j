package com.github.andyshao.neo4j.driver;

import java.time.LocalDateTime;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Values;
import org.neo4j.driver.v1.types.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;

public class CreateApiTest {
    @Data
    @AllArgsConstructor
    public static class User {
        private String username;
        private Integer age;
    }
    
    public static void main(String[] args) {
        try(
        Driver driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j" , "1303595"));
        Session session = driver.session();){
            Entity entity = session.writeTransaction(tx -> {
                String query = "CREATE "
                    + "(n:Api {systemAlias: $systemAlias, apiName: $apiName, createTime: $createTime}) "
                    + "-[:CreateUser]-> "
                    + "(u:User {name:$name, age:$age}) "
                    + "RETURN n,u";
                StatementResult result = tx.run(query,
                    Values.parameters("createTime", LocalDateTime.now(), 
                        "systemAlias", "my.system", 
                        "apiName", "api001",
                        "name", "andy.shao",
                        "age", 28)
                    );
                return result.single().get(0).asEntity();
            });
            entity.asMap().forEach((key,val) -> System.out.println(String.format("%s:%s" , key, val)));
            
        }
    }
}
