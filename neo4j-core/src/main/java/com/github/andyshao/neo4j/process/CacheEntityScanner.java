package com.github.andyshao.neo4j.process;

import com.github.andyshao.neo4j.domain.Neo4jEntity;
import com.google.common.collect.Maps;

import java.util.Optional;
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
    private static final ConcurrentMap<Class<?>, Optional<Neo4jEntity>> CACHE = Maps.newConcurrentMap();

    public CacheEntityScanner(EntityScanner entityScanner) {
        this.entityScanner = entityScanner;
    }

    @Override
    public Optional<Neo4jEntity> scan(Class<?> entityType) {
        return CACHE.computeIfAbsent(entityType, this.entityScanner::scan);
    }
}
