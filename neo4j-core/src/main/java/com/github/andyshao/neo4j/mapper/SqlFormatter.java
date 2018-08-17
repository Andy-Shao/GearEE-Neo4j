package com.github.andyshao.neo4j.mapper;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.andyshao.lang.StringOperation;
import com.google.common.base.Splitter;

/**
 * 
 * @author weichuang.shao
 *
 */
public interface SqlFormatter {
	static Set<String> findReplacement(String query) {
		Set<String> ret = new HashSet<>();
		Pattern p = Pattern.compile("\\#\\{[a-zA-Z\\.0-9\\_]+\\}");
        Matcher m = p.matcher(query);
        while(m.find()) ret.add(m.group());
		return ret;
	}
	
	static List<String> analysisReplacement(String replacement) {
	    String expression = replacement;
	    expression = StringOperation.replaceAll(expression , "#{" , "");
	    expression = StringOperation.replaceAll(expression , "}" , "");
	    return Splitter.on('.').omitEmptyStrings().trimResults().splitToList(expression);
	}
	
	Optional<String> format(String query, Map<String, Object> params);
}
