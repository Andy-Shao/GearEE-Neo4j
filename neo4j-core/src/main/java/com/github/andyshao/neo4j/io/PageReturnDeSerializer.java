package com.github.andyshao.neo4j.io;

import java.util.concurrent.CompletionStage;

import org.neo4j.driver.v1.StatementResultCursor;

import com.github.andyshao.lang.NotSupportConvertException;
import com.github.andyshao.neo4j.model.SqlMethod;

public class PageReturnDeSerializer implements DeSerializer {

    @Override
    public CompletionStage<?> deSerialize(StatementResultCursor src , SqlMethod sqlMethod) throws NotSupportConvertException {
        // TODO Auto-generated method stub
        return null;
    }

}
