package com.core.persist.mysql;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.core.persist.CommonDB;

public class Mysql {
	public static void main(String...args) {
		java.sql.Connection conn;
		Statement stmt;
		ResultSet rs;
		try {
			Class.forName("com.mysql.jdbc.Driver");
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}

		try {
			conn = DriverManager.getConnection(CommonDB.MYSQL_URL, CommonDB.MYSQL_USER, CommonDB.MYSQL_PASSWORD);
			stmt = conn.createStatement();
			rs = stmt.executeQuery("show tables");
			if (stmt.execute("show tables")) {
				rs = stmt.getResultSet();
			}

			while (rs.next()) {
				String str =  rs.getNString(1);
				System.out.println(str);
			}
			stmt.close();
			conn.close();

		} catch (SQLException e) {
			System.out.println("SQLException : " + e.getMessage());
		}
	}
}
