package com.model;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.MarlboroBlueConfiguration;
import com.base.DcpUnitTest;
import com.core.model.TestDataBox;
import com.core.model.dto.TestData;

public class TestDataBoxIT extends DcpUnitTest{
	
	@BeforeClass
	public static void setupOnce() throws Exception {
		MarlboroBlueConfiguration.load(true);
		TestDataBox.getInstance().init();
	}

	@Test
	public void shouldSuccess() throws Exception {
		String id = makeRandomString();
		String pw = makeRandomString();
		String content = makeRandomString();
		
		TestDataBox.getInstance().putTestData(new TestData(id, pw, content));
		TestData table = TestDataBox.getInstance().getTestData(id);
		Assert.assertNotNull(table);
		Assert.assertEquals(id, table.getId());
		Assert.assertEquals(pw, table.getPw());
		Assert.assertEquals(content, table.getContent());
	}
}
