package com.exception;

import com.common.ProtocolCommon;


public class InvalidJsonFormatException extends DcpException {

	private static final long serialVersionUID = -3915681770800621123L;

	public InvalidJsonFormatException(String telegramNumber) {
		super(telegramNumber);
	}

	@Override
	public String getResultCode() {
		return ProtocolCommon.RESULT_CODE_INVALID_JSON_FORMAT;
	}

	@Override
	public String getResultMessage() {
		return ProtocolCommon.RESULT_MESSAGE_INVALID_JSON_FORMAT;
	}
}
