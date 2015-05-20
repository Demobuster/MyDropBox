package com.dropbox.auth;

import java.sql.*;

public class Record {
	public static void insert(PreparedStatement ps, String username, String password) throws SQLException {
		ps.setString(1, username);
		ps.setString(2, password);
		
		ps.executeUpdate();
	}
	
}
