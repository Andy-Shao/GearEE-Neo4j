package com.github.andyshao.neo4j.demo.dao;

import com.github.andyshao.neo4j.annotation.Neo4jSqlClip;
import com.github.andyshao.neo4j.demo.Person;
import com.github.andyshao.reflect.annotation.Param;

import java.util.List;

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
    public static String saveByList(@Param("ps")List<Person> persons) {
        return "";
    }
}
