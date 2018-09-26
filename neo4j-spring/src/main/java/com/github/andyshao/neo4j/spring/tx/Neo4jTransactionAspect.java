package com.github.andyshao.neo4j.spring.tx;

import java.util.concurrent.CompletionStage;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * Title:<br>
 * Descript:<br>
 * Copyright: Copryright(c) Sep 10, 2018<br>
 * Encoding:UNIX UTF-8
 * @author Andy.Shao
 *
 */
@Slf4j
@Aspect
@Configuration
@EnableAspectJAutoProxy
public class Neo4jTransactionAspect {
    @Autowired
    private Driver driver;
    
    @Pointcut("execution(public * *(..))")
    public void publicMethod() {}
    
    @Pointcut("@within(com.github.andyshao.neo4j.annotation.Neo4jTransaction)")
    public void processClass() {}
    
    @Pointcut("@annotation(com.github.andyshao.neo4j.annotation.Neo4jTransaction)")
    public void processMethod() {}
    
    @Around("publicMethod() && processClass()")
    public Object doClassProcessor(ProceedingJoinPoint pjp) throws Throwable {
        return process(pjp);
    }
    
    @Around("processMethod()")
    public Object doMethodProcessor(ProceedingJoinPoint pjp) throws Throwable {
        return process(pjp);
    }

    public Object process(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        int index = -1;
        Transaction tx = null;
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Class<?>[] parameterTypes = signature.getParameterTypes();
        for(int i=0; i<parameterTypes.length; i++) {
            if(parameterTypes[i].isAssignableFrom(Transaction.class)) {
                index = i;
                tx = (Transaction) args[i];
            }
        }
        @SuppressWarnings("resource")
        final Session session = tx == null ? this.driver.session() : null;
        final Transaction transaction = tx == null ? session.beginTransaction() : tx;
        if(tx == null) args[index] = transaction;
        Object obj = pjp.proceed(args);
        CompletionStage<?> cs = (CompletionStage<?>) obj;
        if(index != -1) cs.thenAcceptAsync(o -> transaction.commitAsync().thenAcceptAsync(v -> session.closeAsync()))
            .exceptionally(ex -> {
                log.error("SQL process has an exception" , ex);
                transaction.rollbackAsync().thenAcceptAsync(v -> session.closeAsync());
                return null;
            });
        return obj;
    }
}
