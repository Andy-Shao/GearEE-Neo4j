package com.github.andyshao.neo4j.io;

import java.util.concurrent.CompletionStage;

import org.neo4j.driver.v1.StatementResultCursor;

import com.github.andyshao.lang.NotSupportConvertException;
import com.github.andyshao.neo4j.model.SqlMethod;

/**
 * 
 * Title:<br>
 * Descript:<br>
 * Copyright: Copryright(c) Aug 29, 2018<br>
 * Encoding:UNIX UTF-8
 * @author Andy.Shao
 *
 */
public class DefaultDeSerializer implements DeSerializer {

    @Override
    public CompletionStage<?> deSerialize(StatementResultCursor src , SqlMethod sqlMethod) throws NotSupportConvertException {
        throw new NotSupportConvertException(String.format("Not support return type is %s" , sqlMethod.getDefinition().getReturnType().getName()));
    }

}
