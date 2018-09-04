package com.github.andyshao.neo4j.model;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicReference;

import com.github.andyshao.neo4j.annotation.Alias;
import com.github.andyshao.reflect.FieldOperation;
import com.github.andyshao.reflect.GenericNode;
import com.github.andyshao.reflect.MethodOperation;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

/**
 * 
 * Title:<br>
 * Descript:<br>
 * Copyright: Copryright(c) Sep 4, 2018<br>
 * Encoding:UNIX UTF-8
 * @author Andy.Shao
 *
 */
@Data
@SuppressWarnings("serial")
public class SqlMethodRet implements Serializable{
    @Getter(AccessLevel.PRIVATE)
    private final AtomicReference<List<Method>> setMethods = new AtomicReference<>();
    @Getter(AccessLevel.PRIVATE)
    private final ConcurrentMap<String , String> realFieldName = new ConcurrentHashMap<>();
    private GenericNode returnTypeInfo;
    
    public List<Method> getSetMethods(Class<?> returnType){
        while(true){
            if(setMethods.get() == null) {
                synchronized (setMethods) {
                    if(setMethods.get() == null) {
                        setMethods.set(MethodOperation.getSetMethods(returnType));
                        return setMethods.get();
                    } else continue;
                }
            } else return setMethods.get();
        }
    }
    
    public String getRealName(String fieldName, Class<?> clazz) {
        return realFieldName.computeIfAbsent(fieldName , key -> {
            Field field = FieldOperation.superGetDeclaredField(clazz , fieldName);
            Alias alias = field.getAnnotation(Alias.class);
            if(alias == null) return fieldName;
            else return alias.value();
        });
    }
}
