package com.github.andyshao.neo4j.demo.impl;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

import org.neo4j.driver.v1.Transaction;

import com.github.andyshao.neo4j.demo.Api;
import com.github.andyshao.neo4j.demo.ApiDao;
import com.github.andyshao.neo4j.demo.ApiKey;
import com.github.andyshao.neo4j.model.PageReturn;
import com.github.andyshao.neo4j.model.Pageable;

public class DefaultApiDao implements ApiDao{

    @Override
    public CompletionStage<Optional<Api>> findByPk(ApiKey pk , Transaction tx) {
        // TODO Auto-generated method stub
        return null;
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
