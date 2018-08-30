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
import com.github.andyshao.neo4j.model.PageReturn;
import com.github.andyshao.neo4j.model.Pageable;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ApiDaoOutput implements ApiDao{
    private final DaoProcessor daoProcessor;

    @Override
    public CompletionStage<Optional<Api>> findByPk(ApiKey pk , Transaction tx) {
        DaoProcessorParam param = new DaoProcessorParam();
        param.setArgs(new Object[] {pk, tx});
        param.setArgTypes(new Class<?>[] {ApiKey.class, Transaction.class});
        param.setMethodName("findByPk");
        return daoProcessor.process(param);
    }

    @Override
    public CompletionStage<PageReturn<Api>> findByPage(Pageable<ApiKey> pageable , Transaction tx) {
        DaoProcessorParam param = new DaoProcessorParam();
        param.setArgs(new Object[] {pageable, tx});
        param.setArgTypes(new Class<?>[] {Pageable.class, Transaction.class});
        param.setMethodName("findByPage");
        return daoProcessor.process(param);
    }

    @Override
    public CompletionStage<Optional<Long>> findByPageCount(Pageable<ApiKey> pageable , Transaction tx) {
        DaoProcessorParam param = new DaoProcessorParam();
        param.setArgs(new Object[] {pageable, tx});
        param.setArgTypes(new Class<?>[] {Pageable.class, Transaction.class});
        param.setMethodName("findByPageCount");
        return daoProcessor.process(param);
    }

    @Override
    public CompletionStage<Optional<Api>> saveSelective(Api api , Transaction tx) {
        DaoProcessorParam param = new DaoProcessorParam();
        param.setArgs(new Object[] {api, tx});
        param.setArgTypes(new Class<?>[] {Api.class, Transaction.class});
        param.setMethodName("saveSelective");
        return daoProcessor.process(param);
    }

    @Override
    public CompletionStage<Optional<Api>> updateByPk(Api api , Transaction tx) {
        DaoProcessorParam param = new DaoProcessorParam();
        param.setArgs(new Object[] {api, tx});
        param.setArgTypes(new Class<?>[] {Api.class, Transaction.class});
        param.setMethodName("saveSelective");
        return daoProcessor.process(param);
    }

    @Override
    public CompletionStage<Void> removeByPk(ApiKey pk , Transaction tx) {
        DaoProcessorParam param = new DaoProcessorParam();
        param.setArgs(new Object[] {pk, tx});
        param.setArgTypes(new Class<?>[] {ApiKey.class, Transaction.class});
        param.setMethodName("removeByPk");
        return daoProcessor.process(param);
    }

    @Override
    public CompletionStage<List<Api>> findSameSystem(String systemAlias , Transaction tx) {
        DaoProcessorParam param = new DaoProcessorParam();
        param.setArgs(new Object[] {systemAlias, tx});
        param.setArgTypes(new Class<?>[] {String.class, Transaction.class});
        param.setMethodName("findSameSystem");
        return daoProcessor.process(param);
    }

}
