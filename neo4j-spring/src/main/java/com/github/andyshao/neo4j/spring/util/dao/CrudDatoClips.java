package com.github.andyshao.neo4j.spring.util.dao;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import org.neo4j.ogm.annotation.NodeEntity;

import com.github.andyshao.neo4j.annotation.SqlClip;
import com.github.andyshao.reflect.FieldOperation;

public class CrudDatoClips {
    @SqlClip(sqlClipName = "saveSelective")
    public <D> String saveSelective(D data) {
        String label = findLabel(data.getClass());
        
        StringBuilder query = new StringBuilder("CREATE (n:").append(label).append(")");
        Field[] fields = FieldOperation.superGetDeclaredFields(data.getClass());
        boolean isHeader = false;
        for(Field field : fields) {
            int modifiers = field.getModifiers();
            if(Modifier.isStatic(modifiers) || Modifier.isFinal(modifiers)) continue;
            
            if(isHeader) {
                isHeader = false;
                query.append(" SET n.").append(field.getName()).append(" = ");
                //TODO
            } else {
                query.append(", n.").append(field.getName()).append(" = ");
                //TODO
            }
        }
        query.append(" RETURN n");
        return query.toString();
    }

    public static final String findLabel(Class<? extends Object> clazz) {
        String label;
        NodeEntity nodeEntity = clazz.getAnnotation(NodeEntity.class);
        if(nodeEntity == null) label = clazz.getSimpleName();
        else label = nodeEntity.label();
        return label;
    }
    
    @SqlClip(sqlClipName = "findByPk")
    public <K> String findByPk(K k) {
        String label = findLabel(k.getClass());
        StringBuilder query = new StringBuilder("MATCH (n:").append(label).append(") WHERE 1=1");
        Field[] fields = FieldOperation.superGetDeclaredFields(k.getClass());
        Arrays.stream(fields)
            .filter(it -> {
                int modifiers = it.getModifiers();
                return !(Modifier.isFinal(modifiers) || Modifier.isStatic(modifiers));
            }).forEach(it -> {
                query.append(", n.").append(it.getName()).append(" = ");
                //TODO
            });
        return query.toString();
    }
}
