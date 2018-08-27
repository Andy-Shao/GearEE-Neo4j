package com.github.andyshao.neo4j.mapper.impl;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;

import org.mockito.Mockito;
import org.neo4j.driver.v1.Transaction;

import com.github.andyshao.neo4j.demo.Api;
import com.github.andyshao.neo4j.demo.ApiDao;
import com.github.andyshao.neo4j.demo.ApiKey;
import com.github.andyshao.neo4j.mapper.Sql;
import com.github.andyshao.neo4j.model.Neo4jDaoInfo;
import com.github.andyshao.neo4j.model.Pageable;
import com.github.andyshao.reflect.MethodOperation;

public class SqlComputeTest {
    public static void main(String[] args) {
        PackageMapperScanner scanner = new PackageMapperScanner();
        scanner.setPackagePath(ApiDao.class.getPackage());
        Map<String , Neo4jDaoInfo> scan = scanner.scan();
        
        EmptyParamSqlFormatter emptyParamSqlFormatter = new EmptyParamSqlFormatter();
        MultiParamSqlFormatter multiParamSqlFormatter = new MultiParamSqlFormatter();
        multiParamSqlFormatter.setNext(emptyParamSqlFormatter);
        NoClipSqlCompute noClipSqlCompute = new NoClipSqlCompute();
        noClipSqlCompute.setSqlFormatter(multiParamSqlFormatter);
        ClipSqlCompute clipSqlCompute = new ClipSqlCompute();
        clipSqlCompute.setNext(noClipSqlCompute);
        clipSqlCompute.setSqlFormatter(multiParamSqlFormatter);

        Neo4jDaoInfo neo4jDaoInfo = scan.get("ApiDao");
        Method method = MethodOperation.getDeclaredMethod(neo4jDaoInfo.getDaoClass() , "findByPk" , ApiKey.class, Transaction.class);
        Api api = new Api();
        api.setApiName("api001");
        api.setSystemAlias("system");
        Transaction tx = Mockito.mock(Transaction.class);
        Optional<Sql> compute = clipSqlCompute.compute(method , neo4jDaoInfo , api, tx);
        System.out.println(compute);
        
        method = MethodOperation.getDeclaredMethod(neo4jDaoInfo.getDaoClass() , "findByPage" , Pageable.class, Transaction.class);
        Pageable<Api> pageable = new Pageable<>();
        pageable.setData(api);
        compute = clipSqlCompute.compute(method , neo4jDaoInfo , pageable, tx);
        System.out.println(compute);
    }
}
