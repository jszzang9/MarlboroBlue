package com.core.persist.base;

import com.core.persist.PersistableFactory.PersistType;
import com.core.persist.base.element.ParamSet;
import com.core.persist.base.element.ResultSet;
import com.core.persist.base.element.Schema;
import com.exception.ServerInternalErrorException;

public interface Persistable {
	/**
	 * Database 타입을 가져온다.
	 * 
	 * @return Database 타입.
	 * @throws Exception
	 */
	public abstract PersistType getType() throws ServerInternalErrorException;
	
	/**
	 * 테이블 존재 유무를 판단한다.
	 * 
	 * @param tableName 테이블.
	 * @return 테이블 존재 유무.
	 * @throws Exception
	 */
	public abstract boolean isExist(String tableName) throws ServerInternalErrorException;
	
	/**
	 * 테이블을 생성한다.
	 * 
	 * @param schema 스키마.
	 * @throws Exception
	 */
	public abstract void create(Schema schema) throws ServerInternalErrorException;
	
	/**
	 * 테이블을 삭제한다.
	 * 
	 * @param schema 스키마.
	 * @throws Exception
	 */
	public abstract void drop(Schema schema) throws ServerInternalErrorException;
	
	/**
	 * 데이터를 가져온다.
	 * 
	 * @param paramSet 파라미터 세트.
	 * @return 결과 세트.
	 * @throws Exception
	 */
	public abstract ResultSet get(ParamSet paramSet) throws ServerInternalErrorException;
	
	/**
	 * 데이터를 검색한다.
	 * 
	 * @param paramSet 파라미터 세트.
	 * @return 결과 세트.
	 * @throws Exception
	 */
	public abstract ResultSet scan(ParamSet paramSet) throws ServerInternalErrorException;
	
	/**
	 * 데이터를 입력한다.
	 * 
	 * @param paramSet 파라미터 세트.
	 * @throws Exception
	 */
	public abstract void put(ParamSet paramSet) throws ServerInternalErrorException;
	
	/**
	 * 데이터를 삭제한다.
	 * 
	 * @param paramSet 파라미터 세트.
	 * @throws Exception
	 */
	public abstract void delete(ParamSet paramSet) throws ServerInternalErrorException;
}
