package com.github.andyshao.neo4j.spring.process;

import com.github.andyshao.neo4j.domain.Neo4jDao;
import com.github.andyshao.neo4j.domain.analysis.Neo4jDaoAnalysis;
import com.github.andyshao.neo4j.process.DaoScanner;
import com.google.common.collect.Maps;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2020/9/1
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
public class ClassPathAnnotationDaoScanner implements DaoScanner {
    private String[] basePackages;
    private Package[] pkgs;

    public ClassPathAnnotationDaoScanner(String... basePackages) {
        this.basePackages = basePackages;
    }

    public ClassPathAnnotationDaoScanner(Package... pkgs) {
        this.pkgs = pkgs;
    }

    @Override
    public Map<String, Neo4jDao> scan() {
        if(Objects.nonNull(this.basePackages)) {
            //TODO
            return null;
        } else if(Objects.nonNull(this.pkgs)) {
            return Arrays.stream(this.pkgs)
                    .flatMap(pkg -> Neo4jDaoAnalysis.analyseDaoFromOnePackage(pkg).stream())
                    .collect(Collectors.toMap(Neo4jDao::getDaoId, it -> it));
        }
        return Maps.newHashMap();
    }
}
