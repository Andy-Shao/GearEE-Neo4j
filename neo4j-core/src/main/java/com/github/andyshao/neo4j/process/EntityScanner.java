package com.github.andyshao.neo4j.process;

import com.github.andyshao.neo4j.domain.Neo4jEntity;

import java.util.Optional;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2020/8/28
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
public interface EntityScanner {
    Optional<Neo4jEntity> scan(Class<?> entityType);
}
