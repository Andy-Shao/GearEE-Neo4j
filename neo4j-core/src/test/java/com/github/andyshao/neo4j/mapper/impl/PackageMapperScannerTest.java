package com.github.andyshao.neo4j.mapper.impl;

import java.util.Map;

import org.junit.jupiter.api.Test;

import com.github.andyshao.neo4j.demo.ApiDao;
import com.github.andyshao.neo4j.model.Neo4jDaoInfo;

public class PackageMapperScannerTest {
    @Test
    public void test() {
        PackageMapperScanner scanner = new PackageMapperScanner();
        scanner.setPackagePath(ApiDao.class.getPackage());
        Map<String , Neo4jDaoInfo> scan = scanner.scan();
        scan.values().forEach(it -> System.out.println(it));
    }
}
