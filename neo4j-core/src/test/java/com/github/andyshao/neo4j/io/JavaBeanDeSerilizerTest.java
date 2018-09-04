package com.github.andyshao.neo4j.io;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.Transaction;

import com.github.andyshao.neo4j.demo.Api;
import com.github.andyshao.neo4j.model.SqlMethod;
import com.github.andyshao.reflect.MethodOperation;

public class JavaBeanDeSerilizerTest {
    public static void main(String[] args) {
        Driver driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j" , "1303595"));
        Session session = driver.session();
        Transaction tx = session.beginTransaction();
        JavaBeanDeSerializer ds = new JavaBeanDeSerializer();
        ds.setNext(new DefaultDeSerializer());
        Object obj = tx.runAsync("MATCH (n:Api) RETURN n LIMIT 1").thenComposeAsync(src -> {
            SqlMethod sqlMethod = new SqlMethod();
            Method declaredMethod = MethodOperation.getDeclaredMethod(JavaBeanDeSerilizerTest.class , "types");
            sqlMethod.getSqlMethodRet().setReturnTypeInfo(MethodOperation.getReturnTypeInfo(declaredMethod));
            sqlMethod.setDefinition(declaredMethod);
            return ds.deSerialize(src , sqlMethod);
        });
        @SuppressWarnings("unchecked")
        CompletionStage<Optional<Api>> ret = (CompletionStage<Optional<Api>>) obj;
        Optional<Api> op = ret.toCompletableFuture().join();
        System.out.println(op);
        tx.commitAsync();
        session.closeAsync();
        driver.close();
    }
    
    public CompletionStage<Optional<Api>> types(){
       return null;
    }
}
