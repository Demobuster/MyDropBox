package com.dropbox.auth;

import java.io.*;
import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class RegistrationServlet
 */
@WebServlet("/RegistrationServlet")
public class RegistrationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String PATTERN = "^[A-Za-z0-9_-]{3,15}$";

	private boolean validate(final String username, final String password) {
		Pattern nameP, passP;
		Matcher nameM, passM;
		
		nameP = Pattern.compile(PATTERN);
		nameM = nameP.matcher(username);
		
		passP = Pattern.compile(PATTERN);
		passM = passP.matcher(password);
		
		return nameM.matches() && passM.matches();
	}

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RegistrationServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		boolean flagRedirect = true;
		final String FAIL_REDIRECT = "registration.jsp?error=true";

		String username = request.getParameter("username");
		String password = request.getParameter("password");

		if (!validate(username, password)) {
			response.sendRedirect(FAIL_REDIRECT);
		} else {
			Connection cn = null;
			try {
				InitialContext ic = new InitialContext();
				Context initialContext = (Context) ic.lookup("java:comp/env");
				DataSource datasource = (DataSource) initialContext.lookup("jdbc/MySQLDS");
				cn = datasource.getConnection();

				Statement st = cn.createStatement();
				String sql = ("SELECT * FROM users;");
				ResultSet rs = st.executeQuery(sql);
				if (username != null && password != null) {
					while (rs.next()) {
						String u = rs.getString("username");

						if (username.equals(u)) {
							response.sendRedirect(FAIL_REDIRECT);
							flagRedirect = false;

							break;
						}
					}
				}

				cn.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			cn = null;

			try {
				InitialContext ic = new InitialContext();
				Context initialContext = (Context) ic.lookup("java:comp/env");
				DataSource datasource = (DataSource) initialContext
						.lookup("jdbc/MySQLDS");
				cn = datasource.getConnection();
				Statement stmt = cn.createStatement();
				String query = "insert into users (username, password) values ('"
						+ username + "', '" + password + "');";

				stmt.executeUpdate(query);
			} catch (Exception ex) {
				out.println("Exception: " + ex + ex.getMessage());
			}

			if (flagRedirect) {
				response.sendRedirect("signin.html");
			}
		}
		
	}

}
