package com.github.andyshao.neo4j2.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

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
public class Relationship {
    private String relationAlias;
    private String relationType;
    private Neo4jNode dominance;
    private Neo4jNode target;

    public boolean hasRelationship() {
        return Objects.nonNull(this.dominance) && Objects.nonNull(this.target);
    }
}
