package com.github.andyshao.neo4j.process.serializer;

import com.github.andyshao.lang.NotSupportConvertException;
import com.github.andyshao.lang.StringOperation;
import com.github.andyshao.neo4j.domain.Neo4jEntity;
import com.github.andyshao.neo4j.domain.Neo4jEntityField;
import com.github.andyshao.reflect.ClassOperation;
import com.github.andyshao.reflect.ConstructorOperation;
import com.github.andyshao.reflect.MethodOperation;
import com.github.andyshao.time.LocalDateTimeOperation;
import org.neo4j.driver.Value;
import org.neo4j.driver.internal.value.BooleanValue;
import org.neo4j.driver.internal.value.FloatValue;
import org.neo4j.driver.internal.value.IntegerValue;
import org.neo4j.driver.internal.value.StringValue;
import org.neo4j.driver.types.Entity;
import org.neo4j.driver.types.IsoDuration;

import java.lang.reflect.Method;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 
 * Title:<br>
 * Descript:<br>
 * Copyright: Copryright(c) Aug 28, 2018<br>
 * Encoding:UNIX UTF-8
 * @author Andy.Shao
 *
 */
public final class Deserializers {
    private Deserializers() {}

    public static final Object formatValue(Class<?> clazz, Value value) {
        if(value.isNull()) return null;
        if(clazz.isAssignableFrom(Integer.class)) return (Integer)value.asInt();
        if(clazz.isAssignableFrom(int.class)) return value.asInt();
        if(clazz.isAssignableFrom(Short.class)) return Short.valueOf(value.asString());
        if(clazz.isAssignableFrom(short.class)) return Short.valueOf((value.asString()));
        if(clazz.isAssignableFrom(Character.class)) return (Character)value.asString().charAt(0);
        if(clazz.isAssignableFrom(char.class)) return (Character)value.asString().charAt(0);
        if(clazz.isAssignableFrom(Float.class)) return (Float)value.asFloat();
        if(clazz.isAssignableFrom(float.class)) return value.asFloat();
        if(clazz.isAssignableFrom(Double.class)) return (Double)value.asDouble();
        if(clazz.isAssignableFrom(double.class)) return value.asDouble();
        if(clazz.isAssignableFrom(Long.class)) return (Long)value.asLong();
        if(clazz.isAssignableFrom(long.class)) return value.asLong();
        if(clazz.isAssignableFrom(Number.class)) return value.asNumber();
        if(clazz.isAssignableFrom(Boolean.class)) return (Boolean)value.asBoolean();
        if(clazz.isAssignableFrom(byte[].class)) return value.asByteArray();
        if(clazz.isAssignableFrom(LocalDate.class)) return value.asLocalDate();
        if(clazz.isAssignableFrom(LocalTime.class)) return value.asLocalTime();
        if(clazz.isAssignableFrom(LocalDateTime.class)) return value.asLocalDateTime();
        if(clazz.isAssignableFrom(OffsetTime.class)) return value.asOffsetTime();
        if(clazz.isAssignableFrom(OffsetDateTime.class)) return value.asOffsetDateTime();
        if(clazz.isAssignableFrom(IsoDuration.class)) return value.asIsoDuration();
        if(clazz.isAssignableFrom(ZonedDateTime.class)) return value.asZonedDateTime();
        if(clazz.isAssignableFrom(Date.class)) return LocalDateTimeOperation.toDate().convert(value.asLocalDateTime());
        if(clazz.isAssignableFrom(String.class)) return formatToString(value);
        if(Enum.class.isAssignableFrom(clazz)) return MethodOperation.invoked(null ,
            MethodOperation.getMethod(clazz , "valueOf" , String.class), value.asString());

        if(List.class.isAssignableFrom(clazz)) return value.asList();
        if(Collection.class.isAssignableFrom(clazz)) return formatCollection(clazz, value);
        throw new NotSupportConvertException("The class type is out of the default Deserializers!");
    }

    private static Object formatCollection(Class<?> clazz, Value value) {
        Collection<Object> result = (Collection<Object>) ClassOperation.newInstance(clazz);
        result.addAll(value.asList());
        return result;
    }

    public static final String formatToString(Value value) {
        if(value instanceof StringValue) return value.asString();
        else if(value instanceof IntegerValue) return String.valueOf(value.asInt());
        else if(value instanceof FloatValue) return String.valueOf(value.asFloat());
        else if(value instanceof BooleanValue) return String.valueOf(value.asBoolean());
        throw new NotSupportConvertException();
    }

    public static final boolean isBaseType(Class<?> declareType) {
        if(declareType.isAssignableFrom(Integer.class)) return true;
        if(declareType.isAssignableFrom(int.class)) return true;
        if(declareType.isAssignableFrom(Short.class)) return true;
        if(declareType.isAssignableFrom(short.class)) return true;
        if(declareType.isAssignableFrom(Character.class)) return true;
        if(declareType.isAssignableFrom(char.class)) return true;
        if(declareType.isAssignableFrom(Float.class)) return true;
        if(declareType.isAssignableFrom(float.class)) return true;
        if(declareType.isAssignableFrom(Double.class)) return true;
        if(declareType.isAssignableFrom(double.class)) return true;
        if(declareType.isAssignableFrom(Long.class)) return true;
        if(declareType.isAssignableFrom(long.class)) return true;
        if(declareType.isAssignableFrom(Number.class)) return true;
        if(declareType.isAssignableFrom(Boolean.class)) return true;
        if(declareType.isAssignableFrom(boolean.class)) return true;
        if(declareType.isAssignableFrom(byte[].class)) return true;
        if(declareType.isAssignableFrom(LocalDate.class)) return true;
        if(declareType.isAssignableFrom(LocalTime.class)) return true;
        if(declareType.isAssignableFrom(LocalDateTime.class)) return true;
        if(declareType.isAssignableFrom(OffsetTime.class)) return true;
        if(declareType.isAssignableFrom(OffsetDateTime.class)) return true;
        if(declareType.isAssignableFrom(IsoDuration.class)) return true;
        if(declareType.isAssignableFrom(ZonedDateTime.class)) return true;
        if(declareType.isAssignableFrom(Date.class)) return true;
        if(declareType.isAssignableFrom(String.class)) return true;
        if(declareType.isAssignableFrom(Void.class)) return true;
        return false;
    }
    
    public static final Object formatJavaBean(Class<?> returnType, Neo4jEntity neo4jEntity, Value val) {
        Map<String, Neo4jEntityField> entityFieldMap = neo4jEntity.getFields()
                .stream()
                .collect(Collectors.toMap(it -> it.getDefinition().getName(), it -> it));
        List<Method> setMethods = MethodOperation.getSetMethods(returnType);
        if(val.isNull()) return null;
        Entity entity = val.asEntity();
        if(entity.size() == 0) return null;
        Object tmp = ConstructorOperation.newInstance(returnType);
        for(Method setMethod : setMethods) {
            String key = setMethod.getName();
            key = StringOperation.replaceFirst(key , "set" , "");
            key = key.substring(0 , 1).toLowerCase() + key.substring(1);
            Neo4jEntityField entityField = entityFieldMap.get(key);
            Class<? extends Deserializer> deserializerClass = entityField.getDeserializer();
            Object value;
            if(Objects.nonNull(deserializerClass)) {
                Deserializer deserializer = ClassOperation.newInstance(deserializerClass);
                value = deserializer.decode(entity.get(key));
            } else value = formatValue(setMethod.getParameterTypes()[0] , entity.get(key));
            if(value != null) MethodOperation.invoked(tmp , setMethod , value);
        }
        return tmp;
    }
}
