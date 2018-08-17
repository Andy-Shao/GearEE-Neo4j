package com.github.andyshao.neo4j.spring.util.dao;

import java.util.Optional;

import com.github.andyshao.neo4j.annotation.Create;
import com.github.andyshao.neo4j.annotation.Match;
import com.github.andyshao.neo4j.annotation.Neo4jDao;
import com.github.andyshao.neo4j.annotation.SqlInject;
import com.github.andyshao.neo4j.model.PageReturn;
import com.github.andyshao.neo4j.model.Pageable;

@Neo4jDao(clipClass = CrudDatoClips.class)
public interface CrudDao<D, K> {
    @Create(sqlInject = @SqlInject(sqlClipName = "saveSelective"))
    Optional<D> saveSelective(D data);
    @Match(sqlInject = @SqlInject(sqlClipName = "findByPk"))
    Optional<D> findByPk(K pk);
    Optional<D> updateSelectiveByPk(D data);
    Optional<D> saveOrUpdateSelectiveByPk(D data);
    PageReturn<D> findByPage(Pageable<Void> pageable);
}
