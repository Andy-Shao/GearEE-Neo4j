package com.github.andyshao.neo4j.spring.util.dao;

import org.neo4j.ogm.annotation.NodeEntity;

import com.github.andyshao.neo4j.annotation.SqlClip;

public class CrudDatoClips {
    @SqlClip(sqlClipName = "saveSelective")
    public <D> String saveSelective(D data) {
        String label;
        NodeEntity nodeEntity = data.getClass().getAnnotation(NodeEntity.class);
        if(nodeEntity == null) label = data.getClass().getSimpleName();
        else label = nodeEntity.label();
        
        StringBuilder query = new StringBuilder("CREATE (n:").append(label).append(")");
        //TODO do something...
        query.append(" RETURN n");
        return query.toString();
    }
}
