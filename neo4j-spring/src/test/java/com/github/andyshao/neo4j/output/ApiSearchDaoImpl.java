package com.github.andyshao.neo4j.output;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.neo4j.ogm.session.SessionFactory;
import org.springframework.data.neo4j.transaction.SessionHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.github.andyshao.neo4j.demo.Api;
import com.github.andyshao.neo4j.demo.ApiKey;
import com.github.andyshao.neo4j.demo.ApiSearchDao;
import com.github.andyshao.neo4j.mapper.SqlCompute;
import com.github.andyshao.neo4j.model.Neo4jDaoInfo;
import com.github.andyshao.neo4j.model.PageReturn;
import com.github.andyshao.neo4j.model.Pageable;
import com.github.andyshao.reflect.MethodOperation;

@Transactional
public class ApiSearchDaoImpl implements ApiSearchDao {
    private final Map<String , Neo4jDaoInfo> scan;
    private final SqlCompute sqlCompute;
    private final SessionFactory sessionFactory;
    
    public ApiSearchDaoImpl(Map<String , Neo4jDaoInfo> scan, SqlCompute sqlCompute, SessionFactory sessionFactory) {
        this.scan = scan;
        this.sqlCompute = sqlCompute;
        this.sessionFactory = sessionFactory;
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
        Optional<String> query = sqlCompute.compute(MethodOperation.getMethod(ApiSearchDao.class , "findByPk" , ApiKey.class) , 
            scan.get("ApiSearchDao") , pk);
        SessionHolder holder = (SessionHolder) TransactionSynchronizationManager.getResource(sessionFactory);
        holder.getSession().query(query.get() , new HashMap<>());
        // TODO running query 
        return null;
    }

    @Override
    public PageReturn<Api> findByPage(Pageable<ApiKey> pageable) {
        Optional<String> query = sqlCompute.compute(MethodOperation.getMethod(ApiSearchDao.class , "findByPage" , Pageable.class) , 
            scan.get("ApiSearchDao") , pageable);
        SessionHolder holder = (SessionHolder) TransactionSynchronizationManager.getResource(sessionFactory);
        holder.getSession().query(query.get() , new HashMap<>());
        // TODO running query
        return null;
    }

}
