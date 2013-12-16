package com.core.persist.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Level;

import com.MarlboroBlueConfiguration;
import com.core.model.schema.Schemas;
import com.core.persist.PersistableFactory.PersistType;
import com.core.persist.base.Persistable;
import com.core.persist.base.element.ColumnDescriptor;
import com.core.persist.base.element.ParamSet;
import com.core.persist.base.element.ResultSet;
import com.core.persist.base.element.Schema;
import com.core.persist.mysql.element.MysqlColumnDescriptor;
import com.core.persist.mysql.element.MysqlResultSet;
import com.exception.DcpException;
import com.exception.ServerInternalErrorException;
import com.util.QueuedLogger.QueuedLogger;




public class MysqlPersist implements Persistable {
	private BasicDataSource ds = null;

	/**
	 * {@link MysqlPersist} 생성자.
	 * @throws DcpException
	 */
	public MysqlPersist() throws DcpException {
		ds = new BasicDataSource();
		ds.setDriverClassName(MarlboroBlueConfiguration.getProperty("jdbc.driver"));
		ds.setUrl(MarlboroBlueConfiguration.getProperty("jdbc.url"));
		ds.setUsername(MarlboroBlueConfiguration.getProperty("jdbc.user"));
		ds.setPassword(MarlboroBlueConfiguration.getProperty("jdbc.passwd"));
		ds.setValidationQuery("select 1");
		ds.setTestWhileIdle(true);
		ds.setTimeBetweenEvictionRunsMillis(7200000);
	}
	
	public PersistType getType() throws ServerInternalErrorException {
		return PersistType.MYSQL;
	}

	public boolean isExist(String tableName) throws ServerInternalErrorException {
		boolean isExist = false;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		java.sql.ResultSet rs = null;
		
		try {
			conn = ds.getConnection();
			StringBuilder builder = new StringBuilder();
			builder.append("SHOW TABLES LIKE '" + tableName + "'");

			QueuedLogger.push(Level.DEBUG, builder.toString());
			pstmt = conn.prepareStatement(builder.toString());
			rs = pstmt.executeQuery();
			
			if (rs.next())
				isExist = true;
			
		} catch( Exception ex ) {
			QueuedLogger.push(Level.ERROR, ex.getMessage(), ex);
			throw new ServerInternalErrorException();
		} finally {
			if( rs != null ) try { rs.close(); } catch( Exception ex ) {} finally { rs = null; }
			if( pstmt != null ) try { pstmt.close(); } catch( Exception ex ) {} finally { pstmt = null; }
			if( conn != null ) try { conn.close(); } catch( Exception ex ) {} finally { conn = null; }
		}
		
		return isExist;
	}

	public void create(Schema schema) throws ServerInternalErrorException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = ds.getConnection();
			StringBuilder builder = new StringBuilder();
			builder.append("CREATE TABLE " + schema.getTableName() + " (");
			for (ColumnDescriptor columnDescriptor : schema) {
				if (columnDescriptor instanceof ColumnDescriptor) {
					builder.append("`" + columnDescriptor.getName() + "` " + columnDescriptor.getType());
					if (((MysqlColumnDescriptor)columnDescriptor).isPrimaryKey())
						builder.append(" PRIMARY KEY");
					if (schema.indexOf(columnDescriptor) < schema.size() - 1)
						builder.append(", ");
				}
			}
			builder.append(")");

