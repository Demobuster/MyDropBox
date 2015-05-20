package com.dropbox.DAOs;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UsersRecord {
	public static void insert(PreparedStatement ps, String username, String password) throws SQLException {
		ps.setString(1, username);
		ps.setString(2, password);
		
		ps.executeUpdate();
	}
}
