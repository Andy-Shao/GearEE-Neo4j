package com.github.andyshao.neo4j.process;

import com.github.andyshao.neo4j.domain.Neo4jEntity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2020/8/28
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
public class CacheEntityScanner implements EntityScanner {
    private final EntityScanner entityScanner;
    private static final ConcurrentMap<String, Map<Class<?>, Neo4jEntity>> CACHE = new ConcurrentHashMap<>(1);

    public CacheEntityScanner(EntityScanner entityScanner) {
        this.entityScanner = entityScanner;
    }

    @Override
    public Map<Class<?>, Neo4jEntity> scan() {
        return CACHE.computeIfAbsent("myKey", k -> this.entityScanner.scan());
    }
}
