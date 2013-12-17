package com.integration_test.base;

public enum ProtocolTestCaseInfo {
	DUMMY000(false),
	USER0001(true),
	;
	
	private boolean isExistParam;

	private ProtocolTestCaseInfo(boolean isExistParam) {
		this.isExistParam = isExistParam;
	}
	
	public boolean isExistParam() {
		return isExistParam;
	}

	public void setExistParam(boolean isExistParam) {
		this.isExistParam = isExistParam;
	}
}
