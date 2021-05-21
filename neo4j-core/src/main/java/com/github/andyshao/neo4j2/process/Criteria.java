package com.github.andyshao.neo4j2.process;

import com.github.andyshao.neo4j.Neo4jException;
import lombok.Builder;
import org.neo4j.driver.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2021/5/21
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
@Builder
public class Criteria {
    @Builder.Default
    private List<Criteria> criteriaList = new ArrayList<>();
    private Conditional conditional;
    private String condition;
    private String propertyKey;
    private Value propertyValue;
    private Class<?> propertyValueType;

    protected void addCriterion(String condition, String key, Value value, Class<?> valuetype) {
        if(Objects.isNull(value)) throw new Neo4jException("Value for " + key + " cannot be null");

        final CriteriaBuilder builder = Criteria.builder()
                .condition(condition)
                .conditional(this.conditional)
                .propertyKey(key)
                .propertyValue(value)
                .propertyValueType(valuetype);

        this.criteriaList.add(builder.build());
    }

    public Criteria addEqualTo(String key, Value value, Class<?> valueType) {
        this.addCriterion("=", key, value, valueType);
        return this;
    }
}
