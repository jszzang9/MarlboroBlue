package com.core.persist.base.element;

import java.util.ArrayList;

public abstract class Schema extends ArrayList<ColumnDescriptor> {
	private static final long serialVersionUID = 2280802235173094909L;
	private String tableName;
	
	public Schema(String tableName) {
		setTableName(tableName);
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
}