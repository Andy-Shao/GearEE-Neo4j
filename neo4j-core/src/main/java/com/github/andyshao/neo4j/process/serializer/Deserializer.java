package com.github.andyshao.neo4j.process.serializer;

import com.github.andyshao.lang.NotSupportConvertException;
import org.neo4j.driver.Value;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2020/8/28
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
public interface Deserializer<T> {
    T decode(Value value) throws NotSupportConvertException;
}
