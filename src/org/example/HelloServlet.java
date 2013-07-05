package org.example;

import java.io.IOException;
import java.sql.*;
import javax.naming.InitialContext;
import javax.servlet.ServletConfig; 
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.sql.DataSource;

public class HelloServlet extends HttpServlet {
	InitialContext ic;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		try {

			DataSource ds = (DataSource) ic.lookup("jdbc/mydb");

			Connection conn = ds.getConnection();
			Statement stmt = conn.createStatement();

			ResultSet rs = stmt.executeQuery("select 1");

			response.setContentType("text/html");
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().println("<h1>Hello Servlet</h1>");
			response.getWriter().println(
					"session=" + request.getSession(true).getId());
			response.getWriter().println("<br>");
			while (rs.next()) {
				response.getWriter().println(
						"The result of the SELECT 1 on the database is: "
								+ rs.getString(1));
			}
			rs.close();
			stmt.close();
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		try {
			ic = new InitialContext();
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

}
