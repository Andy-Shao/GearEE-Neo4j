package com.github.andyshao.neo4j.io;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import org.neo4j.driver.v1.StatementResultCursor;
import org.neo4j.driver.v1.types.IsoDuration;

import com.github.andyshao.lang.NotSupportConvertException;
import com.github.andyshao.neo4j.model.SqlMethod;
import com.github.andyshao.reflect.GenericNode;

/**
 * 
 * Title:<br>
 * Descript:<br>
 * Copyright: Copryright(c) Aug 28, 2018<br>
 * Encoding:UNIX UTF-8
 * @author Andy.Shao
 *
 */
public class BaseTypeDeSerializer implements DeSerializer {
    private DeSerializer next;

    @Override
    public CompletionStage<?> serialize(StatementResultCursor src , SqlMethod sqlMethod) throws NotSupportConvertException {
        GenericNode returnTypeInfo = sqlMethod.getReturnTypeInfo();
        if(!shouldProcess(returnTypeInfo)) return next.serialize(src , sqlMethod);
        Class<?> returnType = getReturnType(returnTypeInfo);
        if(returnType.isAssignableFrom(Integer.class)) return asInteger(src , sqlMethod);
        if(returnType.isAssignableFrom(Short.class)) return asShort(src , sqlMethod);
        if(returnType.isAssignableFrom(Character.class)) return asChar(src, sqlMethod);
        if(returnType.isAssignableFrom(Float.class)) return asFloat(src,sqlMethod);
        if(returnType.isAssignableFrom(Double.class)) return asDouble(src,sqlMethod);
        if(returnType.isAssignableFrom(Long.class)) return asLong(src,sqlMethod);
        if(returnType.isAssignableFrom(Number.class)) return asNumber(src,sqlMethod);
        if(returnType.isAssignableFrom(Boolean.class)) return asBoolean(src,sqlMethod);
        if(returnType.isAssignableFrom(byte[].class)) return asByteArray(src,sqlMethod);
        if(returnType.isAssignableFrom(LocalDate.class)) return asLocalDate(src,sqlMethod);
        if(returnType.isAssignableFrom(LocalTime.class)) return asLocalTime(src, sqlMethod);
        if(returnType.isAssignableFrom(LocalDateTime.class)) return asLocalDateTime(src, sqlMethod);
        if(returnType.isAssignableFrom(OffsetTime.class)) return asOffsetTime(src, sqlMethod);
        if(returnType.isAssignableFrom(IsoDuration.class)) return asIsoDuration(src, sqlMethod);
        if(returnType.isAssignableFrom(ZonedDateTime.class)) return asZonedDateTime(src, sqlMethod);
        if(returnType.isAssignableFrom(Date.class)) return asDate(src, sqlMethod);
        // TODO Auto-generated method stub
        return null;
    }
    
    static final CompletionStage<Long> asLong(StatementResultCursor src , SqlMethod sqlMethod) {
        // TODO Auto-generated method stub
        return null;
    }

    static final CompletionStage<Optional<Double>> asDouble(StatementResultCursor src , SqlMethod sqlMethod) {
        return src.singleAsync().thenApplyAsync(record -> record.get(0).isNull() ? Optional.empty() : Optional.of(record.get(0).asDouble()));
    }

    static final CompletionStage<Optional<Float>> asFloat(StatementResultCursor src , SqlMethod sqlMethod) {
        return src.singleAsync().thenApplyAsync(record -> record.get(0).isNull() ? Optional.empty() : Optional.of(record.get(0).asFloat()));
    }

    static final CompletionStage<Optional<Character>> asChar(StatementResultCursor src , SqlMethod sqlMethod) {
        return src.singleAsync().thenApplyAsync(record -> record.get(0).isNull() ? Optional.empty() : Optional.of(record.get(0).asString().charAt(0)));
    }

    static final CompletionStage<Optional<Short>> asShort(StatementResultCursor src , SqlMethod sqlMethod){
        return src.singleAsync().thenApplyAsync(record -> record.get(0).isNull() ? Optional.empty() : Optional.of(Short.valueOf(record.get(0).asString())));
    }
    
    static final CompletionStage<Optional<Integer>> asInteger(StatementResultCursor src , SqlMethod sqlMethod){
        return src.singleAsync().thenApplyAsync(record -> record.get(0).isNull() ? Optional.empty(): Optional.of(record.get(0).asInt()));
    }

    public static final boolean shouldProcess(GenericNode node){
        List<GenericNode> nodes = node.getComponentTypes();
        if(nodes.size() != 1) return false;
        GenericNode tmp = nodes.get(0);
        Class<?> declareType = tmp.getDeclareType();
        if(!declareType.isAssignableFrom(Optional.class)) return false;
        tmp = tmp.getComponentTypes().get(0);
        declareType = tmp.getDeclareType();
        if(declareType.isAssignableFrom(Integer.class)) return true;
        if(declareType.isAssignableFrom(Short.class)) return true;
        if(declareType.isAssignableFrom(Character.class)) return true;
        if(declareType.isAssignableFrom(Float.class)) return true;
        if(declareType.isAssignableFrom(Double.class)) return true;
        if(declareType.isAssignableFrom(Long.class)) return true;
        if(declareType.isAssignableFrom(Number.class)) return true;
        if(declareType.isAssignableFrom(Boolean.class)) return true;
        if(declareType.isAssignableFrom(byte[].class)) return true;
        if(declareType.isAssignableFrom(LocalDate.class)) return true;
        if(declareType.isAssignableFrom(LocalTime.class)) return true;
        if(declareType.isAssignableFrom(LocalDateTime.class)) return true;
        if(declareType.isAssignableFrom(OffsetTime.class)) return true;
        if(declareType.isAssignableFrom(IsoDuration.class)) return true;
        if(declareType.isAssignableFrom(ZonedDateTime.class)) return true;
        if(declareType.isAssignableFrom(Date.class)) return true;
        return false;
    }
    
    public static final Class<?> getReturnType(GenericNode node){
        List<GenericNode> nodes = node.getComponentTypes();
        GenericNode tmp = nodes.get(0);
        Class<?> declareType = tmp.getDeclareType();
        tmp = tmp.getComponentTypes().get(0);
        declareType = tmp.getDeclareType();
        return declareType;
    }
}
