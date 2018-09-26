package com.github.andyshao.neo4j.io;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletionStage;

import org.neo4j.driver.internal.value.BooleanValue;
import org.neo4j.driver.internal.value.FloatValue;
import org.neo4j.driver.internal.value.IntegerValue;
import org.neo4j.driver.internal.value.StringValue;
import org.neo4j.driver.v1.Value;
import org.neo4j.driver.v1.types.Entity;
import org.neo4j.driver.v1.types.IsoDuration;

import com.github.andyshao.lang.NotSupportConvertException;
import com.github.andyshao.lang.StringOperation;
import com.github.andyshao.neo4j.mapper.IllegalConfigException;
import com.github.andyshao.neo4j.model.SqlMethod;
import com.github.andyshao.reflect.ConstructorOperation;
import com.github.andyshao.reflect.GenericNode;
import com.github.andyshao.reflect.MethodOperation;
import com.github.andyshao.time.LocalDateTimeOperation;

/**
 * 
 * Title:<br>
 * Descript:<br>
 * Copyright: Copryright(c) Aug 28, 2018<br>
 * Encoding:UNIX UTF-8
 * @author Andy.Shao
 *
 */
public final class DeSerializers {
    private DeSerializers() {}
    
    public static final void checkReturnType(SqlMethod sqlMethod) {
        GenericNode returnTypeInfo = sqlMethod.getSqlMethodRet().getReturnTypeInfo();
        if(!returnTypeInfo.getDeclareType().isAssignableFrom(CompletionStage.class)) {
            throw new IllegalConfigException(String.format("the return type of %s.%s should be %s in first level" , 
                sqlMethod.getDefinition().getDeclaringClass().getName(), 
                sqlMethod.getDefinition().getName(),
                CompletionStage.class));
        }
    }

    public static final Object formatValue(Class<?> clazz, Value value) {
        if(value.isNull()) return null;
        if(clazz.isAssignableFrom(Integer.class)) return (Integer)value.asInt();
        if(clazz.isAssignableFrom(Short.class)) return Short.valueOf(value.asString());
        if(clazz.isAssignableFrom(Character.class)) return (Character)value.asString().charAt(0);
        if(clazz.isAssignableFrom(Float.class)) return (Float)value.asFloat();
        if(clazz.isAssignableFrom(Double.class)) return (Double)value.asDouble();
        if(clazz.isAssignableFrom(Long.class)) return (Long)value.asLong();
        if(clazz.isAssignableFrom(Number.class)) return value.asNumber();
        if(clazz.isAssignableFrom(Boolean.class)) return (Boolean)value.asBoolean();
        if(clazz.isAssignableFrom(byte[].class)) return value.asByteArray();
        if(clazz.isAssignableFrom(LocalDate.class)) return value.asLocalDate();
        if(clazz.isAssignableFrom(LocalTime.class)) return value.asLocalTime();
        if(clazz.isAssignableFrom(LocalDateTime.class)) return value.asLocalDateTime();
        if(clazz.isAssignableFrom(OffsetTime.class)) return value.asOffsetTime();
        if(clazz.isAssignableFrom(IsoDuration.class)) return value.asIsoDuration();
        if(clazz.isAssignableFrom(ZonedDateTime.class)) return value.asZonedDateTime();
        if(clazz.isAssignableFrom(Date.class)) return LocalDateTimeOperation.toDate().convert(value.asLocalDateTime());
        if(clazz.isAssignableFrom(String.class)) return formatToString(value);
        if(clazz.isAssignableFrom(Enum.class)) return MethodOperation.invoked(null , 
            MethodOperation.getMethod(clazz , "valueOf" , String.class), value.asString());
        throw new NotSupportConvertException();
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
        if(declareType.isAssignableFrom(String.class)) return true;
        return false;
    }

    public static final Class<?> getReturnType(SqlMethod sqlMethod){
        GenericNode node = sqlMethod.getSqlMethodRet().getReturnTypeInfo();
        List<GenericNode> nodes = node.getComponentTypes();
        GenericNode tmp = nodes.get(0);
        Class<?> declareType = tmp.getDeclareType();
        tmp = tmp.getComponentTypes().get(0);
        declareType = tmp.getDeclareType();
        return declareType;
    }
    
    public static final Object formatJavaBean(Class<?> returnType, SqlMethod sqlMethod, Value val) {
        List<Method> setMethods = sqlMethod.getSqlMethodRet().getSetMethods(returnType);
        if(val.isNull()) return null;
        Entity entity = val.asEntity();
        if(entity.size() == 0) return null;
        Object tmp = ConstructorOperation.newInstance(returnType);
        for(Method setMethod : setMethods) {
            String key = setMethod.getName();
            key = StringOperation.replaceFirst(key , "set" , "");
            key = key.substring(0 , 1).toLowerCase() + key.substring(1);
            key = sqlMethod.getSqlMethodRet().getRealName(key , returnType);
            Object value = formatValue(setMethod.getParameterTypes()[0] , entity.get(key));
            if(value != null) MethodOperation.invoked(tmp , setMethod , value);
        }
        return tmp;
    }

    @Deprecated
    public static final Object formatJavaBean(Class<?> returnType , List<Method> setMethods , Value va) {
        if(va.isNull()) return null;
        Entity entity = va.asEntity();
        if(entity.size() == 0) return null;
        Object tmp = ConstructorOperation.newInstance(returnType);
        for(Method setMethod : setMethods) {
            String key = setMethod.getName();
            key = StringOperation.replaceFirst(key , "set" , "");
            key = key.substring(0 , 1).toLowerCase() + key.substring(1);
            Object value = formatValue(setMethod.getParameterTypes()[0] , entity.get(key));
            if(value != null) MethodOperation.invoked(tmp , setMethod , value);
        }
        return tmp;
    }
}
