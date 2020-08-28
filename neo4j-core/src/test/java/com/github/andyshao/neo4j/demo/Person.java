package com.github.andyshao.neo4j.demo;

import com.github.andyshao.neo4j.annotation.Neo4jEntity;
import com.github.andyshao.neo4j.annotation.Serializer;
import com.github.andyshao.neo4j.process.serializer.EnumSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2020/8/26
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Neo4jEntity
public class Person extends PersonId{
    @EqualsAndHashCode.Exclude
    private String name;
    @EqualsAndHashCode.Exclude
    private Integer age;
    @EqualsAndHashCode.Exclude
    @Serializer(EnumSerializer.class)
    private Gender gender;
}
