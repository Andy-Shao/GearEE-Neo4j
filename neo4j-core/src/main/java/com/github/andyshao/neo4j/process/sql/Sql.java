package com.github.andyshao.neo4j.process.sql;

import lombok.Getter;
import lombok.Setter;
import org.neo4j.driver.Value;

import java.util.Map;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2020/8/27
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
@Getter
@Setter
public class Sql {
    private String sql;
    private Map<String, Value> parameters;
}
