package com.github.andyshao.neo4j.demo.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import org.neo4j.driver.v1.Transaction;
import org.neo4j.driver.v1.Value;
import org.neo4j.driver.v1.Values;

import com.github.andyshao.lang.StringOperation;
import com.github.andyshao.neo4j.demo.Api;
import com.github.andyshao.neo4j.demo.ApiDao;
import com.github.andyshao.neo4j.demo.ApiDaoClips;
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
    public CompletionStage<PageReturn<Api>> findByPage(final Pageable<ApiKey> pageable , Transaction tx) {
        ApiKey data = pageable.getData();
        Map<String , Object> values = new HashMap<>();
        if(!StringOperation.isTrimEmptyOrNull(data.getSystemAlias())) values.put("page.data.system" , data.getSystemAlias());
        if(!StringOperation.isTrimEmptyOrNull(data.getApiName())) values.put("page.data.apiName" , data.getApiName());
        Value parameters = Values.value(values);
        return tx.runAsync(ApiDaoClips.findByPageWithPk(pageable), parameters).thenComposeAsync(src -> {
            return src.listAsync(record -> {
                Api api = new Api();
                api.setApiName(record.get("apiName").asString());
                api.setOthers(record.get("others").asString());
                api.setSystemAlias(record.get("systemAlias").asString());
                return api;
            }).thenApplyAsync(apis -> {
                PageReturn<Api> pageReturn = new PageReturn<>();
                pageReturn.setPageNum(pageable.getPageNum());
                pageReturn.setPageSize(pageable.getPageSize());
                pageReturn.setData(apis);
                return pageReturn;
            });
        });
    }

    @Override
    public CompletionStage<Optional<Api>> saveSelective(Api api , Transaction tx) {
        Map<String , Object> values = new HashMap<>();
        values.put("api.systemAlias" , api.getSystemAlias());
        values.put("api.apiName" , api.getApiName());
        if(!StringOperation.isTrimEmptyOrNull(api.getOthers())) values.put("api.others" , api.getOthers());
        Value parameters = Values.value(values);
        return tx.runAsync(ApiDaoClips.saveSelectiveClips(api), parameters).thenComposeAsync(src -> {
            return src.singleAsync().thenApplyAsync(record -> {
                if(record == null) return Optional.empty();
                
                Api retApi = new Api();
                retApi.setApiName(record.get("apiName").asString());
                retApi.setOthers(record.get("others").asString());
                retApi.setSystemAlias(record.get("systemAlias").asString());
                return Optional.of(retApi);
            });
        });
    }

    @Override
    public CompletionStage<Optional<Api>> updateByPk(Api api , Transaction tx) {
        Map<String , Object> values = new HashMap<>();
        values.put("api.systemAlias" , api.getSystemAlias());
        values.put("api.apiName" , api.getApiName());
        if(!StringOperation.isTrimEmptyOrNull(api.getOthers())) values.put("api.others" , api.getOthers());
        Value parameters = Values.value(values);
        return tx.runAsync(ApiDaoClips.updateByPkSelective(api), parameters).thenComposeAsync(src -> {
            return src.singleAsync().thenApplyAsync(record -> {
                if(record == null) return Optional.empty();
                
                Api retApi = new Api();
                retApi.setApiName(record.get("apiName").asString());
                retApi.setOthers(record.get("others").asString());
                retApi.setSystemAlias(record.get("systemAlias").asString());
                return Optional.of(retApi);
            });
        });
    }

    @Override
    public CompletionStage<Optional<Long>> findByPageCount(Pageable<ApiKey> pageable , Transaction tx) {
        ApiKey data = pageable.getData();
        Map<String , Object> values = new HashMap<>();
        if(!StringOperation.isTrimEmptyOrNull(data.getSystemAlias())) values.put("page.data.system" , data.getSystemAlias());
        if(!StringOperation.isTrimEmptyOrNull(data.getApiName())) values.put("page.data.apiName" , data.getApiName());
        Value parameters = Values.value(values);
        return tx.runAsync(ApiDaoClips.findByPageWithPkCount(pageable), parameters).thenComposeAsync(src -> {
            return src.singleAsync().thenApplyAsync(record -> {
                if(record == null) return Optional.empty();
                return record.get(0).isNull() ? Optional.empty() : Optional.of(record.get(0).asLong());
            });
        });
    }

    @Override
    public CompletionStage<Void> removeByPk(ApiKey pk , Transaction tx) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CompletionStage<List<Api>> findSameSystem(String systemAlias , Transaction tx) {
        // TODO Auto-generated method stub
        return null;
    }

}
