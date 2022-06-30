package net.ifonlygaram.util;

import java.sql.Connection;
import java.sql.DriverManager;

import org.springframework.beans.factory.annotation.Autowired;

import lombok.Setter;

public class DBConn {
	@Setter @Autowired
	private Connection connection;
	
	public static Connection getConnection() {
//		Connection connection = null;
//		
//		try {
//			Class.forName("oracle.jdbc.driver.OracleDriver");
//			connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","board", "1234");
////			connection = DriverManager.getConnection("jdbc:oracle:thin:@human.lepelaka.net:1521:xe","USER10", "USER10");
////			connection = DriverManager.getConnection("jdbc:oracle:thin:@db.ifonlygaram.net:1521:xe","JSPUSER", "1234");
////			System.out.println("getConnection complete!");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		return DBConn.getConnection();
	}
	
	public static void main(String[] args) {
		getConnection();
	}
}
