package com.github.andyshao.neo4j.io;

import com.github.andyshao.lang.NotSupportConvertException;

/**
 * 
 * Title:<br>
 * Descript:<br>
 * Copyright: Copryright(c) Aug 17, 2018<br>
 * Encoding:UNIX UTF-8
 * @author Andy.Shao
 *
 */
public interface Serializer {
    String serialize(Object input) throws NotSupportConvertException;
}