package com.core.protocol.request;

import net.sf.json.JSONObject;

import com.exception.MissingParameterException;

public class RequestUSER0001 extends Request{
	private String id;
	private String pw;
	private String content;
	
	@Override
	public String getDesscription() {
		return "회원가입";
	}

	@Override
	public void setBodyData(JSONObject bodyData)
			throws MissingParameterException {
		if (bodyData.containsKey("id") == false)
			throw new MissingParameterException(getTelegramNumber(), "id");
		if (bodyData.containsKey("pw") == false)
			throw new MissingParameterException(getTelegramNumber(), "pw");
		if (bodyData.containsKey("content") == false)
			throw new MissingParameterException(getTelegramNumber(), "content");
		
		setId(bodyData.getString("id"));
		setPw(bodyData.getString("pw"));
		setContent(bodyData.getString("content"));
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPw() {
		return pw;
	}

	public void setPw(String pw) {
		this.pw = pw;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
