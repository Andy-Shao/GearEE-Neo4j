package com.github.andyshao.neo4j.domain;

import com.github.andyshao.lang.StringOperation;
import com.github.andyshao.reflect.GenericNode;
import com.github.andyshao.reflect.annotation.Param;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.lang.reflect.Parameter;
import java.util.Objects;

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
    public static final char SPLITTER = '_';
    public static final char HEAD_CHAR = '$';
    private Parameter definition;
    private Param param;
    private String nativeName;
    private GenericNode returnTypeInfo;

    public String getParamName() {
        String tmp = Objects.nonNull(this.param) ? this.param.value() : null;
        if(StringOperation.isEmptyOrNull(tmp)) return this.nativeName;
        else return tmp;
    }
}
