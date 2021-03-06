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
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface FormatterResult {
    @SuppressWarnings("rawtypes")
    Class<? extends com.github.andyshao.neo4j.process.serializer.FormatterResult> value();
}
