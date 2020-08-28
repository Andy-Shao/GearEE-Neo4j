package com.github.andyshao.neo4j.spring.annotation;

import com.github.andyshao.neo4j.spring.config.DefaultNeo4jConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2020/8/28
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
@Deprecated
@Inherited
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({DefaultNeo4jConfiguration.class})
public @interface EnableNeo4jDao {
    Class<?>[] packageClasses();
}
