package com.github.andyshao.neo4j.base;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PackageTest {
    @Test
    public void test() {
        Package pkg = PackageTest.class.getPackage();
        String pkgName = "com.github.andyshao.neo4j.base";
        Assertions.assertEquals(pkg.getName() , pkgName);
    }
}
