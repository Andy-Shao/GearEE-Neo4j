package com.github.andyshao.neo4j.spring.autoconfigure;

import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;

import com.github.andyshao.neo4j.annotation.Neo4jDao;
import com.github.andyshao.neo4j.spring.conf2.ClassPathNeo4jDaoScanner;

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
public class Neo4jAutoScannerRegsitrar implements ImportBeanDefinitionRegistrar, BeanFactoryAware, ResourceLoaderAware {
    private BeanFactory beanFactory;
    private ResourceLoader resourceLoader;

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata , BeanDefinitionRegistry registry) {
        log.debug("Searching for mappers annotated with @Neo4jDao");
        ClassPathNeo4jDaoScanner scanner = new ClassPathNeo4jDaoScanner(registry);
        
        if(this.resourceLoader != null) scanner.setResourceLoader(this.resourceLoader);
        
        List<String> packages = AutoConfigurationPackages.get(this.beanFactory);
        
        scanner.setAnnotationClass(Neo4jDao.class);
        scanner.registerFilters();
        scanner.doScan(StringUtils.toStringArray(packages));
    }

}
