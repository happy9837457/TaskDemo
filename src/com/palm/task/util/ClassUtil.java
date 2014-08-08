package com.palm.task.util;

import java.lang.reflect.Constructor;

public class ClassUtil {

	public static Object getSimpleInstance(String className) {

		Class<?> clazz = forName(className);

		return getSimpleInstance(clazz);
	}

	public static Class<?> forName(String className) {

		Class<?> clazz = null;

		try {

			clazz = Class.forName(className);
		} catch (Exception e) {

		}

		return clazz;
	}

	public static Object getSimpleInstance(Class<?> clazz) {

		if (clazz == null) {

			return null;
		}

		Object obj = null;

		try {

			Constructor<?> constructor = clazz
					.getDeclaredConstructor(new Class<?>[0]);
			obj = constructor.newInstance(new Object[0]);

		} catch (Exception e) {

		}
		return obj;
	}

}
