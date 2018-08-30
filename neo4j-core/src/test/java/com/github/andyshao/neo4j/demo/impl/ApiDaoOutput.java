package com.github.andyshao.neo4j.demo.impl;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import org.neo4j.driver.v1.Transaction;

import com.github.andyshao.neo4j.dao.DaoProcessor;
import com.github.andyshao.neo4j.dao.DaoProcessorParam;
import com.github.andyshao.neo4j.demo.Api;
import com.github.andyshao.neo4j.demo.ApiDao;
import com.github.andyshao.neo4j.demo.ApiKey;
import com.github.andyshao.neo4j.model.Neo4jDaoInfo;
import com.github.andyshao.neo4j.model.PageReturn;
import com.github.andyshao.neo4j.model.Pageable;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ApiDaoOutput implements ApiDao{
    private final DaoProcessor daoProcessor;
    private final Neo4jDaoInfo neo4jDaoInfo;

    @Override
    public CompletionStage<Optional<Api>> findByPk(ApiKey pk , Transaction tx) {
        DaoProcessorParam param = new DaoProcessorParam();
        param.setArgs(new Object[] {pk, tx});
        param.setArgTypes(new Class<?>[] {ApiKey.class, Transaction.class});
        param.setMethodName("findByPk");
        return daoProcessor.process(param, neo4jDaoInfo);
    }

    @Override
    public CompletionStage<PageReturn<Api>> findByPage(Pageable<ApiKey> pageable , Transaction tx) {
        DaoProcessorParam param = new DaoProcessorParam();
        param.setArgs(new Object[] {pageable, tx});
        param.setArgTypes(new Class<?>[] {Pageable.class, Transaction.class});
        param.setMethodName("findByPage");
        return daoProcessor.process(param, neo4jDaoInfo);
    }

    @Override
    public CompletionStage<Optional<Long>> findByPageCount(Pageable<ApiKey> pageable , Transaction tx) {
        DaoProcessorParam param = new DaoProcessorParam();
        param.setArgs(new Object[] {pageable, tx});
        param.setArgTypes(new Class<?>[] {Pageable.class, Transaction.class});
        param.setMethodName("findByPageCount");
        return daoProcessor.process(param, neo4jDaoInfo);
    }

    @Override
    public CompletionStage<Optional<Api>> saveSelective(Api api , Transaction tx) {
        DaoProcessorParam param = new DaoProcessorParam();
        param.setArgs(new Object[] {api, tx});
        param.setArgTypes(new Class<?>[] {Api.class, Transaction.class});
        param.setMethodName("saveSelective");
        return daoProcessor.process(param, neo4jDaoInfo);
    }

    @Override
    public CompletionStage<Optional<Api>> updateByPk(Api api , Transaction tx) {
        DaoProcessorParam param = new DaoProcessorParam();
        param.setArgs(new Object[] {api, tx});
        param.setArgTypes(new Class<?>[] {Api.class, Transaction.class});
        param.setMethodName("saveSelective");
        return daoProcessor.process(param, neo4jDaoInfo);
    }

    @Override
    public CompletionStage<Void> removeByPk(ApiKey pk , Transaction tx) {
        DaoProcessorParam param = new DaoProcessorParam();
        param.setArgs(new Object[] {pk, tx});
        param.setArgTypes(new Class<?>[] {ApiKey.class, Transaction.class});
        param.setMethodName("removeByPk");
        return daoProcessor.process(param, neo4jDaoInfo);
    }

    @Override
    public CompletionStage<List<Api>> findSameSystem(String systemAlias , Transaction tx) {
        DaoProcessorParam param = new DaoProcessorParam();
        param.setArgs(new Object[] {systemAlias, tx});
        param.setArgTypes(new Class<?>[] {String.class, Transaction.class});
        param.setMethodName("findSameSystem");
        return daoProcessor.process(param, neo4jDaoInfo);
    }
    
    public CompletionStage<Optional<Long>> oneParam(Transaction tx){
        DaoProcessorParam param = new DaoProcessorParam();
        param.setArgs(new Object[] {tx});
        param.setArgTypes(new Class<?>[] {Transaction.class});
        param.setMethodName("oneParam");
        return daoProcessor.process(param, neo4jDaoInfo);
    }
    
    public CompletionStage<Optional<Long>> threeParam(Transaction tx, Object obj, Object obj2){
        DaoProcessorParam param = new DaoProcessorParam();
        param.setArgs(new Object[] {tx, obj, obj2});
        param.setArgTypes(new Class<?>[] {Transaction.class, Object.class, Object.class});
        param.setMethodName("oneParam");
        return daoProcessor.process(param, neo4jDaoInfo);
    }
    
    public CompletionStage<Optional<Long>> sevenParam(Transaction tx, Object obj, Object obj2, ApiKey pk, ApiKey pk2, Api api, int i){
        DaoProcessorParam param = new DaoProcessorParam();
        param.setArgs(new Object[] {tx, obj, obj2, pk, pk2, api, i});
        param.setArgTypes(new Class<?>[] {Transaction.class, Object.class, Object.class, ApiKey.class, ApiKey.class, Api.class, int.class});
        param.setMethodName("oneParam");
        return daoProcessor.process(param, neo4jDaoInfo);
    }
    
}
