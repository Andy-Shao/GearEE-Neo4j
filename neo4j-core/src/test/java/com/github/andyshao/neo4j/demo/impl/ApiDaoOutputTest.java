package com.github.andyshao.neo4j.demo.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.Transaction;

import com.github.andyshao.neo4j.dao.DaoProcessor;
import com.github.andyshao.neo4j.dao.impl.SimpleDaoProcessor;
import com.github.andyshao.neo4j.demo.Api;
import com.github.andyshao.neo4j.demo.ApiDao;
import com.github.andyshao.neo4j.demo.ApiKey;
import com.github.andyshao.neo4j.io.AnnotationSupportDeSerializer;
import com.github.andyshao.neo4j.io.BaseTypeDeSerializer;
import com.github.andyshao.neo4j.io.DefaultDeSerializer;
import com.github.andyshao.neo4j.io.JavaBeanDeSerializer;
import com.github.andyshao.neo4j.io.ListDeSerializer;
import com.github.andyshao.neo4j.io.PageReturnDeSerializer;
import com.github.andyshao.neo4j.io.VoidDeSerializer;
import com.github.andyshao.neo4j.mapper.impl.ClipSqlCompute;
import com.github.andyshao.neo4j.mapper.impl.EmptyParamSqlFormatter;
import com.github.andyshao.neo4j.mapper.impl.MultiParamSqlFormatter;
import com.github.andyshao.neo4j.mapper.impl.NoClipSqlCompute;
import com.github.andyshao.neo4j.mapper.impl.PackageMapperScanner;
import com.github.andyshao.neo4j.model.Neo4jDaoInfo;
import com.github.andyshao.neo4j.model.PageReturn;
import com.github.andyshao.neo4j.model.Pageable;

public class ApiDaoOutputTest {
    private DaoProcessor daoProcessor;
    private ApiDaoOutput apiDao;
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        ApiDaoOutputTest test = new ApiDaoOutputTest();
        test.init();
        
        try(Driver driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j" , "1303595"));
        Session session = driver.session();){
            final Transaction tx = session.beginTransaction();
            ApiKey pk = new ApiKey();
            pk.setApiName("api001");
            pk.setSystemAlias("my.system");
            CompletableFuture<Optional<Api>> findByPk = test.apiDao.findByPk(pk , tx).toCompletableFuture();
            CompletableFuture<List<Api>> findSameSystem = test.apiDao.findSameSystem("my.system" , tx).toCompletableFuture();
            Pageable<ApiKey> pageable = new Pageable<>();
            pageable.setData(pk);
            CompletableFuture<PageReturn<Api>> findByPage = test.apiDao.findByPage(pageable , tx).toCompletableFuture();
            CompletableFuture<Optional<Long>> findByPageCount = test.apiDao.findByPageCount(pageable , tx).toCompletableFuture();
            Api api = new Api();
            api.setApiName("myApi");
            api.setSystemAlias("mySystem");
            api.setOthers("others");
//            CompletableFuture<Optional<Api>> saveSelective = test.apiDao.saveSelective(api , tx).toCompletableFuture();
            CompletableFuture<Void> removeByPk = test.apiDao.removeByPk(api , tx).toCompletableFuture();
            CompletableFuture.allOf(findByPk, findSameSystem, findByPage, findByPageCount, removeByPk)
                .thenAcceptAsync(v -> tx.commitAsync());
            System.out.println(findByPk.join());
            System.out.println(findSameSystem.join());
            System.out.println(findByPage.join());
            System.out.println(findByPageCount.join());
//            System.out.println(saveSelective.join());
        }
    }
    
    public void init() {
        PackageMapperScanner packageMapperScanner = new PackageMapperScanner();
        packageMapperScanner.setPackagePath(ApiDao.class.getPackage());
        Map<String , Neo4jDaoInfo> scan = packageMapperScanner.scan();
        Neo4jDaoInfo neo4jDaoInfo = scan.get("ApiDao");
        
        EmptyParamSqlFormatter emptyParamSqlFormatter = new EmptyParamSqlFormatter();
        MultiParamSqlFormatter multiParamSqlFormatter = new MultiParamSqlFormatter();
        multiParamSqlFormatter.setNext(emptyParamSqlFormatter);
        NoClipSqlCompute noClipSqlCompute = new NoClipSqlCompute();
        noClipSqlCompute.setSqlFormatter(multiParamSqlFormatter);
        ClipSqlCompute sqlCompute = new ClipSqlCompute();
        sqlCompute.setNext(noClipSqlCompute);
        sqlCompute.setSqlFormatter(multiParamSqlFormatter);
        
        JavaBeanDeSerializer javaBeanDeSerializer = new JavaBeanDeSerializer();
        javaBeanDeSerializer.setNext(new DefaultDeSerializer());
        BaseTypeDeSerializer baseTypeDeSerializer = new BaseTypeDeSerializer();
        baseTypeDeSerializer.setNext(javaBeanDeSerializer);
        ListDeSerializer listDeSerializer = new ListDeSerializer();
        listDeSerializer.setNext(baseTypeDeSerializer);
        PageReturnDeSerializer pageReturnDeSerializer = new PageReturnDeSerializer();
        pageReturnDeSerializer.setNext(listDeSerializer);
        VoidDeSerializer voidDeSerializer = new VoidDeSerializer();
        voidDeSerializer.setNext(pageReturnDeSerializer);
        AnnotationSupportDeSerializer deSerializer = new AnnotationSupportDeSerializer();
        deSerializer.setNext(voidDeSerializer);
        
        this.daoProcessor = new SimpleDaoProcessor(sqlCompute , deSerializer); 
        this.apiDao = new ApiDaoOutput(daoProcessor, neo4jDaoInfo);
    }
}
