package com.github.andyshao.neo4j.demo;

import com.github.andyshao.lang.StringOperation;
import com.github.andyshao.neo4j.annotation.SqlClip;
import com.github.andyshao.neo4j.model.Pageable;
import com.github.andyshao.reflect.annotation.Param;

public class ApiDaoClips {
    @SqlClip(sqlClipName = "insertSelective")
    public static final String saveSelectiveClips(@Param("api")Api api) {
        StringBuilder query = new StringBuilder("CREATE (n:Api {systemAlias: $api.systemAlias, apiName: $api.apiName");
        if(!StringOperation.isEmptyOrNull(api.getOthers())) query.append(", others:$api.others");
        query.append("}) RETURN n");
        return query.toString();
    }
    
    @SqlClip(sqlClipName = "findByPageWithPk")
    public static final String findByPageWithPk(@Param("page")Pageable<ApiKey> pageable) {
        StringBuilder query = findByPk(pageable);
        query.append(" RETURN n");
        return query.toString();
    }
    
    @SqlClip(sqlClipName = "findByPageWithPkCount")
    public static final String findByPageWithPkCount(@Param("page")Pageable<ApiKey> pageable) {
        StringBuilder query = findByPk(pageable);
        query.append(" RETURN count(n)");
        return query.toString();
    }

    private static StringBuilder findByPk(Pageable<ApiKey> pageable) {
        StringBuilder query = new StringBuilder();
        query.append("MATCH (n:Api) WHERE 1=1 ");
        ApiKey apiKey = pageable.getData();
        if(!StringOperation.isTrimEmptyOrNull(apiKey.getApiName())) query.append(" AND n.apiName = $page.data.apiName");
        if(!StringOperation.isTrimEmptyOrNull(apiKey.getSystemAlias())) query.append(" AND n.systemAlias = $page.data.systemAlias");
        return query;
    }
    
    @SqlClip(sqlClipName = "updateByPkSelective")
    public static final String updateByPkSelective(@Param("api")Api api) {
        StringBuilder query = new StringBuilder("CREATE (n:Api {systemAlias:$api.systemAlias, apiName:$api.apiName})");
        query.append(" SET n.systemAlias = $api.systemAlias");
        if(!StringOperation.isTrimEmptyOrNull(api.getOthers())) query.append(", n.others = $api.others");
        query.append(" RETURN n");
        return query.toString();
    }
}
