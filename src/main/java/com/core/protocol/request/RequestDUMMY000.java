package com.core.protocol.request;

import com.exception.MissingParameterException;

import net.sf.json.JSONObject;


public class RequestDUMMY000 extends Request{

	@Override
	public String getDesscription() {
		return "더미";
	}

	@Override
	public void setBodyData(JSONObject bodyData)
			throws MissingParameterException {
		
	}

}
