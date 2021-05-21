package com.github.andyshao.neo4j2.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import org.neo4j.driver.Value;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2021/5/21
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
@Getter
@Builder
public class Neo4jNode {
    private String nodeAlias;
    private String nodeType;
    @Getter(AccessLevel.PRIVATE)
    private Map<String, Value> properties;
    private boolean isPureAlias;

    public Map<String, Value> getProperties() {
        return Collections.unmodifiableMap(this.properties);
    }

    public Set<String> getPropertyKeys() {
        return this.properties.keySet();
    }

    public Value getPropertyByKey(String key) {
        return this.properties.get(key);
    }

    public Neo4jNode pureAlias() {
        return Neo4jNode.builder()
                .nodeAlias(this.nodeAlias)
                .nodeType(this.nodeType)
                .isPureAlias(true)
                .build();
    }
}
