package com.github.andyshao.neo4j.spring.config;

import com.github.andyshao.neo4j.domain.Neo4jDao;
import com.github.andyshao.neo4j.process.DaoFactory;
import com.github.andyshao.neo4j.process.DaoScanner;
import lombok.Setter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;

import java.util.Map;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2020/8/28
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
public class Neo4jDaoScanner implements BeanDefinitionRegistryPostProcessor {
    @Setter
    private DaoScanner daoScanner;
    @Setter
    private DaoFactory daoFactory;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        Map<String, Neo4jDao> daoMap = this.daoScanner.scan();
        for(Map.Entry<String, Neo4jDao> daoEntry : daoMap.entrySet()) {
            BeanDefinitionBuilder beanDefinBuilder = BeanDefinitionBuilder.genericBeanDefinition(Neo4jDaoFactoryBean.class);
            beanDefinBuilder.addPropertyValue("daoInterface" , daoEntry.getValue().getClass().getInterfaces()[0]);
            beanDefinBuilder.addPropertyValue("daoFactory" , this.daoFactory);
            registry.registerBeanDefinition(daoEntry.getKey() , beanDefinBuilder.getBeanDefinition());
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }
}
