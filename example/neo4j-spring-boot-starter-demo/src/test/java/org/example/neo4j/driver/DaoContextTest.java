package org.example.neo4j.driver;

import org.assertj.core.api.Assertions;
import org.example.neo4j.dao.ApiMatchDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.andyshao.neo4j.dao.DaoContext;

@SpringBootTest
@RunWith(SpringRunner.class)
public class DaoContextTest {
    @Autowired
    private DaoContext daoContext;
    
    @Test
    public void testGetDao() {
        ApiMatchDao dao = this.daoContext.getDao(ApiMatchDao.class);
        Assertions.assertThat(dao).isNotNull();
    }
}
