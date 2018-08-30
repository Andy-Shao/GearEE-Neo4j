package com.github.andyshao.neo4j.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.andyshao.neo4j.mapper.impl.PackageMapperScanner;
import com.github.andyshao.neo4j.model.Neo4jDaoInfo;

/**
 * 
 * Title:<br>
 * Descript:<br>
 * Copyright: Copryright(c) Aug 30, 2018<br>
 * Encoding:UNIX UTF-8
 * @author Andy.Shao
 *
 */
public class PackageScanDaoContext extends AbstractDaoContext {
    private final Map<String , Neo4jDaoInfo> cacheByName = new HashMap<>();
    private final Map<Class<?>, Neo4jDaoInfo> cacheByClass = new HashMap<>();
    
    public PackageScanDaoContext(List<Package> pkg) {
        pkg.forEach(it -> {
            PackageMapperScanner scanner = new PackageMapperScanner();
            scanner.setPackagePath(it);
            cacheByName.putAll(scanner.scan());
        });
        cacheByClass.forEach((k,v) -> cacheByClass.put(v.getDaoClass() , v));
    }

    @Override
    protected Neo4jDaoInfo findByName(String daoName) {
        return cacheByName.get(daoName);
    }

    @Override
    protected Neo4jDaoInfo findByClass(Class<?> clazz) {
        return cacheByClass.get(clazz);
    }

}
