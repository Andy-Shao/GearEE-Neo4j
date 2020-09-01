package com.github.andyshao.neo4j.spring.autoconfigure;

import com.github.andyshao.neo4j.spring.annotation.Neo4jDaoBeanNameGenerator;
import com.github.andyshao.reflect.ClassOperation;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.TypeFilter;

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
    private BeanNameGenerator beanNameGenerator = Neo4jDaoBeanNameGenerator.INSTANCE;

    public ClassPathNeo4jDaoScanner(BeanDefinitionRegistry registry) {
        super(registry, Boolean.FALSE);
    }

    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        LinkedHashSet<BeanDefinitionHolder> beanDefinitions = new LinkedHashSet<>();
        for(String pkg : basePackages) {
            Set<BeanDefinition> candidates = findCandidateComponents(pkg);
            candidates.forEach(candidate -> {
                String beanName = this.beanNameGenerator.generateBeanName(candidate, this.getRegistry());
                Class<?> beanClass = ClassOperation.forName(candidate.getBeanClassName());
                BeanDefinitionBuilder beanDefinitionBuilder =
                        BeanDefinitionBuilder.genericBeanDefinition(Neo4jDaoFactoryBean.class);
                beanDefinitionBuilder.addPropertyValue("daoInterface", beanClass);
                beanDefinitionBuilder.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_CONSTRUCTOR);
                AbstractBeanDefinition beanDefinition = beanDefinitionBuilder.getBeanDefinition();
                getRegistry().registerBeanDefinition(beanName, beanDefinition);
                beanDefinitions.add(new BeanDefinitionHolder(beanDefinition, beanName));
            });
        }

        return beanDefinitions;
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
