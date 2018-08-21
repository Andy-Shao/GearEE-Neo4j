package com.github.andyshao.neo4j.demo;

import java.util.Optional;

import com.github.andyshao.neo4j.annotation.Create;
import com.github.andyshao.neo4j.annotation.Neo4jDao;
import com.github.andyshao.neo4j.annotation.SqlInject;
import com.github.andyshao.reflect.annotation.Param;

@Neo4jDao(clipClass = ApiModifyDaoClips.class)
public interface ApiModifyDao {
    @Create(sqlInject = @SqlInject(sqlClipName = "insertSelective"), returnType = Api.class)
    Optional<Api> saveSelective(@Param("api")Api api);
    
    
    @Create(sqlInject = @SqlInject(sqlClipName = "replaceSelective"))
    void saveOrUpdate(Api api);
}
