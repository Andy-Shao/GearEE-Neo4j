package com.github.andyshao.neo4j.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2020/8/28
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
@Getter
@Setter
public class Neo4jEntity {
    private Class<?> definition;
    private List<Neo4jEntityField> fields;
}
