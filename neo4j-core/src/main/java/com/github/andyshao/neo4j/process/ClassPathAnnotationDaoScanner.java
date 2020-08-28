package com.github.andyshao.neo4j.process;

import com.github.andyshao.neo4j.domain.Neo4jDao;
import com.github.andyshao.neo4j.domain.analysis.Neo4jDaoAnalysis;
import com.google.common.collect.Maps;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2020/8/27
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
public class ClassPathAnnotationDaoScanner implements DaoScanner{
    private String[] packageRegexes;
    private Package[] pkgs;

    public ClassPathAnnotationDaoScanner(String... packageRegexes) {
        this.packageRegexes = packageRegexes;
    }

    public ClassPathAnnotationDaoScanner(Package... pkgs) {
        this.pkgs = pkgs;
    }

    @Override
    public Map<String, Neo4jDao> scan() {
        if(Objects.nonNull(this.packageRegexes)) {
            return Arrays.stream(this.packageRegexes)
                .flatMap(packageRegex -> Neo4jDaoAnalysis.analyseDaoFromPackageRegex(packageRegex).stream())
                .collect(Collectors.toMap(Neo4jDao::getEntityId, it -> it));
        } else if(Objects.nonNull(this.pkgs)) {
            return Arrays.stream(this.pkgs)
                    .flatMap(pkg -> Neo4jDaoAnalysis.analyseDaoFromOnePackage(pkg).stream())
                    .collect(Collectors.toMap(Neo4jDao::getEntityId, it -> it));
        }
        return Maps.newHashMap();
    }
}
