package com.github.andyshao.neo4j.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * Title:<br>
 * Descript:<br>
 * Copyright: Copryright(c) Aug 3, 2018<br>
 * Encoding:UNIX UTF-8
 * @author Andy.Shao
 *
 */
@Inherited
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Match {
    String sql() default "";
    SqlInject sqlInject() default @SqlInject(sqlClipName = "");
    /**
     * a return type could be a java bean class, Integer, Long, Double, Float, String
     * @return the return type your method will taking
     */
    Class<?> returnType() default Object.class;
}
