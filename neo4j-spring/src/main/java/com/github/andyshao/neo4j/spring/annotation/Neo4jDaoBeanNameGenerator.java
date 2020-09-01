package com.github.andyshao.neo4j.spring.annotation;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.beans.Introspector;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2020/9/1
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
public class Neo4jDaoBeanNameGenerator implements BeanNameGenerator {
    public static final Neo4jDaoBeanNameGenerator INSTANCE = new Neo4jDaoBeanNameGenerator();
    private final Map<String, Set<String>> metaAnnotationTypesCache = new ConcurrentHashMap<>();
    private static final String NEO4JDAO_ANNOTATION_CLASSNAME = "com.github.andyshao.neo4j.annotation.Neo4jDao";

    @Override
    public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
        if (definition instanceof AnnotatedBeanDefinition) {
            String beanName = determineBeanNameFromAnnotation((AnnotatedBeanDefinition) definition);
            if (StringUtils.hasText(beanName)) {
                // Explicit bean name found.
                return beanName;
            }
        }
        return buildDefaultBeanName(definition);
    }

    @Nullable
    protected String determineBeanNameFromAnnotation(AnnotatedBeanDefinition annotatedDef) {
        AnnotationMetadata amd = annotatedDef.getMetadata();
        Set<String> types = amd.getAnnotationTypes();
        String beanName = null;
        for (String type : types) {
            AnnotationAttributes attributes =
                    AnnotationAttributes.fromMap(amd.getAnnotationAttributes(type, false));
            if (attributes != null) {
                Set<String> metaTypes = this.metaAnnotationTypesCache.computeIfAbsent(type, key -> {
                    Set<String> result = amd.getMetaAnnotationTypes(key);
                    return (result.isEmpty() ? Collections.emptySet() : result);
                });
                if (isStereotypeWithNameValue(type, metaTypes, attributes)) {
                    Object value = attributes.get("value");
                    if (value instanceof String) {
                        String strVal = (String) value;
                        if (StringUtils.hasLength(strVal)) {
                            if (beanName != null && !strVal.equals(beanName)) {
                                throw new IllegalStateException("Stereotype annotations suggest inconsistent " +
                                        "component names: '" + beanName + "' versus '" + strVal + "'");
                            }
                            beanName = strVal;
                        }
                    }
                }
            }
        }
        return beanName;
    }

    /**
     * Check whether the given annotation is a stereotype that is allowed
     * to suggest a component name through its annotation {@code value()}.
     * @param annotationType the name of the annotation class to check
     * @param metaAnnotationTypes the names of meta-annotations on the given annotation
     * @param attributes the map of attributes for the given annotation
     * @return whether the annotation qualifies as a stereotype with component name
     */
    protected boolean isStereotypeWithNameValue(String annotationType,
                                                Set<String> metaAnnotationTypes, @Nullable Map<String, Object> attributes) {

        boolean isStereotype = annotationType.equals(NEO4JDAO_ANNOTATION_CLASSNAME) ||
                metaAnnotationTypes.contains(NEO4JDAO_ANNOTATION_CLASSNAME) ||
                annotationType.equals("javax.annotation.ManagedBean") ||
                annotationType.equals("javax.inject.Named");

        return (isStereotype && attributes != null && attributes.containsKey("value"));
    }

    /**
     * Derive a default bean name from the given bean definition.
     * <p>The default implementation simply builds a decapitalized version
     * of the short class name: e.g. "mypackage.MyJdbcDao" -&gt; "myJdbcDao".
     * <p>Note that inner classes will thus have names of the form
     * "outerClassName.InnerClassName", which because of the period in the
     * name may be an issue if you are autowiring by name.
     * @param definition the bean definition to build a bean name for
     * @return the default bean name (never {@code null})
     */
    protected String buildDefaultBeanName(BeanDefinition definition) {
        String beanClassName = definition.getBeanClassName();
        Assert.state(beanClassName != null, "No bean class name set");
        String shortClassName = ClassUtils.getShortName(beanClassName);
        return Introspector.decapitalize(shortClassName);
    }
}
