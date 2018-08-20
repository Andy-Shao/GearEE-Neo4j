package com.github.andyshao.neo4j.demo;

import com.github.andyshao.lang.StringOperation;
import com.github.andyshao.neo4j.annotation.SqlClip;
import com.github.andyshao.neo4j.model.Pageable;
import com.github.andyshao.reflect.annotation.Param;

public class ApiSearchDaoClips extends GeneralDaoClips{
    @SqlClip(sqlClipName = "findByPageWithPk")
    public String findByPageWithPk(@Param("pageable")Pageable<ApiKey> pageable) {
        StringBuilder query = new StringBuilder();
        query.append("MATCH (n:Api) WHERE 1=1 ");
        ApiKey apiKey = pageable.getData();
        if(!StringOperation.isTrimEmptyOrNull(apiKey.getApiName())) query.append(" AND n.apiKey = '").append(apiKey.getApiName()).append("'");
        if(!StringOperation.isTrimEmptyOrNull(apiKey.getSystemAlias())) query.append(" AND n.systemAlias = '").append(apiKey.getSystemAlias()).append("'");
        query.append(" RETURN n");
        query.append(pageable(pageable));
        return query.toString();
    }
}
