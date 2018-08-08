package com.github.andyshao.neo4j.spring.util.dao;

import com.github.andyshao.neo4j.annotation.Create;
import com.github.andyshao.neo4j.annotation.Neo4jDao;
import com.github.andyshao.neo4j.annotation.SqlInject;

@Neo4jDao(clipClass = CrudDatoClips.class)
public interface CrudDao<D, K> {
    @Create(sqlInject = @SqlInject(sqlClipName = "saveSelective"))
    D saveSelective(D data);
    D findByPk(K pk);
    D updateSelective(D data);
    D saveOrUpdateSelective(D data);
}
