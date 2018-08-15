package com.github.andyshao.neo4j.demo;

import java.util.Arrays;

public class MyTest {
    public static void main(String[] args) {
        Arrays.stream(ApiDao.class.getInterfaces())
            .forEach(it -> System.out.println(it.getSimpleName()));
        Arrays.stream(ApiDao.class.getMethods())
            .forEach(it -> {
                System.out.println(it.getDeclaringClass() + "#" + it.getName());
            });
    }
}
