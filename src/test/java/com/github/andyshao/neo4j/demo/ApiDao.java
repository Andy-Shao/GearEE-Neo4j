package com.github.andyshao.neo4j.demo;

import java.util.Optional;

import com.github.andyshao.neo4j.annotation.Neo4jDao;

@Neo4jDao
public interface ApiDao extends ApiSearchDao, ApiModifyDao{
    default Optional<Api> trySave(Api api) {
        Optional<Api> inDb = findByPk(api);
        return inDb.isPresent() ? inDb : saveSelective(api);
    }
}
