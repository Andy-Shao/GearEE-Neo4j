package org.example.neo4j.dao;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import org.example.neo4j.Neo4jTestApplication;
import org.example.neo4j.domain.AuditRecord;
import org.example.neo4j.domain.User;
import org.example.neo4j.domain.vo.UserStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = Neo4jTestApplication.class)
public class UserDaoTest {
    @Autowired
    private UserDao userDao;
    
    @Test
    public void testCreate() {
        User user = new User();
        AuditRecord auditRecord = new AuditRecord();
        user.setAuditRecord(auditRecord);
        auditRecord.setCreateTime(LocalDateTime.now());
        auditRecord.setCreateUser("weichuang.shao");
        user.setPassword("pwd");
        user.setUsername("andy.shao");
        user.setStatus(UserStatus.useable);
        CompletionStage<Optional<User>> userInfo = this.userDao.create(user , null);
        System.out.println(userInfo.toCompletableFuture().join());
    }
}
