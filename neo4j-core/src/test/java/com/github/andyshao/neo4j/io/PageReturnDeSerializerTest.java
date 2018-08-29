package com.github.andyshao.neo4j.io;

import java.lang.reflect.Method;
import java.util.concurrent.CompletionStage;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.Transaction;

import com.github.andyshao.neo4j.demo.Api;
import com.github.andyshao.neo4j.model.PageReturn;
import com.github.andyshao.neo4j.model.Pageable;
import com.github.andyshao.neo4j.model.SqlMethod;
import com.github.andyshao.reflect.MethodOperation;

public class PageReturnDeSerializerTest {
    public static void main(String[] args) {
        Driver driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j" , "1303595"));
        Session session = driver.session();
        Transaction tx = session.beginTransaction();
        PageReturnDeSerializer ds = new PageReturnDeSerializer(new DefaultDeSerializer());
        Object obj = tx.runAsync("MATCH (n:Api) RETURN n, 0 AS pageNum, 10 AS pageSize").thenComposeAsync(src -> {
            SqlMethod sqlMethod = new SqlMethod();
            Method declaredMethod = MethodOperation.getDeclaredMethod(PageReturnDeSerializerTest.class , "types", Pageable.class);
            sqlMethod.setReturnTypeInfo(MethodOperation.getReturnTypeInfo(declaredMethod));
            sqlMethod.setDefinition(declaredMethod);
            return ds.deSerialize(src , sqlMethod);
        });
        @SuppressWarnings("unchecked")
        CompletionStage<PageReturn<Api>> ret = (CompletionStage<PageReturn<Api>>) obj;
        PageReturn<Api> op = ret.toCompletableFuture().join();
        System.out.println(op);
        
        tx.commitAsync();
        session.closeAsync();
        driver.close();
    }
    
    
    public CompletionStage<PageReturn<Api>> types(Pageable<Void> pageable){
       return null;
    }
}
