package com.integration_test.protocol;

import net.sf.json.JSONObject;

import org.junit.Assert;
import org.junit.Test;

import com.common.ProtocolCommon;
import com.integration_test.client.ClientHelper;

public class ProtocolUSER0001IT extends ProtocolIT{
	
	@Test
	public void success() throws Exception {
		JSONObject received1 = ClientHelper.send("USER0001", 
				makeBody("id", "jszzang9", "pw", makeRandomString(), "content", makeRandomString()));
		Assert.assertTrue(ClientHelper.isContainsKeys(received1));
		Assert.assertEquals(ProtocolCommon.RESULT_CODE_SUCCESS, ClientHelper.getResultCode(received1));
	}
}
