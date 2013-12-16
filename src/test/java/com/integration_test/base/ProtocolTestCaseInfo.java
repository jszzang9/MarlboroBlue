package com.integration_test.base;

public enum ProtocolTestCaseInfo {
	DUMMY0001(false),
	USER00001(true),
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
