package com.github.andyshao.neo4j.mapper.impl;

import java.util.Map;
import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import com.github.andyshao.neo4j.demo.Api;
import com.github.andyshao.neo4j.demo.ApiDao;
import com.github.andyshao.neo4j.demo.ApiKey;
import com.github.andyshao.neo4j.demo.ApiModifyDao;
import com.github.andyshao.neo4j.demo.ApiSearchDao;
import com.github.andyshao.neo4j.io.DefaultSerializer;
import com.github.andyshao.neo4j.model.Neo4jDaoInfo;
import com.github.andyshao.neo4j.model.Pageable;
import com.github.andyshao.reflect.MethodOperation;

public class SqlComputeTest {

    @Test
    public void computeTest() {
        PackageMapperScanner scanner = new PackageMapperScanner();
        scanner.setPackagePath(ApiDao.class.getPackage());
        Map<String , Neo4jDaoInfo> scan = scanner.scan();
        
        NoClipSqlCompute noClipSqlCompute = new NoClipSqlCompute();
        EmptyParamSqlFormatter emptyParamSqlFormatter = new EmptyParamSqlFormatter();
        MultiParamSqlFormatter multiParamSqlFormatter = new MultiParamSqlFormatter();
        multiParamSqlFormatter.setNext(emptyParamSqlFormatter);
        multiParamSqlFormatter.setSerializer(new DefaultSerializer());
        noClipSqlCompute.setSqlFormatter(multiParamSqlFormatter);
        ApiKey values = new ApiKey();
        values.setApiName("api001");
        values.setSystemAlias("my.system.alias");
        Optional<String> query = noClipSqlCompute.compute(
            MethodOperation.getDeclaredMethod(ApiSearchDao.class , "findByPk" , ApiKey.class) , 
            scan.get("ApiSearchDao") , values);
        
        Assert.assertThat(query.isPresent() , Matchers.is(true));
        System.out.println(query.get());
        
        
        ClipSqlCompute clipSqlCompute = new ClipSqlCompute();
        clipSqlCompute.setSqlFormatter(multiParamSqlFormatter);
        Pageable<ApiKey> pageable = new Pageable<ApiKey>();
        pageable.setData(values);
        query = clipSqlCompute.compute(MethodOperation.getDeclaredMethod(ApiSearchDao.class , "findByPage" , Pageable.class) , 
            scan.get("ApiSearchDao") , pageable);
        Assert.assertThat(query.isPresent() , Matchers.is(true));
        System.out.println(query.get());
        
        Api api = new Api();
        api.setApiName("apiName002");
        api.setSystemAlias("my.system.alias");
        query = clipSqlCompute.compute(MethodOperation.getDeclaredMethod(ApiModifyDao.class , "saveSelective" , Api.class) , 
            scan.get("ApiModifyDao") , api);
        System.out.println(query.get());
    }
}
