package com.github.andyshao.neo4j.process;

import com.github.andyshao.neo4j.IntegrationTest;
import com.github.andyshao.neo4j.process.sql.SqlAnalysis;
import com.github.andyshao.neo4j.process.sql.SqlAnalysisBySql;
import com.github.andyshao.neo4j.process.sql.SqlAnalysisBySqlClip;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GeneralDaoProcessorTest extends IntegrationTest {
    private DaoProcessor daoProcessor;

    @BeforeEach
    void before() {
        SqlAnalysis sqlAnalysis = new SqlAnalysisBySql(SqlAnalysis.DO_NOTHING);
        sqlAnalysis = new SqlAnalysisBySqlClip(sqlAnalysis);
        //TODO
        this.daoProcessor = new GeneralDaoProcessor(sqlAnalysis, null);
    }

    @Test
    void processing() {
        //TODO
    }
}