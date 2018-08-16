package com.github.andyshao.neo4j.mapper.impl;

import java.util.Arrays;

public class ThrowErrorInStreamTest {
    public static void main(String[] args) {
        try {
            Arrays.asList("1", "2", "3").parallelStream().forEach(it -> {
                throw new RuntimeException();
            });
        } catch(RuntimeException ex) {
            System.out.println("Has an error");
        }
    }
}
