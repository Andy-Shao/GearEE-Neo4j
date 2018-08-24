package com.github.andyshao.neo4j.demo;

import java.io.Serializable;

import com.github.andyshao.lang.StringOperation;
import com.github.andyshao.neo4j.annotation.SqlClip;
import com.github.andyshao.neo4j.model.Pageable;
import com.github.andyshao.reflect.annotation.Param;

public class ApiDaoClips {
    @SqlClip(sqlClipName = "pageable")
    public <T extends Serializable> String pageable(@Param("page")Pageable<T> page) {
        StringBuilder query = new StringBuilder();
        int position = (page.getPageNum() - 1) * page.getPageSize();
        if(position == 0) query.append(" LIMIT ").append(page.getPageSize());
        else {
            int skip = position - 1;
            query.append(" SKIP ").append(skip);
            query.append(" LIMIT #{page.pageSize}");
        }
        String sql = query.toString();
        sql = StringOperation.replaceAll(sql , "#{page.pageSize}" , String.valueOf(page.getPageSize()));
        return sql;
    }
    
    @SqlClip(sqlClipName = "insertSelective")
    public String saveSelectiveClips(@Param("api")Api api) {
        StringBuilder query = new StringBuilder("CREATE (n:Api) SET n.systemAlias = #{api.systemAlias} AND n.apiName = #{api.apiName}");
        if(!StringOperation.isEmptyOrNull(api.getOthers())) query.append(", n.others = #{api.others}");
        query.append(" RETURN n");
        return query.toString();
    }
    
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
