package com.github.andyshao.neo4j.spring.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import com.github.andyshao.neo4j.spring.conf.DefaultNeo4jConf;

/**
 * 
 * Title:<br>
 * Descript:<br>
 * Copyright: Copryright(c) Aug 31, 2018<br>
 * Encoding:UNIX UTF-8
 * @author Andy.Shao
 *
 */
@Inherited
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({DefaultNeo4jConf.class})
public @interface EnableNeo4jDao {
    Class<?>[] packageClasses();
}
