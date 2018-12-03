package org.example.neo4j.core.service;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

import org.example.neo4j.dao.SystemDao;
import org.example.neo4j.domain.SystemInfo;
import org.neo4j.driver.v1.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.andyshao.neo4j.annotation.Neo4jTransaction;
import com.github.andyshao.reflect.annotation.Param;

@Service
@Neo4jTransaction
public class SystemCoreService {
    @Autowired
    private SystemDao systemDao;

    CompletionStage<Optional<SystemInfo>> trySave(@Param("sys")SystemInfo system, @Param("tx")Transaction transaction){
        return systemDao.trySave(system , transaction);
    }
}
