package com.github.andyshao.neo4j.demo;

import reactor.core.publisher.Mono;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2020/8/26
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
public class MyTest {
    public static void main(String[] args) {
        Mono<String> mono = Mono.just("123");
        mono.hasElement()
                .doOnNext(hasEle -> {
                    if(hasEle) {
                        mono.doOnNext(System.out::println)
                                .block();
                    }
                })
                .block();
    }
}
