package com.github.andyshao.neo4j.annotation;

import java.lang.annotation.*;

/**
 * Title: <br>
 * Description: All of methods must be <b style="color:blue;">public</b><br>
 * Copyright: Copyright(c) 2020/8/26
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
@Inherited
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Neo4jSqlClip {
    String sqlClipName() default "";
}
