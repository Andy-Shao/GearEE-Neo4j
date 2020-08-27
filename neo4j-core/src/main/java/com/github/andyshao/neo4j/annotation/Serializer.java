package com.github.andyshao.neo4j.annotation;

import java.lang.annotation.*;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2020/8/27
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
@Inherited
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Serializer {
    @SuppressWarnings("rawtypes")
    Class<? extends com.github.andyshao.neo4j.process.serializer.Serializer> value();
}
