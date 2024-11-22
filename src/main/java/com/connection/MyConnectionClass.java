package com.connection;

import java.sql.Connection;
import java.sql.DriverManager;

public class MyConnectionClass {

	public static Connection conn;

	public static Connection GetConnection() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jsonmanuplation", "root", "root");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}

}
