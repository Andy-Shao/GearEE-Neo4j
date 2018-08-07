package com.github.andyshao.neo4j.demo;

import java.util.Optional;

import com.github.andyshao.neo4j.annotation.Create;
import com.github.andyshao.neo4j.annotation.Match;
import com.github.andyshao.neo4j.annotation.Neo4jDao;
import com.github.andyshao.neo4j.annotation.SqlInject;
import com.github.andyshao.reflect.annotation.Param;

@Neo4jDao(clipClasses = ApiDaoClips.class)
public interface ApiDao {
    @Match(sql = "MATCH (n:Api) WHERE n.systemAlias = #{pk.systemAlias} AND n.apiName = #{pk.apiName}")
    Optional<Api> findByPk(@Param("pk") ApiKey pk);
    
    @Create(sqlInject = @SqlInject(sqlClipName = "insertSelective"))
    Optional<Api> saveSelective(Api api);
    
    default Optional<Api> trySave(Api api) {
        Optional<Api> inDb = findByPk(api);
        return inDb.isPresent() ? inDb : saveSelective(api);
    }
    
    @Create(sqlInject = @SqlInject(sqlClipName = "replaceSelective"))
    void saveOrUpdate(Api api);
}