			QueuedLogger.push(Level.DEBUG, builder.toString());
			pstmt = conn.prepareStatement(builder.toString());
			pstmt.execute();

		} catch( Exception ex ) {
			QueuedLogger.push(Level.ERROR, ex.getMessage(), ex);
			throw new ServerInternalErrorException();
		} finally {
			if( pstmt != null ) try { pstmt.close(); } catch( Exception ex ) {} finally { pstmt = null; }
			if( conn != null ) try { conn.close(); } catch( Exception ex ) {} finally { conn = null; }
		}
	}

	public void drop(Schema schema) throws ServerInternalErrorException {
		if (isExist(schema.getTableName()) == false)
			return;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = ds.getConnection();
			StringBuilder builder = new StringBuilder();
			builder.append("DROP TABLE " + schema.getTableName());

			QueuedLogger.push(Level.DEBUG, builder.toString());
			pstmt = conn.prepareStatement(builder.toString());
			pstmt.execute();

		} catch( Exception ex ) {
			QueuedLogger.push(Level.ERROR, ex.getMessage(), ex);
			throw new ServerInternalErrorException();
		} finally {
			if( pstmt != null ) try { pstmt.close(); } catch( Exception ex ) {} finally { pstmt = null; }
			if( conn != null ) try { conn.close(); } catch( Exception ex ) {} finally { conn = null; }
		}
	}

	public ResultSet get(ParamSet paramSet) throws ServerInternalErrorException {
		ResultSet resultSet = new MysqlResultSet();
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		java.sql.ResultSet rs = null;
		try {
			conn = ds.getConnection();
			StringBuilder builder = new StringBuilder();
			builder.append("SELECT * FROM " + paramSet.getTableName() + (paramSet.size() == 0 ? "":" WHERE "));
			
			Iterator<String> colIterator = paramSet.keySet().iterator();
			while( colIterator.hasNext() ) {
				String columName = colIterator.next();
				String columValue = paramSet.get(columName);
				builder.append("`" + columName + "`=BINARY('" + columValue + "') AND ");
			}
			QueuedLogger.push(Level.DEBUG,paramSet.size() == 0 ? builder.toString() :builder.substring(0, builder.lastIndexOf("AND")-1));
			pstmt = conn.prepareStatement(paramSet.size() == 0 ? builder.toString() :builder.substring(0, builder.lastIndexOf("AND")-1));
			rs = pstmt.executeQuery();

			if( rs != null ) {
				int colCount=rs.getMetaData().getColumnCount();
				
				String[] colNames = new String[colCount];
				int[] colTypes = new int[colCount];
				for ( int i = 0 ; i < colCount ; i++ ) { 
					colNames[i] = rs.getMetaData().getColumnName(i+1); 
					colTypes[i] = rs.getMetaData().getColumnType(i+1); 
				}

				while( rs.next() ) {
					HashMap<String, String> dataMap = new HashMap<String, String>();

					for ( int j = 0 ; j < colCount ; j++ ) {
						switch (colTypes[j]) {
						case Types.CHAR :; 
						case Types.VARCHAR : dataMap.put(colNames[j], rs.getString(colNames[j]));  break;
						default : dataMap.put(colNames[j], rs.getString(colNames[j]));  break;
						}
					}
					resultSet.add(dataMap); dataMap = null;
				}
			}

		} catch( Exception ex ) {
			QueuedLogger.push(Level.ERROR, ex.getMessage(), ex);
			throw new ServerInternalErrorException();
		} finally {
			if( rs != null ) try { rs.close(); } catch( Exception ex ) {} finally { rs = null; }
			if( pstmt != null ) try { pstmt.close(); } catch( Exception ex ) {} finally { pstmt = null; }
			if( conn != null ) try { conn.close(); } catch( Exception ex ) {} finally { conn = null; }
		}

		return resultSet;
	}

	public ResultSet scan(ParamSet paramSet) throws ServerInternalErrorException {
		return get(paramSet);
	}
	
	/**
	 * put의 데이터가 업데이트인지 체크하는 로직
	 * 
	 * @param conn DB 커넥션.
	 * @param paramSet 파라미터 세트.
	 * @param updateParamSet 업데이트 파라미터 세트.
	 * @return 업데이트 여부.
	 * @throws ServerInternalErrorException
	 */
	private int canUpdate(Connection conn, ParamSet paramSet, ParamSet updateParamSet) throws ServerInternalErrorException {
		int updateCnt = 0;
		if( updateParamSet == null || updateParamSet.size() == 0 ) return updateCnt;
		PreparedStatement pstmt = null;
		try {
			StringBuilder updateSelectBuilder = new StringBuilder();
			StringBuilder updateWhereBuilder = new StringBuilder();
			updateSelectBuilder.append("UPDATE  " + paramSet.getTableName()  + " SET ");
			updateWhereBuilder.append("WHERE ");
			Iterator<String> updateSqlIterator = paramSet.keySet().iterator();

			while( updateSqlIterator.hasNext() ) {
				String columName = updateSqlIterator.next();
				String columValue = paramSet.get(columName).replace("\\", "\\\\");
				updateSelectBuilder.append("`" + columName + "`=BINARY('" + columValue + "'),");
			}
			Iterator<String> updateWhereIterator = updateParamSet.keySet().iterator();
			while( updateWhereIterator.hasNext() ) {
				String columName = updateWhereIterator.next();
				String columValue = updateParamSet.get(columName).replace("\\", "\\\\");
				updateWhereBuilder.append("`" + columName + "`='" +  columValue +  "' AND ");
			}
			
			String updateSelect = updateSelectBuilder.substring(0, updateSelectBuilder.length()-1) + " ";
			String updateWhere = updateWhereBuilder.substring(0, updateWhereBuilder.lastIndexOf("AND")-1);
			QueuedLogger.push(Level.DEBUG,updateSelect + " " + updateWhere);
			pstmt = conn.prepareStatement(updateSelect + " " + updateWhere);
			updateCnt =  pstmt.executeUpdate();
			
		} catch( Exception ex ) {
			QueuedLogger.push(Level.ERROR, ex.getMessage(), ex);
			throw new ServerInternalErrorException();
		} finally {
			if( pstmt != null ) try { pstmt.close(); } catch( Exception ex ) {} finally { pstmt = null; }
		}
		return updateCnt;
	}

	public void put(ParamSet paramSet) throws ServerInternalErrorException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		java.sql.ResultSet rs = null;
		try {
			conn = ds.getConnection();
			ParamSet tmpSet = (ParamSet)paramSet.clone(); tmpSet.clear();
			tmpSet.put(Schemas.ROW_NAME_KEY, paramSet.get(Schemas.ROW_NAME_KEY));
			if( canUpdate(conn, paramSet, tmpSet) == 0 ) {
				StringBuilder colNameBuilder = new StringBuilder();
				StringBuilder colValueBuilder = new StringBuilder();
				colNameBuilder.append("INSERT INTO " + paramSet.getTableName()  + "( ");
				colValueBuilder.append("VALUES(");
				Iterator<String> colIterator = paramSet.keySet().iterator();

				while( colIterator.hasNext() ) {
					String columName = colIterator.next();
					String columValue = paramSet.get(columName).replace("\\", "\\\\");
					colNameBuilder.append("`" + columName + "`,");
					colValueBuilder.append("'" + columValue + "',");
				}
				String colNameStr = colNameBuilder.substring(0, colNameBuilder.length()-1) + ")";
				String colValueStr = colValueBuilder.substring(0, colValueBuilder.length()-1) + ")";
				QueuedLogger.push(Level.DEBUG,colNameStr + " " + colValueStr);
				pstmt = conn.prepareStatement(colNameStr + " " + colValueStr);
				pstmt.executeUpdate();
			}
		} catch( Exception ex ) {
			QueuedLogger.push(Level.ERROR, ex.getMessage(), ex);
			throw new ServerInternalErrorException();
		} finally {
			if( rs != null ) try { rs.close(); } catch( Exception ex ) {} finally { rs = null; }
			if( pstmt != null ) try { pstmt.close(); } catch( Exception ex ) {} finally { pstmt = null; }
			if( conn != null ) try { conn.close(); } catch( Exception ex ) {} finally { conn = null; }
		}		
	}
	
	public void delete(ParamSet paramSet) throws ServerInternalErrorException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = ds.getConnection();
			StringBuilder builder = new StringBuilder();
			builder.append("DELETE FROM  " + paramSet.getTableName() + (paramSet.size() == 0 ? "":" WHERE "));

			Iterator<String> colIterator = paramSet.keySet().iterator();
			while( colIterator.hasNext() ) {
				String columName = colIterator.next();
				String columValue = paramSet.get(columName);
				builder.append("`" + columName + "`=BINARY('" + columValue + "') AND ");
			}
			
			QueuedLogger.push(Level.DEBUG, paramSet.size() == 0 ? builder.toString() :builder.substring(0, builder.lastIndexOf("AND")-1));
			pstmt = conn.prepareStatement(paramSet.size() == 0 ? builder.toString() :builder.substring(0, builder.lastIndexOf("AND")-1));
			pstmt.executeUpdate();

		} catch( Exception ex ) {
			QueuedLogger.push(Level.ERROR, ex.getMessage(), ex);
			throw new ServerInternalErrorException();
		} finally {
			if( pstmt != null ) try { pstmt.close(); } catch( Exception ex ) {} finally { pstmt = null; }
			if( conn != null ) try { conn.close(); } catch( Exception ex ) {} finally { conn = null; }
		}
	}
}
