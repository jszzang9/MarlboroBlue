package com.core.persist.mysql;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import com.MarlboroBlueConfiguration;


public class MysqlPersist{
	public static void main(String[] args) throws Exception {

		java.sql.Connection conn;
		Statement stmt;

		try {
			Class.forName("org.gjt.mm.mysql.Driver");//드라이버 로딩: DriverManager에 등록
		}catch(ClassNotFoundException e) {
			System.err.print("ClassNotFoundException: ");
		}

		try {
			conn = DriverManager.getConnection(
					MarlboroBlueConfiguration.getProperty("jdbc.url"),
					MarlboroBlueConfiguration.getProperty("jdbc.user"),
					MarlboroBlueConfiguration.getProperty("jdbc.passwd"));//Connection 객체를 얻어냄
			
			stmt = conn.createStatement();//Statement 객체를 얻어냄
			stmt.close();
			conn.close();
		}catch(SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
		} 
	}
}
