package org.example.neo4j.dao;

import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import org.example.neo4j.domain.Person;
import org.neo4j.driver.v1.StatementResultCursor;
import org.neo4j.driver.v1.Value;
import org.neo4j.driver.v1.types.Entity;

import com.github.andyshao.lang.NotSupportConvertException;
import com.github.andyshao.neo4j.io.DeSerializer;
import com.github.andyshao.neo4j.model.SqlMethod;

public class PersonMapper implements DeSerializer {

    @Override
    public CompletionStage<?> deSerialize(StatementResultCursor src , SqlMethod sqlMethod) throws NotSupportConvertException {
        return src.listAsync(record -> {
            Entity entity = record.get(0).asEntity();
            Person p = new Person();
            Value value = entity.get("name");
            p.setName(value.isNull() ? null : value.asString());
            value = entity.get("age");
            p.setAge(value.isNull() ? null : value.asInt());
            value = entity.get("phone");
            p.setPhone(value.isNull() ? null : value.asString());
            return p;
        }).thenApplyAsync(ls -> {
            return ls.stream().collect(Collectors.toMap(p -> p.getName() , p -> p));
        });
    }
}
