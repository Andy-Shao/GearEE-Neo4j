package com.github.andyshao.neo4j.io;

import com.github.andyshao.lang.NotSupportConvertException;

public class DefaultSerializer implements Serializer {

    @Override
    public String serialize(Object input) {
        return Serializers.defaultSerializer(input , it -> {
            throw new NotSupportConvertException();
        });
    }

}
