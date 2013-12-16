package com.core.persist.mysql.element;

import com.core.persist.PersistableFactory.PersistType;
import com.core.persist.base.element.ParamSet;

public class MysqlParamSet extends ParamSet{

	private static final long serialVersionUID = -1966301328517409822L;

	public MysqlParamSet(String tableName) {
		super(tableName);
	}

	@Override
	public PersistType getType() {
		return PersistType.MYSQL;
	}
}
