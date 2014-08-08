package com.palm.task.util;

import java.util.List;

import com.alibaba.fastjson.JSON;

/**
 * json解析
 * 
 * @author weixiang.qin
 * 
 */
@SuppressWarnings({ "unchecked" })
public class JsonUtil {
	public static final String ARRAY_START = "[";

	/**
	 * object转换成json
	 * 
	 * @param obj
	 * @return
	 */
	public static String formatObject(Object obj) {
		return JSON.toJSONString(obj);
	}

	/**
	 * json转换转换成object
	 * 
	 * @param json
	 * @param cls
	 * @return
	 */
	public static <T> T convertToObject(String json, Class<?> cls) {
		return (T) JSON.parseObject(json, cls);
	}

	/**
	 * json转换转换成array
	 * 
	 * @param json
	 * @param cls
	 * @return
	 */
	public static List<?> convertToArray(String json, Class<?> cls) {
		return JSON.parseArray(json, cls);
	}

	/**
	 * 是否是array
	 * 
	 * @param json
	 * @return
	 */
	public static boolean isJsonArray(String json) {
		if (json.startsWith(ARRAY_START)) {
			return true;
		}
		return false;
	}
}
