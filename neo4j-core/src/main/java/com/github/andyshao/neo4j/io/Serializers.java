package com.github.andyshao.neo4j.io;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import com.github.andyshao.lang.ArrayWrapper;
import com.github.andyshao.lang.Convert;
import com.github.andyshao.lang.NotSupportConvertException;

/**
 * 
 * Title:<br>
 * Descript:<br>
 * Copyright: Copryright(c) Aug 8, 2018<br>
 * Encoding:UNIX UTF-8
 * @author Andy.Shao
 *
 */
public final class Serializers {
    private Serializers() {}
    
    public static final Convert<Integer , String> fromInt = i -> i.toString();
    public static final Convert<Character, String> fromC = c -> String.valueOf(c);
    public static final Convert<Short, String> fromShort = s -> s.toString();
    public static final Convert<Long, String> fromLong = l -> l.toString();
    public static final Convert<Float, String> fromFloat = f -> f.toString();
    public static final Convert<Double , String> fromDouble = d -> d.toString();
    public static final Convert<Date , String> fromDate = d -> String.valueOf(d.getTime());
    public static final Convert<LocalDate , String> fromLocalDate = d -> d.format(DateTimeFormatter.ISO_LOCAL_DATE);
    public static final Convert<LocalDateTime, String> fromLocalDateTime = ldt -> ldt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    public static final Convert<Object, String> fromObj = o -> String.format("'%s'" , o.toString());
    
    public static final String defaultSerializer(Object data, Convert<Object , String> others) {
        try{
            String ret = others.convert(data);
            return ret;
        }catch(NotSupportConvertException ex) {
            if(data.getClass().isArray()) return defaultSerializerArray(ArrayWrapper.wrap(data), others);
            if(data instanceof Collection) return defaultSerializerList((Collection<?>) data, others);
            if(data instanceof Map) return defaultSerializerMap((Map<? , ?>) data, others);
            
            if(data instanceof Integer) return fromInt.convert((Integer) data);
            if(data instanceof Character) return fromC.convert((Character) data);
            if(data instanceof Short) return fromShort.convert((Short) data);
            if(data instanceof Long) return fromLong.convert((Long) data);
            if(data instanceof Float) return fromFloat.convert((Float) data);
            if(data instanceof Double) return fromDouble.convert((Double) data);
            if(data instanceof Date) return fromDate.convert((Date) data);
            if(data instanceof LocalDate) return fromLocalDate.convert((LocalDate) data);
            if(data instanceof LocalDateTime) return fromLocalDateTime.convert((LocalDateTime) data);
            return fromObj.convert(data);
        }
    }
    
    public static final String defaultSerializerArray(ArrayWrapper data, Convert<Object , String> others) {
        StringBuilder ret = new StringBuilder("[");
        for(Object obj : data) {
            if(obj instanceof Collection || obj instanceof Map) throw new NotSupportConvertException("Cannot over 1 list or map in one parameter");
            ret.append(defaultSerializer(obj, others)).append(",");
        }
        if(data.capacity() > 0) ret.delete(ret.length() - 1 , ret.length());
        ret.append("]");
        return ret.toString();
    }
    
    public static final String defaultSerializerList(Collection<?> data, Convert<Object, String> others) {
        return new StringBuilder().toString();
    }
    
    public static final String defaultSerializerMap(Map<?, ?> data, Convert<Object, String> others) {
        StringBuilder ret = new StringBuilder("\"{");
        for(Entry<? , ?> entry : data.entrySet()) {
            if(entry.getKey() instanceof Collection || entry.getKey() instanceof Map) 
                throw new NotSupportConvertException("Cannot over 1 list or map in one parameter");
            if(entry.getValue() instanceof Collection || entry.getValue() instanceof Map) 
                throw new NotSupportConvertException("Cannot over 1 list or map in one parameter");
            ret.append(defaultSerializer(entry.getKey() , others)).append(" = ")
                .append(defaultSerializer(entry.getValue() , others)).append(",");
        }
        if(data.size() > 0) ret.delete(ret.length() - 1 , ret.length());
        ret.append("}\"");
        return ret.toString();
    }
}
