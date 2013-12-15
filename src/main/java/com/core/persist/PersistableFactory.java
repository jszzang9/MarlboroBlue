package com.core.persist;


public class PersistableFactory {
	
	public enum PersistType {
		MYSQL
	}
	
	public static Persistable create(PersistType persistType) throws Exception {
//		if (persistType == persistType.MYSQL)
//			return new MysqlPersist();
		
		return null;
	}
}
