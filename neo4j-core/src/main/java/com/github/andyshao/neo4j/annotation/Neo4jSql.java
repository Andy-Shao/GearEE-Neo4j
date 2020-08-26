package com.github.andyshao.neo4j.annotation;

import java.lang.annotation.*;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2020/8/26
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
@Inherited
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Neo4jSql {
    String sql() default "";
    String sqlClipName() default "";
    boolean isUseSqlClip() default false;
}
