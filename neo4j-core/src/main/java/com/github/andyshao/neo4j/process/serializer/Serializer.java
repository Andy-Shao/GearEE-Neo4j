package com.github.andyshao.neo4j.process.serializer;

import com.github.andyshao.lang.NotSupportConvertException;
import org.neo4j.driver.Value;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2020/8/27
 * Encoding: UNIX UTF-8
 * @param <T> data type
 *
 * @author Andy.Shao
 */
public interface Serializer<T> {
    Value encode(T data) throws NotSupportConvertException;
}
