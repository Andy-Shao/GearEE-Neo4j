package com.github.andyshao.neo4j.io;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import org.neo4j.driver.v1.StatementResultCursor;

import com.github.andyshao.lang.NotSupportConvertException;
import com.github.andyshao.neo4j.model.SqlMethod;
import com.github.andyshao.reflect.GenericNode;

import lombok.RequiredArgsConstructor;

/**
 * 
 * Title:<br>
 * Descript:<br>
 * Copyright: Copryright(c) Aug 28, 2018<br>
 * Encoding:UNIX UTF-8
 * @author Andy.Shao
 *
 */
@RequiredArgsConstructor
public class BaseTypeDeSerializer implements DeSerializer {
    private final DeSerializer next;

    @Override
    public CompletionStage<?> deSerialize(StatementResultCursor src , SqlMethod sqlMethod) throws NotSupportConvertException {
        GenericNode returnTypeInfo = sqlMethod.getReturnTypeInfo();
        if(!shouldProcess(returnTypeInfo)) return next.deSerialize(src , sqlMethod);
        Class<?> returnType = getReturnType(returnTypeInfo);
//        if(returnType.isAssignableFrom(Integer.class)) return asInteger(src , sqlMethod);
//        if(returnType.isAssignableFrom(Short.class)) return asShort(src , sqlMethod);
//        if(returnType.isAssignableFrom(Character.class)) return asChar(src, sqlMethod);
//        if(returnType.isAssignableFrom(Float.class)) return asFloat(src,sqlMethod);
//        if(returnType.isAssignableFrom(Double.class)) return asDouble(src,sqlMethod);
//        if(returnType.isAssignableFrom(Long.class)) return asLong(src,sqlMethod);
//        if(returnType.isAssignableFrom(Number.class)) return asNumber(src,sqlMethod);
//        if(returnType.isAssignableFrom(Boolean.class)) return asBoolean(src,sqlMethod);
//        if(returnType.isAssignableFrom(byte[].class)) return asByteArray(src,sqlMethod);
//        if(returnType.isAssignableFrom(LocalDate.class)) return asLocalDate(src,sqlMethod);
//        if(returnType.isAssignableFrom(LocalTime.class)) return asLocalTime(src, sqlMethod);
//        if(returnType.isAssignableFrom(LocalDateTime.class)) return asLocalDateTime(src, sqlMethod);
//        if(returnType.isAssignableFrom(OffsetTime.class)) return asOffsetTime(src, sqlMethod);
//        if(returnType.isAssignableFrom(IsoDuration.class)) return asIsoDuration(src, sqlMethod);
//        if(returnType.isAssignableFrom(ZonedDateTime.class)) return asZonedDateTime(src, sqlMethod);
//        if(returnType.isAssignableFrom(Date.class)) return asDate(src, sqlMethod);
//        return next.deSerialize(src , sqlMethod);
        return src.singleAsync().thenApplyAsync(record -> Optional.ofNullable(DeSerializers.formatValue(returnType , record.get(0))));
    }
    
//    static final CompletionStage<Optional<Date>> asDate(StatementResultCursor src , SqlMethod sqlMethod) {
//        return asLocalDateTime(src , sqlMethod).thenApplyAsync(op -> op.isPresent() ? Optional.empty() : Optional.of(LocalDateTimeOperation.toDate().convert(op.get())));
//    }
//
//    static final CompletionStage<Optional<ZonedDateTime>> asZonedDateTime(StatementResultCursor src , SqlMethod sqlMethod) {
//        return src.singleAsync().thenApplyAsync(record -> record.get(0).isNull() ? Optional.empty() : Optional.of(record.get(0).asZonedDateTime()));
//    }
//
//    static final CompletionStage<Optional<IsoDuration>> asIsoDuration(StatementResultCursor src , SqlMethod sqlMethod) {
//        return src.singleAsync().thenApplyAsync(record -> record.get(0).isNull() ? Optional.empty() : Optional.of(record.get(0).asIsoDuration()));
//    }
//
//    static final CompletionStage<Optional<OffsetTime>> asOffsetTime(StatementResultCursor src , SqlMethod sqlMethod) {
//        return src.singleAsync().thenApplyAsync(record -> record.get(0).isNull() ? Optional.empty() : Optional.of(record.get(0).asOffsetTime()));
//    }
//
//    static final CompletionStage<Optional<LocalDateTime>> asLocalDateTime(StatementResultCursor src , SqlMethod sqlMethod) {
//        return src.singleAsync().thenApplyAsync(record -> record.get(0).isNull() ? Optional.empty() : Optional.of(record.get(0).asLocalDateTime()));
//    }
//
//    static final CompletionStage<Optional<LocalTime>> asLocalTime(StatementResultCursor src , SqlMethod sqlMethod) {
//        return src.singleAsync().thenApplyAsync(record -> record.get(0).isNull() ? Optional.empty() : Optional.of(record.get(0).asLocalTime()));
//    }
//
//    static final CompletionStage<Optional<LocalDate>> asLocalDate(StatementResultCursor src , SqlMethod sqlMethod) {
//        return src.singleAsync().thenApplyAsync(record -> record.get(0).isNull() ? Optional.empty() : Optional.of(record.get(0).asLocalDate()));
//    }
//
//    static final CompletionStage<Optional<byte[]>> asByteArray(StatementResultCursor src , SqlMethod sqlMethod) {
//        return src.singleAsync().thenApplyAsync(record -> record.get(0).isNull() ? Optional.empty() : Optional.of(record.get(0).asByteArray()));
//    }
//
//    static final CompletionStage<Optional<Boolean>> asBoolean(StatementResultCursor src , SqlMethod sqlMethod) {
//        return src.singleAsync().thenApplyAsync(record -> record.get(0).isNull() ? Optional.empty() : Optional.of(record.get(0).asBoolean()));
//    }
//
//    static final CompletionStage<Optional<Number>> asNumber(StatementResultCursor src , SqlMethod sqlMethod) {
//        return src.singleAsync().thenApplyAsync(record -> record.get(0).isNull() ? Optional.empty() : Optional.of(record.get(0).asNumber()));
//    }
//
//    static final CompletionStage<Optional<Long>> asLong(StatementResultCursor src , SqlMethod sqlMethod) {
//        return src.singleAsync().thenApplyAsync(record -> record.get(0).isNull() ? Optional.empty() : Optional.of(record.get(0).asLong()));
//    }
//
//    static final CompletionStage<Optional<Double>> asDouble(StatementResultCursor src , SqlMethod sqlMethod) {
//        return src.singleAsync().thenApplyAsync(record -> record.get(0).isNull() ? Optional.empty() : Optional.of(record.get(0).asDouble()));
//    }
//
//    static final CompletionStage<Optional<Float>> asFloat(StatementResultCursor src , SqlMethod sqlMethod) {
//        return src.singleAsync().thenApplyAsync(record -> record.get(0).isNull() ? Optional.empty() : Optional.of(record.get(0).asFloat()));
//    }
//
//    static final CompletionStage<Optional<Character>> asChar(StatementResultCursor src , SqlMethod sqlMethod) {
//        return src.singleAsync().thenApplyAsync(record -> record.get(0).isNull() ? Optional.empty() : Optional.of(record.get(0).asString().charAt(0)));
//    }
//
//    static final CompletionStage<Optional<Short>> asShort(StatementResultCursor src , SqlMethod sqlMethod){
//        return src.singleAsync().thenApplyAsync(record -> record.get(0).isNull() ? Optional.empty() : Optional.of(Short.valueOf(record.get(0).asString())));
//    }
//
//    static final CompletionStage<Optional<Integer>> asInteger(StatementResultCursor src , SqlMethod sqlMethod){
//        return src.singleAsync().thenApplyAsync(record -> record.get(0).isNull() ? Optional.empty(): Optional.of(record.get(0).asInt()));
//    }

    static final boolean shouldProcess(GenericNode node){
        List<GenericNode> nodes = node.getComponentTypes();
        if(nodes.size() != 1) return false;
        GenericNode tmp = nodes.get(0);
        Class<?> declareType = tmp.getDeclareType();
        if(!declareType.isAssignableFrom(Optional.class)) return false;
        tmp = tmp.getComponentTypes().get(0);
        declareType = tmp.getDeclareType();
        return DeSerializers.isBaseType(declareType);
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
