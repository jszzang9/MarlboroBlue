package com.core.persist;

import com.core.model.schema.Schemas;
import com.core.persist.base.Persistable;
import com.core.persist.base.element.ParamSet;
import com.core.persist.mysql.MysqlPersist;
import com.exception.DcpException;
import com.core.persist.mysql.element.MysqlParamSet;


public class PersistableFactory {

	public enum PersistType {
		MYSQL
	}

	public static Persistable create(PersistType persistType) throws DcpException {
		if (persistType == PersistType.MYSQL)
			return new MysqlPersist();

		return null;
	}

	public static ParamSet createParamSet(Schemas schema) {
		ParamSet paramSet = null;

		if (schema.getPersistType() == PersistType.MYSQL) {
			paramSet = new MysqlParamSet(schema.getTableName());
		}

		return paramSet;
	}
}
