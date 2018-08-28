package com.github.andyshao.neo4j.annotation;

import com.github.andyshao.neo4j.io.DeSerializer;

/**
 * 
 * Title:<br>
 * Descript:<br>
 * Copyright: Copryright(c) Aug 28, 2018<br>
 * Encoding:UNIX UTF-8
 * @author Andy.Shao
 *
 */
public @interface ResultFormat {
    Class<? extends DeSerializer> value();
}
