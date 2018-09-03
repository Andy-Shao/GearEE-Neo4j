package com.github.andyshao.neo4j.spring.conf;

import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;

import com.github.andyshao.neo4j.dao.DaoContext;

import lombok.Setter;

public class Neo4jDaoScanner implements BeanDefinitionRegistryPostProcessor {
    @Setter
    private DaoContext daoContext;
    
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        Map<String , Object> daos = daoContext.getDaos();
        for(Entry<String , Object> daoEntry : daos.entrySet()) {
            BeanDefinitionBuilder beanDefinBuilder = BeanDefinitionBuilder.genericBeanDefinition(Neo4jDaoFactoryBean.class);
            beanDefinBuilder.addPropertyValue("daoInterface" , daoEntry.getValue().getClass().getInterfaces()[0]);
            beanDefinBuilder.addPropertyValue("daoContext" , daoContext);
            registry.registerBeanDefinition(daoEntry.getKey() , beanDefinBuilder.getBeanDefinition());
        }

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
    }

}
