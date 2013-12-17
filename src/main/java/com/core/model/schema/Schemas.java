package com.core.model.schema;

import com.core.model.dto.ADto;
import com.core.model.dto.TestData;
import com.core.persist.PersistableFactory.PersistType;
import com.core.persist.base.element.Schema;
import com.core.persist.mysql.element.MysqlColumnDescriptor;
import com.core.persist.mysql.element.MysqlSchema;
import com.exception.DcpException;
import com.MarlboroBlueConfiguration;



public enum Schemas {
	TEST_TABLE("TEST_TABLE", "#R", TestData.class),
	;

	public static final String ROW_NAME_KEY = "ROW_NAME_KEY";
	public static final String CFAMILY_NAME_KEY = "CFAMILY_NAME_KEY";

	private PersistType persistType;
	private String tableName;
	private String cfamilyName;
	private Class<? extends ADto> dtoClass;

	/**
	 * {@link Schemas} 생성자.
	 * 
	 * @param tableName 테이블 이름.
	 * @param cfamilyName 컬럼패밀리 이름.
	 */
	private Schemas(String tableName, String cfamilyName, Class<? extends ADto> dtoClass) {
		try {
			this.persistType = MarlboroBlueConfiguration.getProperty(tableName).equals("MYSQL") ? PersistType.MYSQL : null;
			this.tableName = tableName;
			this.cfamilyName = cfamilyName;
			this.dtoClass = dtoClass;
		}
		catch (Exception e) {
		}
	}
	
	/**
	 * Database 타입을 가져온다.
	 * 
	 * @return Database 타입.
	 */
	public PersistType getPersistType() {
		return persistType;
	}

	/**
	 * 테이블 이름을 가져온다.
	 * 
	 * @return 테이블 이름.
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * 컬럼 패밀리 이름을 가져온다.
	 * 
	 * @return 컬럼 패밀리 이름.
	 */
	public String getCfamilyName() {
		return cfamilyName;
	}
	
	/**
	 * DTO 클래스를 가져온다.
	 * 
	 * @return DTO 클래스.
	 */
	public Class<? extends ADto> getDtoClass() {
		return dtoClass;
	}

	/**
	 * 해당 Schema를 생성한다.
	 * 
	 * @return 현재 Schemas 열거에 맞는 Schema
	 */
	public Schema build() throws DcpException {
		Schema schema = null;

		if (persistType == PersistType.MYSQL) {
			schema = new MysqlSchema(getTableName());
			schema.add(new MysqlColumnDescriptor(Schemas.ROW_NAME_KEY, ADto.DEFAULT_ROW_NAME_KEY_TYPE, true));
			
			ADto dtoObject = ADto.create(getDtoClass());
			for (String fieldName : dtoObject.getFieldNameList())
				schema.add(new MysqlColumnDescriptor(fieldName, dtoObject.getType(fieldName)));
		}

		return schema;
	}
}
