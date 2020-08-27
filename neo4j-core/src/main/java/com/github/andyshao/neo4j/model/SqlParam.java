package com.github.andyshao.neo4j.model;

import com.github.andyshao.reflect.GenericNode;
import com.github.andyshao.reflect.annotation.Param;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.lang.reflect.Parameter;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2020/8/27
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
@Getter
@Setter
public class SqlParam implements Serializable {
    private Parameter definition;
    private Param param;
    private String nativeName;
    private GenericNode returnTypeInfo;
}
