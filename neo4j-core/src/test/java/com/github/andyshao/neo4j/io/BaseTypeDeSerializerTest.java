package com.github.andyshao.neo4j.io;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.Transaction;

import com.github.andyshao.neo4j.model.SqlMethod;
import com.github.andyshao.reflect.MethodOperation;

public class BaseTypeDeSerializerTest {
    public static void main(String[] args) {
        test();
    }
    public static void test() {
        Driver driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j" , "1303595"));
        Session session = driver.session();
        Transaction tx = session.beginTransaction();
        BaseTypeDeSerializer ds = new BaseTypeDeSerializer();
        ds.setNext(new DefaultDeSerializer());
        Object find = tx.runAsync("MATCH (n:Api) RETURN count(n)").thenComposeAsync(src -> {
            SqlMethod sqlMethod = new SqlMethod();
            sqlMethod.setReturnTypeInfo(MethodOperation.getReturnTypeInfo(MethodOperation.getDeclaredMethod(BaseTypeDeSerializerTest.class , "types")));
            return ds.deSerialize(src , sqlMethod);
        });
        CompletionStage<Void> commitAsync = tx.commitAsync();
        
        @SuppressWarnings({ "unchecked" })
        CompletionStage<Optional<Integer>> arg = (CompletionStage<Optional<Integer>>) find;
        
        Optional<Integer> join = arg.toCompletableFuture().join();
        System.out.println(join.get());
        System.out.println(commitAsync);
        session.close();
        driver.close();
    }
    
    public CompletionStage<Optional<Integer>> types(){
        return null;
    }
}
