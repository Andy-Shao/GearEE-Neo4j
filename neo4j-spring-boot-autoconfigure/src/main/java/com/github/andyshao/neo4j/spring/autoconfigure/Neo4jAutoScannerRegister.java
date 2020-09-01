package com.github.andyshao.neo4j.spring.autoconfigure;

import com.github.andyshao.neo4j.domain.Neo4jDao;
import com.github.andyshao.neo4j.domain.analysis.Neo4jDaoAnalysis;
import com.github.andyshao.neo4j.spring.config.Neo4jDaoFactoryBean;
import com.github.andyshao.reflect.ClassOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;

import java.util.List;
import java.util.Set;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2020/8/30
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
@Slf4j
public class Neo4jAutoScannerRegister implements ImportBeanDefinitionRegistrar, BeanFactoryAware, ResourceLoaderAware {
    private BeanFactory beanFactory;
    private ResourceLoader resourceLoader;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        log.debug("Searching for mappers annotated with @Neo4jDao");
        ClassPathNeo4jDaoScanner scanner = new ClassPathNeo4jDaoScanner(registry);

        if(this.resourceLoader != null) scanner.setResourceLoader(this.resourceLoader);

        List<String> packages = AutoConfigurationPackages.get(this.beanFactory);

        scanner.setAnnotationClass(com.github.andyshao.neo4j.annotation.Neo4jDao.class);
        scanner.registerFilters();
//        scanner.doScan(StringUtils.toStringArray(packages));

        for(String pkg : packages) {
            Set<BeanDefinition> candidates = scanner.findCandidateComponents(pkg);
            candidates.forEach(candidate -> {
                Neo4jDao neo4jDao = Neo4jDaoAnalysis.analyseDao(ClassOperation.forName(candidate.getBeanClassName()));
                BeanDefinitionBuilder beanDefinBuilder =
                        BeanDefinitionBuilder.genericBeanDefinition(Neo4jDaoFactoryBean.class);
                beanDefinBuilder.addPropertyValue("neo4jDao", neo4jDao);
                beanDefinBuilder.addPropertyValue("daoInterface", neo4jDao.getDaoClass());
                beanDefinBuilder.addAutowiredProperty("daoFactory");
                registry.registerBeanDefinition(neo4jDao.getDaoId(), beanDefinBuilder.getBeanDefinition());
            });
        }
    }
}
