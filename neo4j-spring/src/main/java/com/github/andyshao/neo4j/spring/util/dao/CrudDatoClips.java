package com.github.andyshao.neo4j.spring.util.dao;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Map.Entry;

import org.neo4j.ogm.annotation.NodeEntity;

import com.github.andyshao.neo4j.annotation.SqlClip;
import com.github.andyshao.reflect.FieldOperation;
import com.google.common.collect.Maps;

public class CrudDatoClips {
    @SqlClip(sqlClipName = "saveSelective")
    public <D> String saveSelective(D data) {
        String label;
        NodeEntity nodeEntity = data.getClass().getAnnotation(NodeEntity.class);
        if(nodeEntity == null) label = data.getClass().getSimpleName();
        else label = nodeEntity.label();
        
        StringBuilder query = new StringBuilder("CREATE (n:").append(label).append(")");
        Field[] fields = FieldOperation.superGetDeclaredFields(data.getClass());
        Map<String, Field> tmp = Maps.newHashMap();
        for(Field field : fields) {
            int modifiers = field.getModifiers();
            if(!(Modifier.isFinal(modifiers) || Modifier.isStatic(modifiers))) 
                tmp.put(field.getName() , field);
        }
        if(!tmp.isEmpty()) {
            boolean isHeader = true;
            for(Entry<String , Field> entry : tmp.entrySet()) {
                if(isHeader) {
                    isHeader = false;
                    query.append(" SET n.").append(entry.getKey()).append(" = ");
                } else {
                    query.append(", n.").append(entry.getKey()).append(" = ");
                }
            }
        }
        query.append(" RETURN n");
        return query.toString();
    }
}
