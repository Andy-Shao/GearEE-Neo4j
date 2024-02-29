package com.github.andyshao.neo4j.driver;

import org.neo4j.driver.*;
import org.neo4j.driver.types.Entity;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2020/8/26
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
public class CreatePersonTest {
    public static final String URL = "neo4j://localhost:7687";
    public static final String USER_NAME = "neo4j";
    public static final String PASSWORD = "password";

    public static void main(String[] args) {
        try(Driver driver = GraphDatabase.driver(URL, AuthTokens.basic(USER_NAME, PASSWORD));
            Session session = driver.session()) {
            Entity entity = session.executeWrite(tx -> {
                String query = "CREATE "
                        + "(n:Person {id: $id_id, name: $name, age: $age, gender: $gender}) "
                        + "-[:CreateUser]-> "
                        + "(u:User {username:$userName, age:$age}), "
                        + "(n2:Person {id: 2, name: 'name02', age: 2, gender: 'FEMALE'})"
                        + "RETURN n,u,n2";
                Result result = tx.run(query,
                        Values.parameters("id_id", "ERHSBSYKAHV04SNIPHUPBDR",
                                "name", "ShaoWeiChuang",
                                "userName", "andy.shao",
                                "age", 28,
                                "gender", "MALE"));
                return result.single().get(0).asEntity();
            });
            entity.asMap().forEach((key,val) -> System.out.printf("%s:%s%n", key, val));
        }
    }
}
