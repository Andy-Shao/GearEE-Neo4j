package com.github.andyshao.neo4j.spring.conf2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;

import com.github.andyshao.neo4j.annotation.Neo4jDao;
import com.github.andyshao.neo4j.spring.annotation.EnableNeo4jDao;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * Title:<br>
 * Descript:<br>
 * Copyright: Copryright(c) Dec 3, 2018<br>
 * Encoding:UNIX UTF-8
 * @author Andy.Shao
 *
 */
@Slf4j
public class Neo4jDaoScannerRegistrar implements ImportBeanDefinitionRegistrar, BeanFactoryAware, ResourceLoaderAware, ImportAware{
    private BeanFactory beanFactory;
    private ResourceLoader resourceLoader;
    private List<Package> scannerPackages = Collections.emptyList();

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata , BeanDefinitionRegistry registry) {
        log.debug("Searching for dao annotated with @Neo4jDao");
        ClassPathNeo4jDaoScanner scanner = new ClassPathNeo4jDaoScanner(registry);
        
        if(this.resourceLoader != null) scanner.setResourceLoader(this.resourceLoader);
        
        @SuppressWarnings("unused")
        List<String> packages = AutoConfigurationPackages.get(this.beanFactory);
        
        scanner.setAnnotationClass(Neo4jDao.class);
        scanner.registerFilters();
        scanner.doScan(StringUtils.toStringArray(this.scannerPackages.stream().map(Package::getName).collect(Collectors.toList())));
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        Map<String , Object> enableAttrMap = importMetadata.getAnnotationAttributes(EnableNeo4jDao.class.getName());
        AnnotationAttributes enableAttrs = AnnotationAttributes.fromMap(enableAttrMap);
        Class<?>[] packageClasses = enableAttrs.getClassArray("packageClasses");
        Set<Package> tmp = new HashSet<>();
        for (Class<?> pkgClazz : packageClasses)
            tmp.add(pkgClazz.getPackage());
        this.scannerPackages = new ArrayList<>(tmp);
    }

}
