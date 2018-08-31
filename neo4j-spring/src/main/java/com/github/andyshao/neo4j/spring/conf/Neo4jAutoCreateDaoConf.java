package com.github.andyshao.neo4j.spring.conf;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import com.github.andyshao.neo4j.dao.DaoContext;

@Configuration
public class Neo4jAutoCreateDaoConf implements ApplicationContextAware, BeanDefinitionRegistryPostProcessor{
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        DaoContext daoContext = this.applicationContext.getBean(DaoContext.class);
        daoContext.getDaos();
        BeanDefinitionBuilder beanDefinition = BeanDefinitionBuilder.genericBeanDefinition();
        
    }

}
