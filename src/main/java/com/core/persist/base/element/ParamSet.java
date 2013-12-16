package com.core.persist.base.element;

import java.util.HashMap;

import com.core.persist.PersistableFactory.PersistType;


public abstract class ParamSet extends HashMap<String, String> implements Cloneable {
	private static final long serialVersionUID = -7326876715809024790L;
	private String tableName;
	
	public ParamSet(String tableName) {
		setTableName(tableName);
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	public abstract PersistType getType();
}
