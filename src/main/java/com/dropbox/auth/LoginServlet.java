package com.dropbox.auth;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

import javax.naming.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.*;

import com.dropbox.DAOs.PathDAO;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = -367728987746915651L;

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
		String username = request.getParameter("username");
		String password = request.getParameter("password");

		boolean isConfirmed = false;

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
					String p = rs.getString("password");

					if (username.equals(u) && password.equals(p)) {
						isConfirmed = true;
						HttpSession session = request.getSession();
						session.setAttribute("user", username);
						// setting sesion to expiry in 30 mins
						session.setMaxInactiveInterval(30 * 60);

						Cookie userName = new Cookie("user", username);
						userName.setMaxAge(30 * 60);
						response.addCookie(userName);

						break;
					}
				}
			}

			cn.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		if (!isConfirmed) {
			response.sendRedirect("registration.jsp");
		} else {
			HttpSession session = request.getSession(true);

			session.setAttribute("paths", PathDAO.getAllPathsByUser(username));
			response.sendRedirect("loginSuccess.jsp");
		}

	}

}
