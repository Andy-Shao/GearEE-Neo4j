package com.github.andyshao.neo4j.output;

import java.util.Map;
import java.util.Optional;

import com.github.andyshao.neo4j.demo.Api;
import com.github.andyshao.neo4j.demo.ApiKey;
import com.github.andyshao.neo4j.demo.ApiSearchDao;
import com.github.andyshao.neo4j.mapper.SqlCompute;
import com.github.andyshao.neo4j.model.Neo4jDaoInfo;
import com.github.andyshao.neo4j.model.PageReturn;
import com.github.andyshao.neo4j.model.Pageable;
import com.github.andyshao.reflect.MethodOperation;

public class ApiSearchDaoImpl implements ApiSearchDao {
    private final Map<String , Neo4jDaoInfo> scan;
    private final SqlCompute sqlCompute;
    
    public ApiSearchDaoImpl(Map<String , Neo4jDaoInfo> scan, SqlCompute sqlCompute) {
        this.scan = scan;
        this.sqlCompute = sqlCompute;
//        PackageMapperScanner scanner = new PackageMapperScanner();
//        scanner.setPackagePath(ApiSearchDao.class.getPackage());
//        scan = scanner.scan();
//        
//        NoClipSqlCompute noClipSqlCompute = new NoClipSqlCompute();
//        EmptyParamSqlFormatter emptyParamSqlFormatter = new EmptyParamSqlFormatter();
//        MultiParamSqlFormatter multiParamSqlFormatter = new MultiParamSqlFormatter();
//        multiParamSqlFormatter.setNext(emptyParamSqlFormatter);
//        multiParamSqlFormatter.setSerializer(new DefaultSerializer());
//        noClipSqlCompute.setSqlFormatter(multiParamSqlFormatter);
//        ClipSqlCompute clipSqlCompute = new ClipSqlCompute();
//        clipSqlCompute.setNext(noClipSqlCompute);
//        
//        sqlCompute = clipSqlCompute;
    }

    @Override
    public Optional<Api> findByPk(ApiKey pk) {
        @SuppressWarnings("unused")
        Optional<String> query = sqlCompute.compute(MethodOperation.getMethod(ApiSearchDao.class , "findByPk" , ApiKey.class) , 
            scan.get("ApiSearchDao") , pk);
        // TODO running query 
        return null;
    }

    @Override
    public PageReturn<Api> findByPage(Pageable<ApiKey> pageable) {
        @SuppressWarnings("unused")
        Optional<String> query = sqlCompute.compute(MethodOperation.getMethod(ApiSearchDao.class , "findByPage" , Pageable.class) , 
            scan.get("ApiSearchDao") , pageable);
        // TODO running query
        return null;
    }

}
