package com.github.andyshao.neo4j.demo.dao;

import com.github.andyshao.neo4j.annotation.Neo4jSqlClip;
import com.github.andyshao.neo4j.demo.Person;
import com.github.andyshao.neo4j.process.sql.Sql;
import com.github.andyshao.reflect.annotation.Param;
import com.google.common.collect.Maps;
import org.neo4j.driver.Value;
import org.neo4j.driver.Values;

import java.util.List;
import java.util.Map;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2020/8/26
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
public class PersonDaoClips {
    @Neo4jSqlClip(sqlClipName = "saveByList")
    public static Sql saveByList(@Param("ps")List<Person> persons) {
        final Sql result = new Sql();
        result.setParameters(Maps.newHashMap());
        if(persons.isEmpty()) throw new IllegalArgumentException();
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE ");
        for(int i=0; i<persons.size(); i++) {
            sb.append("( ");
            Person person = persons.get(i);
            Map<String, Value> paramMap = result.getParameters();
            sb.append("n").append(i).append(":Person ");
            sb.append("{ ");
            sb.append("id: $p_id").append(i).append(", ");
            paramMap.put("p_id" + i, Values.value(person.getId()));
            sb.append("name: $p_name").append(i).append(", ");
            paramMap.put("p_name" + i, Values.value(person.getName()));
            sb.append("age: $p_age").append(i).append(", ");
            paramMap.put("p_age" + i, Values.value(person.getAge()));
            sb.append("gender: $p_gender").append(i);
            paramMap.put("p_gender" + i, Values.value(person.getGender().name()));
            sb.append("} ");
            sb.append("), ");
        }
        sb.delete(sb.length() - 2, sb.length()).append(" ");
        sb.append("RETURN ");
        for(int i=0; i<persons.size(); i++) {
            sb.append("n").append(i).append(", ");
        }
        sb.delete(sb.length() - 2, sb.length()).append(" ");
        result.setSql(sb.toString());
        return result;
    }
}
