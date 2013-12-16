package com.core.persist.mysql.element;

import com.core.persist.base.element.ColumnDescriptor;

public class MysqlColumnDescriptor extends ColumnDescriptor{
	private boolean isPrimaryKey = false;

	public MysqlColumnDescriptor(String name, String type) {
		super(name, type);
	}
	
	public MysqlColumnDescriptor(String name, String type, boolean isPrimaryKey) {
		super(name, type);
		this.isPrimaryKey = isPrimaryKey;
	}

	public boolean isPrimaryKey() {
		return isPrimaryKey;
	}

	public void setPrimaryKey(boolean isPrimaryKey) {
		this.isPrimaryKey = isPrimaryKey;
	}

}
