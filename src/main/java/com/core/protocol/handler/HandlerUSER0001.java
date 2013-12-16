package com.core.protocol.handler;

import com.core.model.TestDataBox;
import com.core.model.dto.TestData;
import com.core.protocol.request.RequestUSER0001;
import com.core.protocol.response.ResponseUSER0001;
import com.exception.DcpException;

public class HandlerUSER0001 extends Handler<RequestUSER0001, ResponseUSER0001>{

	@Override
	public void handle() throws DcpException {
		TestDataBox.getInstance().putTestData(new TestData(getRequest().getId(), getRequest().getPw(), getRequest().getContent()));
	}

}
