package com.github.andyshao.neo4j.spring.transaction;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.neo4j.driver.Driver;
import org.neo4j.driver.async.AsyncSession;
import org.neo4j.driver.async.AsyncTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2020/8/28
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
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
        AsyncTransaction tx = null;
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Class<?>[] parameterTypes = signature.getParameterTypes();
        for(int i=0; i<parameterTypes.length; i++) {
            if(parameterTypes[i].isAssignableFrom(AsyncTransaction.class)) {
                index = i;
                tx = (AsyncTransaction) args[i];
            }
        }
        @SuppressWarnings("resource")
        final AsyncSession session = tx == null ? this.driver.asyncSession() : null;
        final CompletionStage<AsyncTransaction> transaction = tx == null ? session.beginTransactionAsync() : CompletableFuture.completedStage(tx);
        if(tx == null) args[index] = transaction.toCompletableFuture().join();
        Object obj = pjp.proceed(args);

        if(obj instanceof Mono) {
            Mono<?> cs = (Mono<?>) obj;
            //TODO
        } else if(obj instanceof Flux) {
            Flux<?> cs = (Flux<?>) obj;
            //TODO
        }
//        CompletionStage<?> cs = (CompletionStage<?>) obj;
//        if(index != -1) cs.thenAcceptAsync(o -> transaction.commitAsync().thenAcceptAsync(v -> session.closeAsync()))
//                .exceptionally(ex -> {
//                    log.error("SQL process has an exception" , ex);
//                    transaction.rollbackAsync().thenAcceptAsync(v -> session.closeAsync());
//                    return null;
//                });
        return obj;
    }
}
