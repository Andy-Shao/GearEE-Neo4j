package com.github.andyshao.neo4j.spring.process;

import com.github.andyshao.neo4j.domain.Neo4jDao;
import com.github.andyshao.neo4j.domain.analysis.Neo4jDaoAnalysis;
import com.github.andyshao.neo4j.process.DaoScanner;
import com.github.andyshao.reflect.ClassOperation;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.lang.Nullable;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2020/9/1
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
@Slf4j
public class ClassPathAnnotationDaoScanner implements DaoScanner {
    private String[] basePackages;
    private Package[] pkgs;
    @Nullable
    private Environment environment;
    @Nullable
    private ResourcePatternResolver resourcePatternResolver;
    @Nullable
    private MetadataReaderFactory metadataReaderFactory;

    public ClassPathAnnotationDaoScanner(String... basePackages) {
        this.basePackages = basePackages;
    }

    public ClassPathAnnotationDaoScanner(Package... pkgs) {
        this.pkgs = pkgs;
    }

    @Override
    public Map<String, Neo4jDao> scan() {
        if(Objects.nonNull(this.basePackages)) {
            return scanFromBasePackages();
        } else if(Objects.nonNull(this.pkgs)) {
            return Arrays.stream(this.pkgs)
                    .flatMap(pkg -> Neo4jDaoAnalysis.analyseDaoFromOnePackage(pkg).stream())
                    .collect(Collectors.toMap(Neo4jDao::getDaoId, it -> it));
        }
        return Maps.newHashMap();
    }

    private Map<String, Neo4jDao> scanFromBasePackages() {
        return Arrays.stream(this.basePackages)
                .flatMap(basePackage -> {
                    try {
                        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                                resolveBasePackage(basePackage) + '/' + "**/*.class";
                        Resource[] resources = getResourcePatternResolver().getResources(packageSearchPath);
                        final boolean traceEnabled = log.isTraceEnabled();
                        final boolean debugEnabled = log.isDebugEnabled();
                        return Arrays.stream(resources)
                                .flatMap(resource -> {
                                    if (traceEnabled) {
                                        log.trace("Scanning " + resource);
                                    }

                                    if (resource.isReadable()) {
                                        try {
                                            MetadataReader metadataReader = getMetadataReaderFactory().getMetadataReader(resource);
                                            if (isCandidateComponent(metadataReader)) {
                                                ScannedGenericBeanDefinition sbd = new ScannedGenericBeanDefinition(metadataReader);
                                                sbd.setSource(resource);
                                                if (debugEnabled) {
                                                    log.debug("Identified candidate component class: " + resource);
                                                }
                                                return Stream.of(ClassOperation.forName(sbd.getBeanClassName()));
                                            } else {
                                                if (debugEnabled) {
                                                    log.debug("Ignored because not a concrete top-level class: " + resource);
                                                }
                                            }
                                        } catch (Throwable ex) {
                                            throw new BeanDefinitionStoreException(
                                                    "Failed to read candidate component class: " + resource, ex);
                                        }
                                    }

                                    return Stream.empty();
                                })
                                .map(Neo4jDaoAnalysis::analyseDao);
                    } catch (IOException ex) {
                        throw new BeanDefinitionStoreException("I/O failure during classpath scanning", ex);
                    }
                })
                .collect(Collectors.toMap(Neo4jDao::getDaoId, it -> it));
    }

    private boolean isCandidateComponent(MetadataReader metadataReader) {
        AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
        Set<String> annotationTypes = annotationMetadata.getAnnotationTypes();
        return annotationTypes.stream()
                .anyMatch(it -> Objects.equals(it, com.github.andyshao.neo4j.annotation.Neo4jDao.class.getName()));
    }

    /**
     * Resolve the specified base package into a pattern specification for
     * the package search path.
     * <p>The default implementation resolves placeholders against system properties,
     * and converts a "."-based package path to a "/"-based resource path.
     * @param basePackage the base package as specified by the user
     * @return the pattern specification to be used for package searching
     */
    protected String resolveBasePackage(String basePackage) {
        return ClassUtils.convertClassNameToResourcePath(getEnvironment().resolveRequiredPlaceholders(basePackage));
    }

    public final Environment getEnvironment() {
        if (this.environment == null) {
            this.environment = new StandardEnvironment();
        }
        return this.environment;
    }

    private ResourcePatternResolver getResourcePatternResolver() {
        if (this.resourcePatternResolver == null) {
            this.resourcePatternResolver = new PathMatchingResourcePatternResolver();
        }
        return this.resourcePatternResolver;
    }

    /**
     * Return the MetadataReaderFactory used by this component provider.
     */
    public final MetadataReaderFactory getMetadataReaderFactory() {
        if (this.metadataReaderFactory == null) {
            this.metadataReaderFactory = new CachingMetadataReaderFactory();
        }
        return this.metadataReaderFactory;
    }
}
