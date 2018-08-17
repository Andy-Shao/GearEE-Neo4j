package com.github.andyshao.neo4j.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author weichuang.shao
 *
 */
public interface SqlFormatter {
	static List<String> findReplacement(String query) {
		List<String> ret = new ArrayList<>();
		Pattern p = Pattern.compile("\\#\\{[a-zA-Z\\.0-9\\_]+\\}");
        Matcher m = p.matcher(query);
        while(m.find()) ret.add(m.group());
		return ret;
	}
	
	Optional<String> format(String query, Map<String, Object> params);
}
