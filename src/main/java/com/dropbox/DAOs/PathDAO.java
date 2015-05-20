package com.dropbox.DAOs;

import java.sql.*;
import java.sql.Date;
import java.util.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class PathDAO {

	public static ArrayList<String> getAllPathsByUser(String username) {
		ArrayList<String> paths = new ArrayList<String>();

		ResultSet rs = null;
		Connection cn = null;
		try {
			InitialContext ic = new InitialContext();
			Context initialContext = (Context) ic.lookup("java:comp/env");
			DataSource datasource = (DataSource) initialContext.lookup("jdbc/MySQLDS");
			cn = datasource.getConnection();

			Statement st = cn.createStatement();
			String sql = ("SELECT * FROM paths;");
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
	
	public static void persist(String username, String path, Date date) {
		Connection result = null;
		try {
			InitialContext ic = new InitialContext();
			Context initialContext = (Context) ic.lookup("java:comp/env");
			DataSource datasource = (DataSource) initialContext.lookup("jdbc/MySQLDS");
			result = datasource.getConnection();

			try {
				String sql = "INSERT INTO paths(username,path,date) VALUES(?,?,?)";
				PreparedStatement ps = result.prepareStatement(sql);
				PathRecord.insert(ps, username, path, date);

			} finally {
				if (result != null) {
					result.close();
				}
			}
		} catch (Exception ex) {

		}
	}

}
