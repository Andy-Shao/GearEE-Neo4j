package com.github.andyshao.neo4j.driver;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.Transaction;

public class FindApi {
    public static void main(String[] args) {
        testFindCount();
    }
    
    public static void testFindCount() {
        Driver driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j" , "1303595"));
        Session session = driver.session();
        Transaction tx = session.beginTransaction();
        tx.runAsync("MATCH (n:Api) RETURN count(n)").thenApplyAsync(src -> {
            src.singleAsync().thenAcceptAsync(record -> {
                System.out.println(String.format("api size is: %d" , record.get(0).asLong()));
            });
            return null;
        });
        tx.commitAsync();
    }
}
