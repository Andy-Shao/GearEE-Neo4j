package com.github.andyshao.neo4j2.process;

import java.util.ArrayList;
import java.util.List;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2021/5/21
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
public class Conditional {
    private static final Conditional NON_CONDITIONAL = new Conditional();
    private final List<Criteria> orderCriteria = new ArrayList<>();

    public static Conditional excludeConditional() {
        return NON_CONDITIONAL;
    }

//    public void or(Criteria criteria) {
//        this.orderCriteria.add(criteria);
//    }

    public Criteria or() {
        final Criteria result = Criteria.builder()
                .conditional(this)
                .build();
        this.orderCriteria.add(result);
        return result;
    }

}
