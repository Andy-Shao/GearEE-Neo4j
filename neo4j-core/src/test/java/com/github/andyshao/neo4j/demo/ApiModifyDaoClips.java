package com.github.andyshao.neo4j.demo;

import com.github.andyshao.lang.StringOperation;
import com.github.andyshao.neo4j.annotation.SqlClip;
import com.github.andyshao.reflect.annotation.Param;

/**
 * 
 * Title:<br>
 * Descript:<br>
 * Copyright: Copryright(c) Aug 6, 2018<br>
 * Encoding:UNIX UTF-8
 * @author Andy.Shao
 *
 */
public class ApiModifyDaoClips extends GeneralDaoClips{
    @SqlClip(sqlClipName = "insertSelective")
    public String saveSelectiveClips(@Param("api")Api api) {
        StringBuilder query = new StringBuilder("CREATE (n:Api) SET n.systemAlias = #{api.systemAlias} AND n.apiName = #{api.apiName}");
        if(!StringOperation.isEmptyOrNull(api.getOthers())) query.append(", n.others = #{api.others}");
        String sql = query.toString();
        sql = StringOperation.replaceAll(sql , "#{api.systemAlias}" , api.getSystemAlias());
        sql = StringOperation.replaceAll(sql , "#{api.apiName}" , api.getApiName());
        sql = StringOperation.replaceAll(sql , "#{api.others}" , api.getOthers());
        return sql;
    }
    
    @SqlClip(sqlClipName = "replaceSelective")
    public String saveOrUpdateSelective(@Param("api") Api api) { 
        StringBuilder query = new StringBuilder("MERGE (n:Api {systemAlias: #{api.systemAlias}, apiName: #{api.apiName}}) SET ");
        query.append(" n.apiName = #{api.apiName}");
        if(!StringOperation.isEmptyOrNull(api.getOthers())) query.append(", n.others = '").append(api.getOthers()).append("'");
        String sql = query.toString();
        sql = StringOperation.replaceAll(sql , "#{api.systemAlias}" , api.getSystemAlias());
        sql = StringOperation.replaceAll(sql , "#{api.apiName}" , api.getApiName());
        return sql;
    }
}
