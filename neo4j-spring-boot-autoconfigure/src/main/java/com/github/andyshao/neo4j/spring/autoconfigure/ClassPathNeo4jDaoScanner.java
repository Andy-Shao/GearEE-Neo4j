package com.github.andyshao.neo4j.spring.autoconfigure;

import com.github.andyshao.reflect.ClassOperation;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.*;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.Assert;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.Set;

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
@Setter
public class ClassPathNeo4jDaoScanner extends ClassPathBeanDefinitionScanner {
    private Class<? extends Annotation> annotationClass;
    private Class<?> markerInterface;
    private BeanNameGenerator beanNameGenerator = AnnotationBeanNameGenerator.INSTANCE;
    private ScopeMetadataResolver scopeMetadataResolver = new AnnotationScopeMetadataResolver();

    public ClassPathNeo4jDaoScanner(BeanDefinitionRegistry registry) {
        super(registry, Boolean.FALSE);
    }

    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
//        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
//
//        if(beanDefinitions.isEmpty()) {
//            log.warn("No Neo4j dao was found in '{}' package. Please check your configuration", Arrays.toString(basePackages));
//        } else processBeanDefinition(beanDefinitions);
//        return beanDefinitions;

        Assert.notEmpty(basePackages, "At least one base package must be specified");
        Set<BeanDefinitionHolder> beanDefinitions = new LinkedHashSet<>();
        for (String basePackage : basePackages) {
            Set<BeanDefinition> candidates = findCandidateComponents(basePackage);
            for (BeanDefinition candidate : candidates) {
                ScopeMetadata scopeMetadata = this.scopeMetadataResolver.resolveScopeMetadata(candidate);
                candidate.setScope(scopeMetadata.getScopeName());
                String beanName = this.beanNameGenerator.generateBeanName(candidate, this.getRegistry());
                BeanDefinitionHolder definitionHolder = new BeanDefinitionHolder(candidate, beanName);
                processBeanDefinition(definitionHolder);
                if (candidate instanceof AbstractBeanDefinition) {
                    postProcessBeanDefinition((AbstractBeanDefinition) candidate, beanName);
                }
                if (candidate instanceof AnnotatedBeanDefinition) {
                    AnnotationConfigUtils.processCommonDefinitionAnnotations((AnnotatedBeanDefinition) candidate);
                }
                if (checkCandidate(beanName, candidate)) {
//                    definitionHolder =
//                            AnnotationConfigUtils.applyScopedProxyMode(scopeMetadata, definitionHolder, this.getRegistry());
//                    beanDefinitions.add(definitionHolder);
//                    registerBeanDefinition(definitionHolder, this.getRegistry());
                    registerBeanDefinition(definitionHolder, this.getRegistry());
                }
            }
        }
        return beanDefinitions;
    }
    
    private void processBeanDefinition(Set<BeanDefinitionHolder> beanDefinitions) {
        GenericBeanDefinition definition;
        for(BeanDefinitionHolder holder : beanDefinitions) {
            processBeanDefinition(holder);
        }
    }

    private void processBeanDefinition(BeanDefinitionHolder holder) {
        GenericBeanDefinition definition;
        definition = (GenericBeanDefinition) holder.getBeanDefinition();
        String beanClassName = definition.getBeanClassName();
        definition.setBeanClass(Neo4jDaoFactoryBean.class);
        definition.getPropertyValues().add("daoInterface" , ClassOperation.forName(beanClassName));
        definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_CONSTRUCTOR);
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isIndependent() && beanDefinition.getMetadata().isInterface();
    }

    /**
     * Configures parent scanner to search for the right interfaces. It can search
     * for all interfaces or just for those that extends a markerInterface or/and
     * those annotated with the annotationClass
     */
    public void registerFilters() {
        boolean acceptAllInterface = true;
        
        // if specified use the given annotation and / or marker interface
        if(this.annotationClass != null) {
            addIncludeFilter(new AnnotationTypeFilter(this.annotationClass));
            acceptAllInterface = false;
        }
        
        // override AssignableTypeFilter to ignore matches on the actual marker interface
        if(this.markerInterface != null) {
            addIncludeFilter(new AssignableTypeFilter(this.markerInterface) {
                @Override
                protected boolean matchClassName(String className) {
                    return false;
                }
            });
            acceptAllInterface = false;
        }
        
        if(acceptAllInterface) {
            // default include filter that accepts all classes
            addIncludeFilter(new TypeFilter() {
                
                @Override
                public boolean match(MetadataReader metadataReader , MetadataReaderFactory metadataReaderFactory) throws IOException {
                    return true;
                }
            });
        }
        
        // exclude package-info.java
        addExcludeFilter(new TypeFilter() {
            
            @Override
            public boolean match(MetadataReader metadataReader , MetadataReaderFactory metadataReaderFactory) throws IOException {
                String className = metadataReader.getClassMetadata().getClassName();
                return className.endsWith("package-info");
            }
        });
    }
}
