package com.autodeskcrm.gerericutils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import com.mysql.cj.protocol.Resultset;
import com.mysql.jdbc.Driver;

/**
 * @author Deepak
 *
 */
public class DataBaseLib {

	static Connection con;
	/**
	 * Connect to Database
	 * @throws SQLException
	 */
	public void connectToDB() throws SQLException {
		/*1. Resister Driver*/
		Driver driverRef = new Driver();
		DriverManager.registerDriver(driverRef);
		/*2. Connect to Database*/
		con = DriverManager.getConnection("jdbc:mysql://localhost:3306/employees", "root", "admin");
		/*3. Create Statement*/
		Statement stm = con.createStatement();	
	}
	
	/** Execute query & return the result
	 * @param query
	 * @return
	 */
	public Resultset executeQuery(String query) {
		 return null;
	}
	
	/**Close the Database
	 * @throws Throwable
	 */
	public void disconnectDB() throws Throwable {
		con.close();
	}







}
