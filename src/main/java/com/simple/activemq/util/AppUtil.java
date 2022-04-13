package com.simple.activemq.util;

import java.util.Random;

/**
 * Contains common application utilities
 * 
 * @author Rahul
 */
public class AppUtil {

	/**
	 * Gets value from env variable if exists, otherwise returns default value
	 * 
	 * @param key
	 *            env variable key
	 * @param defaultValue
	 *            default value to be returned in case env variable is not set
	 * @return value from env variable if exists, otherwise returns default
	 *         value
	 */
	public static String env(String key, String defaultValue) {
		String rc = System.getenv(key);
		if (rc == null) {
			return defaultValue;
		}
		return rc;
	}

	/**
	 * Gets value from {@code args} if index and args length are >=
	 * {@code index}, otherwise returns default value
	 * 
	 * @param args
	 *            {@link String[]} args
	 * @param index
	 *            index value
	 * @param defaultValue
	 *            default value
	 * @return value from {@code args} if index and args length are >=
	 */
	public static String arg(String[] args, int index, String defaultValue) {
		if (index < args.length) {
			return args[index];
		} else {
			return defaultValue;
		}
	}

	/**
	 * @return random string based on current time
	 */
	public static String createRandomString() {
		Random random = new Random(System.currentTimeMillis());
		long randomLong = random.nextLong();
		return Long.toHexString(randomLong);
	}
}
