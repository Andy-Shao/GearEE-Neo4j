package com.github.andyshao.neo4j.process.config;

import com.github.andyshao.neo4j.process.ClassPathAnnotationEntityScanner;
import com.github.andyshao.neo4j.process.DaoProcessor;
import com.github.andyshao.neo4j.process.EntityScanner;
import com.github.andyshao.neo4j.process.GeneralDaoProcessor;
import com.github.andyshao.neo4j.process.serializer.BasicTypeFormatterResult;
import com.github.andyshao.neo4j.process.serializer.FormatterResult;
import com.github.andyshao.neo4j.process.serializer.JavaBeanFormatterResult;
import com.github.andyshao.neo4j.process.serializer.SelfFormatterResult;
import com.github.andyshao.neo4j.process.sql.SqlAnalysis;
import com.github.andyshao.neo4j.process.sql.SqlAnalysisBySql;
import com.github.andyshao.neo4j.process.sql.SqlAnalysisBySqlClip;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2020/8/28
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
public class DaoConfiguration {

    public EntityScanner entityScanner() {
        return new ClassPathAnnotationEntityScanner();
    }

    public SqlAnalysis sqlAnalysis() {
        SqlAnalysisBySqlClip sqlAnalysisBySqlClip = new SqlAnalysisBySqlClip(SqlAnalysis.DO_NOTHING);
        return new SqlAnalysisBySql(sqlAnalysisBySqlClip);
    }

    public FormatterResult formatterResult(EntityScanner entityScanner) {
        JavaBeanFormatterResult javaBeanFormatterResult =
                new JavaBeanFormatterResult(FormatterResult.DO_NOTHING, entityScanner);
        BasicTypeFormatterResult basicTypeFormatterResult = new BasicTypeFormatterResult(javaBeanFormatterResult);
        return new SelfFormatterResult(basicTypeFormatterResult);
    }

    public DaoProcessor daoProcessor(SqlAnalysis sqlAnalysis, FormatterResult formatterResult) {
        return new GeneralDaoProcessor(sqlAnalysis, formatterResult);
    }
}
