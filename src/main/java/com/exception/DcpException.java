package com.exception;

import com.core.protocol.response.Response;
import com.core.protocol.response.ResponseDUMMY000;


public abstract  class DcpException extends Exception {
	private static final long serialVersionUID = -5888452282689661445L;
	private String telegramNumber = "";
	
	public DcpException() {
		setTelegramNumber("");
	}
	
	public DcpException(String telegramNumber) {
		setTelegramNumber(telegramNumber);
	}

	public String getTelegramNumber() {
		return telegramNumber;
	}

	public void setTelegramNumber(String telegramNumber) {
		this.telegramNumber = telegramNumber;
	}	
	
	/**
	 * 에러 메시지를 생성한다.
	 * 
	 * @param lastTelegramNumber 마지막 전문 번호.
	 * @return 에러 메시지.
	 */
	public Response createErrorResponse(String lastTelegramNumber) {
		Response response = new ResponseDUMMY000();
		response.setResultCode(getResultCode());
		response.setResultMessage(getResultMessage());
		if (getTelegramNumber().length() > 0)
			response.setTelegramNumber(getTelegramNumber());
		else if (lastTelegramNumber != null)
			response.setTelegramNumber(lastTelegramNumber);
		
		return response;
	}
	
	public abstract String getResultCode();
	
	public abstract String getResultMessage();
	
}
