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

import java.util.Objects;
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

    @SuppressWarnings("unchecked")
    public Object process(ProceedingJoinPoint pjp) throws Throwable {
        final Object[] args = pjp.getArgs();
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Class<?>[] parameterTypes = signature.getParameterTypes();
        //NOTE: Generic type testing cannot use the instanceof
        if(!Objects.equals(parameterTypes[parameterTypes.length-1].getName(), CompletionStage.class.getName())) {
            return pjp.proceed(args);
        }

        CompletionStage<AsyncTransaction> tx;
        boolean hasTx;
        if(Objects.isNull(args) || args.length == 0) {
            return pjp.proceed(args);
        }
        else if(Objects.isNull(args[args.length - 1])) {
            tx = null;
            hasTx = false;
        } else {
            tx = (CompletionStage<AsyncTransaction>) args[args.length - 1];
            hasTx = true;
        }
        final AsyncSession asyncSession = hasTx ? null : this.driver.asyncSession();
        if(!hasTx) {
            tx = asyncSession.beginTransactionAsync();
            args[args.length - 1] = tx;
        }

        Object obj = pjp.proceed(args);

        final CompletionStage<AsyncTransaction> trx = tx;
        if(obj instanceof Mono) {
            Mono<?> cs = (Mono<?>) obj;
            if(hasTx) cs = cs.doFinally(signalType -> finishingTransaction(asyncSession, trx, signalType));
            obj = cs;
        } else if(obj instanceof Flux) {
            Flux<?> cs = (Flux<?>) obj;
            if(hasTx) cs = cs.doFinally(signalType -> finishingTransaction(asyncSession, trx, signalType));
            obj = cs;
        }

        return obj;
    }

    private void finishingTransaction(AsyncSession asyncSession, CompletionStage<AsyncTransaction> trx,
                                      reactor.core.publisher.SignalType signalType) {
        switch (signalType) {
            case ON_ERROR:
            case CANCEL:
                trx.thenCompose(transaction -> {
                    return transaction.rollbackAsync()
                            .thenCompose(v -> asyncSession.closeAsync());
                });
            case ON_COMPLETE:
                trx.thenCompose(transaction -> {
                    return transaction.commitAsync()
                            .thenCompose(v -> asyncSession.closeAsync());
                });
        }
    }
}
