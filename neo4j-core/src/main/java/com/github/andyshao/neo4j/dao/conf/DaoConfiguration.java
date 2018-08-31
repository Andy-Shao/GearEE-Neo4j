package com.github.andyshao.neo4j.dao.conf;

import com.github.andyshao.neo4j.dao.DaoFactory;
import com.github.andyshao.neo4j.dao.DaoProcessor;
import com.github.andyshao.neo4j.dao.impl.SimpleDaoFactory;
import com.github.andyshao.neo4j.dao.impl.SimpleDaoProcessor;
import com.github.andyshao.neo4j.io.AnnotationSupportDeSerializer;
import com.github.andyshao.neo4j.io.BaseTypeDeSerializer;
import com.github.andyshao.neo4j.io.DeSerializer;
import com.github.andyshao.neo4j.io.DefaultDeSerializer;
import com.github.andyshao.neo4j.io.JavaBeanDeSerializer;
import com.github.andyshao.neo4j.io.ListDeSerializer;
import com.github.andyshao.neo4j.io.PageReturnDeSerializer;
import com.github.andyshao.neo4j.io.VoidDeSerializer;
import com.github.andyshao.neo4j.mapper.SqlCompute;
import com.github.andyshao.neo4j.mapper.impl.ClipSqlCompute;
import com.github.andyshao.neo4j.mapper.impl.EmptyParamSqlFormatter;
import com.github.andyshao.neo4j.mapper.impl.MultiParamSqlFormatter;
import com.github.andyshao.neo4j.mapper.impl.NoClipSqlCompute;

/**
 * 
 * Title:<br>
 * Descript:<br>
 * Copyright: Copryright(c) Aug 31, 2018<br>
 * Encoding:UNIX UTF-8
 * @author Andy.Shao
 *
 */
public class DaoConfiguration {
    
    public SqlCompute sqlCompute() {
        EmptyParamSqlFormatter emptyParamSqlFormatter = new EmptyParamSqlFormatter();
        MultiParamSqlFormatter multiParamSqlFormatter = new MultiParamSqlFormatter();
        multiParamSqlFormatter.setNext(emptyParamSqlFormatter);
        NoClipSqlCompute noClipSqlCompute = new NoClipSqlCompute();
        noClipSqlCompute.setSqlFormatter(multiParamSqlFormatter);
        ClipSqlCompute sqlCompute = new ClipSqlCompute();
        sqlCompute.setNext(noClipSqlCompute);
        sqlCompute.setSqlFormatter(multiParamSqlFormatter);
        return sqlCompute;
    }
    
    public DeSerializer deSerializer() {
        JavaBeanDeSerializer javaBeanDeSerializer = new JavaBeanDeSerializer();
        javaBeanDeSerializer.setNext(new DefaultDeSerializer());
        BaseTypeDeSerializer baseTypeDeSerializer = new BaseTypeDeSerializer();
        baseTypeDeSerializer.setNext(javaBeanDeSerializer);
        ListDeSerializer listDeSerializer = new ListDeSerializer();
        listDeSerializer.setNext(baseTypeDeSerializer);
        PageReturnDeSerializer pageReturnDeSerializer = new PageReturnDeSerializer();
        pageReturnDeSerializer.setNext(listDeSerializer);
        VoidDeSerializer voidDeSerializer = new VoidDeSerializer();
        voidDeSerializer.setNext(pageReturnDeSerializer);
        AnnotationSupportDeSerializer deSerializer = new AnnotationSupportDeSerializer();
        deSerializer.setNext(voidDeSerializer);
        return deSerializer;
    }
    
    public DaoProcessor daoProcessor(SqlCompute sqlCompute, DeSerializer deSerializer) {
        return new SimpleDaoProcessor(sqlCompute , deSerializer);
    }
    
    public DaoFactory daoFactory(DaoProcessor daoProcessor) {
        SimpleDaoFactory simpleDaoFactory = new SimpleDaoFactory();
        simpleDaoFactory.setDaoProcessor(daoProcessor);
        return simpleDaoFactory;
    }
}
