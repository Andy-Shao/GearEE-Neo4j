package com.github.andyshao.neo4j.demo.impl;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

import org.neo4j.driver.v1.Transaction;

import com.github.andyshao.neo4j.demo.Api;
import com.github.andyshao.neo4j.demo.ApiDao;
import com.github.andyshao.neo4j.demo.ApiKey;
import com.github.andyshao.neo4j.mapper.Sql;
import com.github.andyshao.neo4j.mapper.SqlCompute;
import com.github.andyshao.neo4j.model.MethodKey;
import com.github.andyshao.neo4j.model.Neo4jDaoInfo;
import com.github.andyshao.neo4j.model.PageReturn;
import com.github.andyshao.neo4j.model.Pageable;
import com.github.andyshao.neo4j.model.SqlMethod;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ApiDaoOutput implements ApiDao{
    private Neo4jDaoInfo neo4jDaoInfo;
    private SqlCompute sqlCompute;

    @Override
    public CompletionStage<Optional<Api>> findByPk(ApiKey pk , Transaction tx) {
        MethodKey methodKey = new MethodKey();
        methodKey.setMethodName("findByPk");
        methodKey.setParameTypes(new Class<?>[] {ApiKey.class, Transaction.class});
        SqlMethod sqlMethod = neo4jDaoInfo.getSqlMethods().get(methodKey);
        Optional<Sql> query = sqlCompute.compute(sqlMethod.getDefinition() , neo4jDaoInfo , pk, tx);
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CompletionStage<PageReturn<Api>> findByPage(Pageable<ApiKey> pageable , Transaction tx) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CompletionStage<Optional<Long>> findByPageCount(Pageable<ApiKey> pageable , Transaction tx) {
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
