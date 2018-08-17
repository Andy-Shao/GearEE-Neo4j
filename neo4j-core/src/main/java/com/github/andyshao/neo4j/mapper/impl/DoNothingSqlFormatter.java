package com.github.andyshao.neo4j.mapper.impl;

import java.util.Map;
import java.util.Optional;

import com.github.andyshao.neo4j.mapper.SqlFormatter;

/**
 * 
 * Title:<br>
 * Descript:<br>
 * Copyright: Copryright(c) Aug 17, 2018<br>
 * Encoding:UNIX UTF-8
 * @author Andy.Shao
 *
 */
public class DoNothingSqlFormatter implements SqlFormatter{

	@Override
	public Optional<String> format(String query, Map<String, Object> params) {
		return Optional.of(query);
	}
}
