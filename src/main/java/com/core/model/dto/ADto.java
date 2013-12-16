package com.core.model.dto;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import com.exception.DcpException;
import com.exception.ServerInternalErrorException;


public abstract class ADto {
	public static final String DEFAULT_ROW_NAME_KEY_TYPE = "varchar(250)";
	public static final String DEFAULT_FIELD_NORMAL_TYPE = "varchar(512)";
	public static final String DEFAULT_FIELD_BIG_TYPE = "varchar(1024)";
	public static final String DEFAULT_FIELD_HUGE_TYPE = "varchar(4096)";
	
	/**
	 * Java Reflection 으로 해당 객체의 필드들을 HashMap으로 변환해준다.
	 * 
	 * @return 필드의 이름과 값을 포함한 HashMap
	 * @throws DcpException
	 */
	public HashMap<String, String> get() throws DcpException {
		HashMap<String, String> dataMap = new HashMap<String, String>();
		
		try {
			Field[] fields = getClass().getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				Object value = field.get(this);
				if (value != null && value instanceof String)
					dataMap.put(field.getName(), (String)value);
				field.setAccessible(false);
			}
			
		} catch (Exception e) {
			throw new ServerInternalErrorException();
		}
		
		return dataMap;
	}
	
	/**
	 * Java Reflection 으로 해당 객체에 맞는 필드를 찾아 값을 설정한다.
	 * 
	 * @param dataMap 필드의 이름과 값을 포함한 HashMap
	 * @throws DcpException
	 */
	public void set(HashMap<String, String> dataMap) throws DcpException {
		try {
			Field[] fields = getClass().getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				if (dataMap.containsKey(field.getName()))
					field.set(this, dataMap.get(field.getName()));
				field.setAccessible(false);
			}
			
		} catch (Exception e) {
			throw new ServerInternalErrorException();
		}
	}
	
	/**
	 * Java Reflection 으로 해당 객체의 필드 이름 리스트를 가져온다.
	 * 
	 * @return 필드 이름 리스트.
	 * @throws DcpException
	 */
	public ArrayList<String> getFieldNameList() throws DcpException {
		ArrayList<String> fieldNameList = new ArrayList<String>();
		
		try {
			Field[] fields = getClass().getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				fieldNameList.add(field.getName());
				field.setAccessible(false);
			}
			
		} catch (Exception e) {
			throw new ServerInternalErrorException();
		}
		
		return fieldNameList;
	}
	
	/**
	 * 필드의 타입을 결정한다.
	 * 
	 * @param fieldName 필드 이름.
	 * @return 필드 타입.
	 * @throws DcpException
	 */
	public String getType(String fieldName) throws DcpException {
		return DEFAULT_FIELD_NORMAL_TYPE;
	}

	/**
	 * ADto 인스턴스를 생성한다.
	 * 
	 * @param cls ADto 클래스.
	 * @return ADto 인스턴스.
	 * @throws DcpException
	 */
	public static ADto create(Class<? extends ADto> cls) throws DcpException {
		ADto dtoObject = null;
		
		try {
			dtoObject = cls.newInstance();
		} catch (Exception e) {
			throw new ServerInternalErrorException();
		}
		
		return dtoObject;
	}
}