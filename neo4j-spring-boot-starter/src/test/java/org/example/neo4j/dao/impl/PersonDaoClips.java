package org.example.neo4j.dao.impl;

import com.github.andyshao.neo4j.annotation.Neo4jSqlClip;
import com.github.andyshao.neo4j.process.sql.Sql;
import com.github.andyshao.reflect.annotation.Param;
import com.google.common.collect.Maps;
import org.example.neo4j.domain.Person;
import org.neo4j.driver.Value;
import org.neo4j.driver.Values;

import java.util.Map;
import java.util.Objects;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2021/7/27
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
public class PersonDaoClips {
    @Neo4jSqlClip
    public static final Sql saveOrUpdate(@Param("p")Person person) {
        final Sql sql = new Sql();
        sql.setParameters(Maps.newHashMap());
        final Map<String, Value> parameters = sql.getParameters();
        final String id = person.getId();
        if(Objects.isNull(id)) throw new IllegalArgumentException("id cannot be null");

        final StringBuilder sqlString = new StringBuilder();
        sqlString.append("MERGE (n:Person {id: $p_id}) ");
        sqlString.append("ON CREATE ");
        sqlString.append("SET n.age = $p_age, ");
        sqlString.append("n.name = $p_name, ");
        sqlString.append("n.gender = $p_gender ");
        sqlString.append("On MATCH ");
        sqlString.append("SET n.age = $p_age, ");
        sqlString.append("n.name = $p_name, ");
        sqlString.append("n.gender = $p_gender ");
        sqlString.append("RETURN n");
        sql.setSql(sqlString.toString());

        parameters.put("p_id", Values.value(id));
        parameters.put("p_age", Values.value(person.getAge()));
        parameters.put("p_name", Values.value(person.getName()));
        parameters.put("p_gender", Values.value(person.getGender().name()));
        return sql;
    }
}
