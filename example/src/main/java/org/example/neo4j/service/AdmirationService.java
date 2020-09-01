package org.example.neo4j.service;

import com.github.andyshao.neo4j.annotation.Neo4jTransaction;
import org.example.neo4j.dao.PersonDao;
import org.example.neo4j.domain.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2020/9/1
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
@Service
@Neo4jTransaction
public class AdmirationService {
    @Autowired
    private PersonDao personDao;
    
    public Mono<Person> findAdmiration() {
        return this.personDao.findByPk("ERHSBSYKAHV04SNIPHUPBDR", null);
    }
}
