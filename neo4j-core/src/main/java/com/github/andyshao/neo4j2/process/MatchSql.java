package com.github.andyshao.neo4j2.process;

import com.github.andyshao.util.stream.Pair;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

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
public class MatchSql {
    private List<Pair<String, ResultType>> returnList;
    private List<MatchSqlNode> matchSqlNodes;
}
