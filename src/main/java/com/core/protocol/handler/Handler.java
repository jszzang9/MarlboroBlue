package com.core.protocol.handler;

import com.core.protocol.request.Request;
import com.core.protocol.response.Response;
import com.exception.DcpException;

public abstract class Handler <I extends Request, O extends Response> {
	private I request;
	private O response;
	
	public I getRequest() {
		return request;
	}

	public void setRequest(I request) {
		this.request = request;
	}

	public O getResponse() {
		return response;
	}

	public void setResponse(O response) {
		this.response = response;
	}

	public abstract void handle() throws DcpException;
}
