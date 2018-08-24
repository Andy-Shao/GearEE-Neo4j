package com.github.andyshao.neo4j.demo.impl;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

import org.neo4j.driver.v1.Transaction;
import org.neo4j.driver.v1.Value;
import org.neo4j.driver.v1.Values;

import com.github.andyshao.neo4j.demo.Api;
import com.github.andyshao.neo4j.demo.ApiDao;
import com.github.andyshao.neo4j.demo.ApiKey;
import com.github.andyshao.neo4j.model.PageReturn;
import com.github.andyshao.neo4j.model.Pageable;

public class DefaultApiDao implements ApiDao{

    @Override
    public CompletionStage<Optional<Api>> findByPk(ApiKey pk , Transaction tx) {
        String sql = "MATCH (n:Api) WHERE n.systemAlias = $pk.systemAlias AND n.apiName = $pk.apiName RETURN n";
        Value parameters = Values.parameters("pk.systemAlias", pk.getSystemAlias(), "pk.apiName", pk.getApiName());
        
        return tx.runAsync(sql, parameters).thenComposeAsync(src -> {
            return src.singleAsync().thenApplyAsync(record -> {
                if(record == null) return Optional.empty();
                
                Api api = new Api();
                api.setApiName(record.get("apiName").asString());
                api.setOthers(record.get("others").asString());
                api.setSystemAlias(record.get("systemAlias").asString());
                return Optional.of(api);
            });
        });
    }

    @Override
    public CompletionStage<PageReturn<Api>> findByPage(Pageable<ApiKey> pageable , Transaction tx) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CompletionStage<Optional<Api>> saveSelective(Api api , Transaction tx) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CompletionStage<Optional<Api>> updateByPk(Api api , Transaction tx) {
        // TODO Auto-generated method stub
        return null;
    }

}
