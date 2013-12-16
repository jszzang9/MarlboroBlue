package com.core.model;

import com.core.model.dto.TestTable;
import com.core.model.schema.Schemas;
import com.core.model.util.DcpGenerator;
import com.core.persist.PersistableFactory;
import com.core.persist.base.element.ParamSet;
import com.core.persist.base.element.ResultSet;
import com.exception.DcpException;

public class TestDataBox extends DataBox {
	private static TestDataBox testDataBox;
	public static TestDataBox getInstance() throws DcpException {
		if (testDataBox == null)
			testDataBox = new TestDataBox();
		return testDataBox;
	}
	
	public TestDataBox() throws DcpException {
		super();
	}

	public void init() throws DcpException {
		if (persistPool.getPersistable(Schemas.TEST_TABLE.getPersistType()).isExist(Schemas.TEST_TABLE.getTableName()) == false)
			persistPool.getPersistable(Schemas.TEST_TABLE.getPersistType()).create(Schemas.TEST_TABLE.build());
	}
	
	public TestTable getTestData(String id) throws DcpException {
		ParamSet paramSet = PersistableFactory.createParamSet(Schemas.TEST_TABLE);
		paramSet.put(Schemas.ROW_NAME_KEY, DcpGenerator.makeSynthesisKey(id));
		
		ResultSet resultSet = persistPool.getPersistable(Schemas.TEST_TABLE.getPersistType()).get(paramSet);
		if (resultSet.size() == 0)
			return null;
		
		TestTable table = new TestTable();
		table.set(resultSet.get(0));
		
		return table;
	}
	
	public void putTestData(TestTable table) throws DcpException {
		ParamSet paramSet = PersistableFactory.createParamSet(Schemas.TEST_TABLE);
		paramSet.put(Schemas.ROW_NAME_KEY, DcpGenerator.makeSynthesisKey(table.getId()));
		paramSet.putAll(table.get());
		
		persistPool.getPersistable(Schemas.TEST_TABLE.getPersistType()).put(paramSet);
	}
	
	public void deleteTestData(TestTable table) throws DcpException {
		ParamSet paramSet = PersistableFactory.createParamSet(Schemas.TEST_TABLE);
		paramSet.put(Schemas.ROW_NAME_KEY, DcpGenerator.makeSynthesisKey(table.getId()));
		paramSet.putAll(table.get());
		
		persistPool.getPersistable(Schemas.TEST_TABLE.getPersistType()).delete(paramSet);
	}
	
}
