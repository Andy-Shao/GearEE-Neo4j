package com.github.andyshao.neo4j.demo;

import java.util.List;
import java.util.Optional;

import com.github.andyshao.neo4j.annotation.Match;
import com.github.andyshao.neo4j.annotation.Neo4jDao;
import com.github.andyshao.neo4j.annotation.SqlInject;
import com.github.andyshao.neo4j.model.Pageable;
import com.github.andyshao.reflect.annotation.Param;

@Neo4jDao(clipClass = ApiSearchDaoClips.class)
public interface ApiSearchDao {
    @Match(sql = "MATCH (n:Api) WHERE n.systemAlias = #{pk.systemAlias} AND n.apiName = #{pk.apiName} RETURN n", returnType = Api.class)
    Optional<Api> findByPk(@Param("pk") ApiKey pk);
    
  
    @Match(sqlInject = @SqlInject(sqlClipName = "findByPageWithPk"), returnType = Api.class)
    List<Api> findByPage(Pageable<ApiKey> pageable);
    
    void doExecution();
}
