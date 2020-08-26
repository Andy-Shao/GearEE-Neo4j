package com.github.andyshao.neo4j.driver;

import com.google.common.collect.Maps;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Record;
import org.neo4j.driver.async.AsyncSession;
import org.neo4j.driver.async.AsyncTransaction;
import org.neo4j.driver.async.ResultCursor;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2020/8/26
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
public class MatchPersonTest {

    public static void main(String[] args) {
        try(Driver driver =
                    GraphDatabase.driver(CreatePersonTest.URL,
                            AuthTokens.basic(CreatePersonTest.USER_NAME, CreatePersonTest.PASSWORD));
        ){
            AsyncSession asyncSession = driver.asyncSession();
            CompletionStage<AsyncTransaction> tx = asyncSession.beginTransactionAsync();
            CompletionStage<List<Record>> rs = tx
                    .thenComposeAsync(transaction -> {
                        return transaction.runAsync("MATCH (p:Person) -[:CreateUser]-> (u:User) RETURN p,u");
                    })
                    .thenComposeAsync(ResultCursor::listAsync);
            Mono.fromCompletionStage(rs)
                    .doOnNext(records -> {
                        records.forEach(record -> {
                            System.out.println(record.get("p").asMap(Maps.newHashMap()));
                            System.out.println(record.get("u").asMap(Maps.newHashMap()));
                        });
                    })
                    .doFinally(signalType -> {
                        switch (signalType) {
                            case ON_COMPLETE:
                                tx.toCompletableFuture().join().commitAsync();
                            case CANCEL:
                            case ON_ERROR:
                                tx.toCompletableFuture().join().rollbackAsync();
                        }
                    })
                    .block();
        }
    }
}
