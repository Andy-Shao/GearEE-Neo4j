package org.example.neo4j.dao;

import org.example.neo4j.domain.Person;

import com.github.andyshao.lang.StringOperation;
import com.github.andyshao.neo4j.annotation.SqlClip;
import com.github.andyshao.reflect.annotation.Param;

public class PersonDaoClip {
    @SqlClip(sqlClipName = "updateSelectiveByPk")
    public String updateSelectiveByPk(@Param("p")Person person) {
        StringBuilder sb = new StringBuilder("MATCH (n:Person {name:$p.name})");
        sb.append(" SET n.name = $p.name");
        if(person.getAge() != null)sb.append(", n.age = $p.age");
        if(!StringOperation.isTrimEmptyOrNull(person.getPhone())) sb.append(", n.phone=$p.phone");
        sb.append(" RETURN n");
        return sb.toString();
    }
}
