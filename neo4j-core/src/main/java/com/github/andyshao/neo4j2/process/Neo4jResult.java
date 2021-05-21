package com.github.andyshao.neo4j2.process;

import com.github.andyshao.neo4j2.domain.Neo4jNode;
import com.github.andyshao.neo4j2.domain.Relationship;
import lombok.Builder;

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
@Builder
public class Neo4jResult {
    private Map<String, Neo4jNode> nodes;
    private Map<String, Relationship> relations;

    public Neo4jNode getNode(String alias) {
        return this.nodes.get(alias);
    }

    public Set<String> getNodeAlias() {
        return this.nodes.keySet();
    }

    public Relationship getRelationship(String alias) {
        return this.relations.get(alias);
    }

    public Set<String> getRelationshipAlias() {
        return this.relations.keySet();
    }
}
