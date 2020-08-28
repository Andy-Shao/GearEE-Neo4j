package com.github.andyshao.neo4j.domain.analysis;

import com.github.andyshao.neo4j.domain.Neo4jEntity;
import com.github.andyshao.reflect.PackageOperation;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2020/8/28
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
public final class Neo4jEntityAnalysis {
    public static final Optional<Neo4jEntity> analyseNeo4jEntity(Class<?> clazz) {
        com.github.andyshao.neo4j.annotation.Neo4jEntity annotation =
                clazz.getAnnotation(com.github.andyshao.neo4j.annotation.Neo4jEntity.class);
        if(Objects.isNull(annotation)) return Optional.empty();
        Neo4jEntity ret = new Neo4jEntity();
        ret.setDefinition(clazz);
        ret.setFields(Neo4jEntityFieldAnalysis.analyse(clazz));
        return Optional.of(ret);
    }

    public static final List<Neo4jEntity> analyseNeo4jEntity(Package pkg) {
        return Arrays.stream(PackageOperation.getPackageClasses(pkg))
                .filter(it -> {
                    com.github.andyshao.neo4j.annotation.Neo4jEntity annotation =
                            it.getAnnotation(com.github.andyshao.neo4j.annotation.Neo4jEntity.class);
                    return Objects.nonNull(annotation);
                })
                .map(Neo4jEntityAnalysis::analyseNeo4jEntity)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public static final List<Neo4jEntity> analyseNeo4jEntity(String packageRegex) {
        return Arrays.stream(PackageOperation.getPackages(packageRegex))
                .flatMap(it -> analyseNeo4jEntity(it).stream())
                .collect(Collectors.toList());
    }
}
