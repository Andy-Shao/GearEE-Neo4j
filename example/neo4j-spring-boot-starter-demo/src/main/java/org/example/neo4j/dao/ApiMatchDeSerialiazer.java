package org.example.neo4j.dao;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

import org.example.neo4j.domain.ApiMatch;
import org.neo4j.driver.v1.StatementResultCursor;
import org.neo4j.driver.v1.Value;
import org.neo4j.driver.v1.types.Entity;

import com.github.andyshao.lang.NotSupportConvertException;
import com.github.andyshao.neo4j.io.DeSerializer;
import com.github.andyshao.neo4j.model.SqlMethod;

public class ApiMatchDeSerialiazer implements DeSerializer {

    @Override
    public CompletionStage<?> deSerialize(StatementResultCursor src , SqlMethod sqlMethod) throws NotSupportConvertException {
        return src.nextAsync().thenApplyAsync(record -> {
            if(record == null) return Optional.empty();
            Entity entity = record.get(0).asEntity();
            ApiMatch apiMatch = new ApiMatch();
            Value apiMatchName = entity.get("apiMatchName");
            apiMatch.setApiMatchName(apiMatchName.isNull() ? null : apiMatchName.asString());
            return Optional.of(apiMatch);
        });
    }

}
