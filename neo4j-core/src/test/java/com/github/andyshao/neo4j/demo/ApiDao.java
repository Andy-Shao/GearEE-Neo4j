package com.github.andyshao.neo4j.demo;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

import org.neo4j.driver.v1.Transaction;

import com.github.andyshao.neo4j.annotation.Create;
import com.github.andyshao.neo4j.annotation.Match;
import com.github.andyshao.neo4j.annotation.Neo4jDao;
import com.github.andyshao.neo4j.annotation.SqlInject;
import com.github.andyshao.neo4j.model.PageReturn;
import com.github.andyshao.neo4j.model.Pageable;
import com.github.andyshao.reflect.annotation.Param;

@Neo4jDao(clipClass = ApiDaoClips.class)
public interface ApiDao {
    @Match(sql = "MATCH (n:Api) WHERE n.systemAlias = $pk.systemAlias AND n.apiName = $pk.apiName RETURN n")
    CompletionStage<Optional<Api>> findByPk(@Param("pk")ApiKey pk,@Param("tx") Transaction tx);
    
    @Match(sqlInject = @SqlInject(sqlClipName = "findByPageWithPk"))
    CompletionStage<PageReturn<Api>> findByPage(@Param("page")Pageable<ApiKey> pageable,@Param("tx") Transaction tx);
    
    @Match(sqlInject = @SqlInject(sqlClipName = "findByPageWithPkCount"))
    CompletionStage<Optional<Long>> findByPageCount(@Param("page")Pageable<ApiKey> pageable,@Param("tx") Transaction tx);
    
    @Create(sqlInject = @SqlInject(sqlClipName = "insertSelective"))
    CompletionStage<Optional<Api>> saveSelective(@Param("api")Api api,@Param("tx") Transaction tx);
    
    @Create(sql = "MERGE (n:Api {n.systemAlias:$api.systemAlias, n.apiName:$api.apiName}) SET n.others=$api.others RETURN n")
    CompletionStage<Optional<Api>> updateByPk(@Param("api")Api api,@Param("tx") Transaction tx);
    
    default CompletionStage<Optional<Api>> saveOrUpdate(@Param("api")Api api, @Param("tx") Transaction tx){
        return findByPk(api , tx).thenComposeAsync(op -> {
            if(op.isPresent()) return updateByPk(api , tx);
            return saveSelective(api , tx);
        });
    }
}
