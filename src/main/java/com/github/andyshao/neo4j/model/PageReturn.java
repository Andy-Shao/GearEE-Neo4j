package com.github.andyshao.neo4j.model;

import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;

import lombok.Data;

@Data
@SuppressWarnings("serial")
public class PageReturn<T extends Serializable> implements Serializable{
    private Integer pageNum = 0;
    private Integer pageSize = 10;
    private List<T> data = Lists.newArrayList();
}