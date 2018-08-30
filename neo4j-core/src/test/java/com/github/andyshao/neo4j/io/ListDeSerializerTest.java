package com.github.andyshao.neo4j.io;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.CompletionStage;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.Transaction;

import com.github.andyshao.neo4j.demo.Api;
import com.github.andyshao.neo4j.model.SqlMethod;
import com.github.andyshao.reflect.MethodOperation;

public class ListDeSerializerTest {
    public static void main(String[] args) {
        Driver driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j" , "1303595"));
        Session session = driver.session();
        Transaction tx = session.beginTransaction();
        ListDeSerializer ds = new ListDeSerializer();
        ds.setNext(new DefaultDeSerializer());
        Object obj = tx.runAsync("MATCH (n:Api) RETURN n").thenComposeAsync(src -> {
            SqlMethod sqlMethod = new SqlMethod();
            Method declaredMethod = MethodOperation.getDeclaredMethod(ListDeSerializerTest.class , "types");
            sqlMethod.setReturnTypeInfo(MethodOperation.getReturnTypeInfo(declaredMethod));
            sqlMethod.setDefinition(declaredMethod);
            return ds.deSerialize(src , sqlMethod);
        });
        @SuppressWarnings("unchecked")
        CompletionStage<List<Api>> ret = (CompletionStage<List<Api>>) obj;
        List<Api> op = ret.toCompletableFuture().join();
        System.out.println(op);
        
        tx.commitAsync();
        session.closeAsync();
        driver.close();
    }
    
    
    public CompletionStage<List<Api>> types(){
       return null;
    }
}
