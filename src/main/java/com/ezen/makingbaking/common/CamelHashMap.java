package com.ezen.makingbaking.common;

import java.util.HashMap;

import org.springframework.jdbc.support.JdbcUtils;

@SuppressWarnings("serial")
public class CamelHashMap extends HashMap<String, Object> {
	@Override
	public Object put(String key, Object value) {
		return super.put(JdbcUtils.convertUnderscoreNameToPropertyName(key), value);
	}
}