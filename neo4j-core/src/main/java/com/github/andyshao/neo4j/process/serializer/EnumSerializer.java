package com.github.andyshao.neo4j.process.serializer;

import com.github.andyshao.lang.NotSupportConvertException;
import org.neo4j.driver.Value;
import org.neo4j.driver.Values;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2020/8/27
 * Encoding: UNIX UTF-8
 * @param <T> Data type
 *
 * @author Andy.Shao
 */
@SuppressWarnings("ALL")
public class EnumSerializer<T extends Enum> implements Serializer<T> {
    @Override
    public Value encode(T data) throws NotSupportConvertException {
        return Values.value(data.name());
    }
}
