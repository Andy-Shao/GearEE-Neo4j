package com.github.andyshao.neo4j.output;

import java.util.Optional;

import com.github.andyshao.neo4j.dao.DaoProcessor;
import com.github.andyshao.neo4j.dao.DaoProcessorParam;
import com.github.andyshao.neo4j.demo.Api;
import com.github.andyshao.neo4j.demo.ApiKey;
import com.github.andyshao.neo4j.demo.ApiSearchDao;
import com.github.andyshao.neo4j.model.PageReturn;
import com.github.andyshao.neo4j.model.Pageable;

public class ApiSearchDaoImpl implements ApiSearchDao {
    private final DaoProcessor daoProcessor;
    
    public ApiSearchDaoImpl(DaoProcessor daoProcessor) {
        this.daoProcessor = daoProcessor;
    }

    @Override
    public Optional<Api> findByPk(ApiKey pk) {
        DaoProcessorParam param = new DaoProcessorParam();
        param.setArgs(new Object[] {pk});
        param.setArgTypes(new Class[] {ApiKey.class});
        param.setDaoName("ApiSearchDao");
        param.setMethodName("findByPk");
        param.setTargetClass(ApiSearchDao.class);
        return this.daoProcessor.execute(param , Api.class);
    }

    @Override
    public PageReturn<Api> findByPage(Pageable<ApiKey> pageable) {
        DaoProcessorParam param = new DaoProcessorParam();
        param.setArgs(new Object[] {pageable});
        param.setArgTypes(new Class[] {Pageable.class});
        param.setDaoName("ApiSearchDao");
        param.setMethodName("findByPk");
        param.setTargetClass(ApiSearchDao.class);
        return this.daoProcessor.findByPage(param , Api.class);
    }

    @Override
    public void doExecution() {
        DaoProcessorParam param = new DaoProcessorParam();
        param.setArgs(new Object[] {});
        param.setArgTypes(new Class[] {});
        param.setDaoName("ApiSearchDao");
        param.setMethodName("findByPk");
        param.setTargetClass(ApiSearchDao.class);
        this.daoProcessor.execute(param);
    }
    
    public Optional<Api> findByPk(ApiKey pk, int i){
        DaoProcessorParam param = new DaoProcessorParam();
        param.setArgs(new Object[] {pk, i});
        param.setArgTypes(new Class[] {ApiKey.class, int.class});
        param.setDaoName("ApiSearchDao");
        param.setMethodName("findByPk");
        param.setTargetClass(ApiSearchDao.class);
        return this.daoProcessor.execute(param , Api.class);
    }
    
    public Optional<Api> findByPk(ApiKey pk, int i, long b){
        DaoProcessorParam param = new DaoProcessorParam();
        param.setArgs(new Object[] {pk, i, b});
        param.setArgTypes(new Class[] {ApiKey.class, int.class, long.class});
        param.setDaoName("ApiSearchDao");
        param.setMethodName("findByPk");
        param.setTargetClass(ApiSearchDao.class);
        return this.daoProcessor.execute(param , Api.class);
    }
    
    public Optional<Api> findByPk(ApiKey pk, int i, long b, String str){
        DaoProcessorParam param = new DaoProcessorParam();
        param.setArgs(new Object[] {pk, i, b});
        param.setArgTypes(new Class[] {ApiKey.class, int.class, long.class});
        param.setDaoName("ApiSearchDao");
        param.setMethodName("findByPk");
        param.setTargetClass(ApiSearchDao.class);
        return this.daoProcessor.execute(param , Api.class);
    }

}
