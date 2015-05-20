package com.dropbox.DAOs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class UsersDAO {

	public static ArrayList<String> getAllByUser(String username) {
		ArrayList<String> paths = new ArrayList<String>();

		ResultSet rs = null;
		Connection cn = null;
		try {
			InitialContext ic = new InitialContext();
			Context initialContext = (Context) ic.lookup("java:comp/env");
			DataSource datasource = (DataSource) initialContext.lookup("jdbc/MySQLDS");
			cn = datasource.getConnection();

			Statement st = cn.createStatement();
			String sql = ("SELECT * FROM users;");
			rs = st.executeQuery(sql);
			
			while (rs.next()) {
				String u = rs.getString("username");
				
				if (username.equals(u)) {
					String path = rs.getString("path");
					paths.add(path);
				}
			}
			
			cn.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return paths;
	}
	
	public static void persist(String username, String password) {
		Connection result = null;
		try {
			InitialContext ic = new InitialContext();
			Context initialContext = (Context) ic.lookup("java:comp/env");
			DataSource datasource = (DataSource) initialContext.lookup("jdbc/MySQLDS");
			result = datasource.getConnection();

			try {
				String sql = "INSERT INTO paths(username,path,date) VALUES(?,?,?)";
				PreparedStatement ps = result.prepareStatement(sql);
				UsersRecord.insert(ps, username, password);

			} finally {
				if (result != null) {
					result.close();
				}
			}
		} catch (Exception ex) {

		}
	}

}
