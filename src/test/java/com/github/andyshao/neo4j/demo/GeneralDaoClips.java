package com.github.andyshao.neo4j.demo;

import java.io.Serializable;

import com.github.andyshao.neo4j.annotation.SqlClip;
import com.github.andyshao.neo4j.model.Pageable;

public class GeneralDaoClips {
    @SqlClip(sqlClipName = "pageable")
    public <T extends Serializable> String pageable(Pageable<T> page) {
        StringBuilder query = new StringBuilder();
        int position = (page.getPageNum() - 1) * page.getPageSize();
        if(position == 0) query.append(" LIMIT ").append(page.getPageSize());
        else {
            int skip = position - 1;
            query.append(" SKIP ").append(skip);
            query.append(" LIMIT ").append(page.getPageSize());
        }
        return query.toString();
    }
}
