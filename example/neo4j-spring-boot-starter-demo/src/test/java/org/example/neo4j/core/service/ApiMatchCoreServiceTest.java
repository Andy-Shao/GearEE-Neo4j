package org.example.neo4j.core.service;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

import org.example.neo4j.Neo4jTestApplication;
import org.example.neo4j.domain.ApiMatch;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = Neo4jTestApplication.class)
public class ApiMatchCoreServiceTest {
    @Autowired
    private ApiMatchCoreService coreService;
    
    @Test
    public void testTrySave() {
        ApiMatch apiMatch = new ApiMatch();
        apiMatch.setApiMatchName("api001");
        CompletionStage<Optional<ApiMatch>> cs = this.coreService.trySave(apiMatch , null);
        System.out.println(cs.toCompletableFuture().join());
    }
}