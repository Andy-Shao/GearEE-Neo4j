package com.github.andyshao.neo4j.driver;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.async.AsyncSession;
import org.neo4j.driver.async.ResultCursor;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2021/7/26
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
public class AsyncCreatePersonTest {

    public static final String QUERY = "CREATE (n:Person {id:1, age:32, name:'Andy.Shao', gender:'MALE'}) RETURN n";

    public static void main(String[] args) {
        try(Driver driver =
                    GraphDatabase.driver(CreatePersonTest.URL,
                            AuthTokens.basic(CreatePersonTest.USER_NAME, CreatePersonTest.PASSWORD));
        ){
            final AsyncSession asyncSession = driver.asyncSession();
//            final CompletionStage<AsyncTransaction> transation = asyncSession.beginTransactionAsync();
//            final CompletionStage<ResultCursor> result =
//                    transation.thenCompose(tx -> tx.runAsync(QUERY));

            final CompletionStage<ResultCursor> result = asyncSession.writeTransactionAsync(tx -> tx.runAsync(QUERY));

            Mono.fromCompletionStage(result).block();
        }
    }
}
