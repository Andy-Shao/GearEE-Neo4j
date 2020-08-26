package com.github.andyshao.neo4j.demo;

import com.github.andyshao.neo4j.annotation.DeSerializer;
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
public class Person extends PersonId{
    private String name;
    private Integer age;
    @DeSerializer(GenderDeSerializer.class)
    private Gender gender;
}
