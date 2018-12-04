package com.github.andyshao.neo4j.dao.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.andyshao.neo4j.mapper.impl.PackageMapperScanner;
import com.github.andyshao.neo4j.model.Neo4jDaoInfo;
import com.github.andyshao.reflect.PackageOperation;

/**
 * 
 * Title:<br>
 * Descript:<br>
 * Copyright: Copryright(c) Dec 4, 2018<br>
 * Encoding:UNIX UTF-8
 * @author Andy.Shao
 *
 */
public class PackagesScanDaoContext extends AbstractDaoContext {
    private final Map<String , Neo4jDaoInfo> cacheByName = new HashMap<>();
    private final Map<Class<?>, Neo4jDaoInfo> cacheByClass = new HashMap<>();
    
    public PackagesScanDaoContext(List<Package> pkg) {
        Map<String, Package> tmp = new HashMap<>();
        pkg.forEach(it -> {
            Arrays.stream(PackageOperation.getPckages(it)).forEach(p -> {
                tmp.put(p.getName() , p);
            });
        });
        tmp.forEach((pn,p) -> {
            PackageMapperScanner scanner = new PackageMapperScanner();
            scanner.setPackagePath(p);
            scanner.scan().forEach((k,v) -> {
                cacheByName.put(k , v);
                cacheByClass.put(v.getDaoClass() , v);
            });
        });
    }

    @Override
    protected Neo4jDaoInfo findByName(String daoName) {
        return cacheByName.get(daoName);
    }

    @Override
    protected Neo4jDaoInfo findByClass(Class<?> clazz) {
        return cacheByClass.get(clazz);
    }

    @Override
    public Map<String, Object> getDaos() {
        Map<String, Object> daos = new HashMap<>();
        cacheByName.keySet()
            .stream().forEach(key -> daos.put(key , this.getDao(key)));
        return daos;
    }
}
