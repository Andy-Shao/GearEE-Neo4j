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
        StringBuilder query = new StringBuilder("CREATE (n:Api) SET ");
        query.append(" n.systemAlias = '").append(api.getSystemAlias()).append("'");
        query.append(", n.apiName = '").append(api.getApiName()).append("'");
        if(!StringOperation.isEmptyOrNull(api.getOthers())) query.append(", n.others = '").append(api.getOthers()).append("'");
        return query.toString();
    }
    
    @SqlClip(sqlClipName = "replaceSelective")
    public String saveOrUpdateSelective(@Param("api") Api api) {
        StringBuilder query = new StringBuilder("MERGE (n:Api {");
        query.append("systemAlias: '").append(api.getSystemAlias()).append("'");
        query.append(", apiName: '").append(api.getApiName()).append("'");
        query.append("}) SET");
        query.append(" n.apiName = '").append(api.getApiName()).append("'");
        if(!StringOperation.isEmptyOrNull(api.getOthers())) query.append(", n.others = '").append(api.getOthers()).append("'");
        return query.toString();
    }
}
