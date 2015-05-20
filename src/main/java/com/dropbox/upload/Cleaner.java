package com.dropbox.upload;

import java.io.*;
import java.sql.Connection;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

/**
 * Servlet implementation class Cleaner
 */
@WebServlet("/Cleaner")
public class Cleaner extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public static void deleteDirectory(File file) throws IOException {

		if (file.isDirectory()) {
			if (file.list().length == 0) {

				file.delete();

			} else {
				String files[] = file.list();

				for (String temp : files) {
					File fileDelete = new File(file, temp);

					deleteDirectory(fileDelete);
				}

				if (file.list().length == 0) {
					file.delete();
				}
			}

		} else {
			file.delete();
		}
	}

	private static void deleteDBRecord(String username, String path) throws Exception {
		if (!path.contains("/")) {
			path = ("/" + path);
		}
		
		Connection cn = null;

		InitialContext ic = new InitialContext();
		Context initialContext = (Context) ic.lookup("java:comp/env");
		DataSource datasource = (DataSource) initialContext.lookup("jdbc/MySQLDS");
		cn = datasource.getConnection();
		Statement stmt = cn.createStatement();
		String query = "DELETE FROM paths WHERE username = '" + username + "' AND path = '" + path + "';";

		stmt.executeUpdate(query);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		String path = System.getenv("OPENSHIFT_DATA_DIR")
				+ session.getAttribute("user") + "/"
				+ request.getParameter("urlToDelete");
		PrintWriter out = response.getWriter();

		try {
			deleteDirectory(new File(path));
		} catch (IOException e) {
			e.printStackTrace(out);
		}

		try {
			deleteDBRecord((String) session.getAttribute("user"), request.getParameter("urlToDelete"));
		} catch (Exception ex) {
			out.println("Exception: " + ex + ex.getMessage());
		}
		
		response.sendRedirect("Service");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
