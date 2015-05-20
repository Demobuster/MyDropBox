package com.dropbox.DAOs;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PathRecord {
	public static void insert(PreparedStatement ps, String username, String path, Date date) throws SQLException {
		ps.setString(1, username);
		ps.setString(2, path);
		ps.setDate(3, date);
		
		ps.executeUpdate();
	}
}
