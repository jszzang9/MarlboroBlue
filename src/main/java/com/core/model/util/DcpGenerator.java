package com.core.model.util;

import java.util.UUID;

public class DcpGenerator {
	public static String makeSynthesisKey(String ... values) {
		String synthesisKey = "";
		for (int i = 0; i < values.length; i ++) {
			if (i < values.length - 1)
				synthesisKey += (values[i] + "/");
			else
				synthesisKey += (values[i]);
		}
		
		return synthesisKey;
	}
	
	public static String makeSignNumber() {
		return Long.toString(System.currentTimeMillis()).substring(8, 13);
	}
	
	public static String make16DigitCode(String key) {
		String uniqueKey = key + Long.toString(System.nanoTime());
		return UUID.nameUUIDFromBytes(uniqueKey.getBytes()).toString().replace("-", "").substring(0, 16);
	}
	
}
