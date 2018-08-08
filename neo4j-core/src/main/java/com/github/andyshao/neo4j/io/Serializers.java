package com.github.andyshao.neo4j.io;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import com.github.andyshao.lang.ArrayWrapper;
import com.github.andyshao.lang.Convert;

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
    
    public static final <T> String defaultSerializer(T data) {
        if(data.getClass().isArray()) return defaultSerializerArray(ArrayWrapper.wrap(data));
        
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
    
    public static final String defaultSerializerArray(ArrayWrapper data) {
        StringBuilder ret = new StringBuilder("[");
        for(Object obj : data) {
            ret.append(defaultSerializer(obj)).append(",");
        }
        if(data.capacity() > 0) ret.delete(ret.length() - 1 , ret.length());
        ret.append("]");
        return ret.toString();
    }
    
    public static final String defaultSerializerList(Collection<?> data) {
        return new StringBuilder().toString();
    }
    
    public static final String defaultSerializerMap(Map<?, ?> data) {
        return new StringBuilder().toString();
    }
}
