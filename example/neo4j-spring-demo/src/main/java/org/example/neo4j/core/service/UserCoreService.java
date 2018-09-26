package org.example.neo4j.core.service;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

import org.example.neo4j.dao.UserDao;
import org.example.neo4j.domain.User;
import org.neo4j.driver.v1.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.andyshao.neo4j.annotation.Neo4jTransaction;

@Service
@Neo4jTransaction
public class UserCoreService {
    @Autowired
    private UserDao userDao;
    
    public CompletionStage<Optional<User>> trySave(User user, Transaction transaction){
        return userDao.trySave(user , transaction);
    }
}
