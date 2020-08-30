package com.github.andyshao.neo4j.process;

import com.github.andyshao.neo4j.domain.Neo4jDao;
import com.github.andyshao.neo4j.domain.analysis.Neo4jDaoAnalysis;

import java.util.Map;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2020/8/27
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
public interface DaoScanner {
    Map<String, Neo4jDao> scan();
    default Neo4jDao scan(Class<?> daoInterface) {
        return Neo4jDaoAnalysis.analyseDao(daoInterface);
    }
}
