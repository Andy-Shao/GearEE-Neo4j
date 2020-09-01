package org.example.neo4j.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2020/9/1
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Person {
    private String id;
    private String username;
    private Integer age;
    private Gender gender;
}
