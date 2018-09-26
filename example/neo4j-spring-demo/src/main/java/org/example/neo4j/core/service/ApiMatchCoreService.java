package org.example.neo4j.core.service;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

import org.example.neo4j.dao.ApiMatchDao;
import org.example.neo4j.domain.ApiMatch;
import org.example.neo4j.domain.SystemInfo;
import org.neo4j.driver.v1.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.andyshao.neo4j.annotation.Neo4jTransaction;

@Service
@Neo4jTransaction
public class ApiMatchCoreService {
    @Autowired
    private ApiMatchDao apiMatchDao;
    @Autowired
    private SystemCoreService sytemCoreService;
    
    public CompletionStage<Optional<ApiMatch>> trySave(ApiMatch apiMatch, Transaction transaction){
        return this.apiMatchDao.trySave(apiMatch , transaction);
    }
    
    public CompletionStage<Void> trySaveWithRelationShip(ApiMatch apiMatch, String systemAlias, 
        String username, Transaction transaction){
        
        return this.trySave(apiMatch , transaction)
            .thenComposeAsync(api -> {
                SystemInfo system = new SystemInfo();
                system.setSystemAlias(systemAlias);
                return this.sytemCoreService.trySave(system , transaction);
            }).thenRunAsync(() -> this.apiMatchDao.addRelationShip(apiMatch.getApiMatchName() , systemAlias , username , transaction));
    }
}
