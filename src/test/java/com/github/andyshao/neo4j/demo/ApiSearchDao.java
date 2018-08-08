package com.github.andyshao.neo4j.demo;

import java.util.Optional;

import com.github.andyshao.neo4j.annotation.Match;
import com.github.andyshao.neo4j.annotation.Neo4jDao;
import com.github.andyshao.neo4j.annotation.SqlInject;
import com.github.andyshao.neo4j.model.PageReturn;
import com.github.andyshao.neo4j.model.Pageable;
import com.github.andyshao.reflect.annotation.Param;

@Neo4jDao(clipClass = ApiSearchDaoClips.class)
public interface ApiSearchDao {
    @Match(sql = "MATCH (n:Api) WHERE n.systemAlias = #{pk.systemAlias} AND n.apiName = #{pk.apiName}")
    Optional<Api> findByPk(@Param("pk") ApiKey pk);
    
    
    @Match(sqlInject = @SqlInject(sqlClipName = "findByPageWithPk"))
    PageReturn<Api> findByPage(Pageable<ApiKey> pageable);
}
