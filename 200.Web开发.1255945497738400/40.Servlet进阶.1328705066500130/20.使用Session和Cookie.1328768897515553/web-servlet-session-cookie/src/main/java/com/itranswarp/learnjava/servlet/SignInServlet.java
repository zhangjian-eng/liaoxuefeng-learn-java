package com.itranswarp.learnjava.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/signin")
public class SignInServlet extends HttpServlet {

	private Map<String, String> users = Map.of("bob", "bob123", "alice", "alice123", "tom", "tomcat");

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html");
		PrintWriter pw = resp.getWriter();
		pw.write("<h1>Sign In</h1>");
		pw.write("<form action=\"/signin\" method=\"post\">");
		pw.write("<p>Username: <input name=\"username\"></p>");
		pw.write("<p>Password: <input name=\"password\" type=\"password\"></p>");
		pw.write("<p><button type=\"submit\">Sign In</button> <a href=\"/\">Cancel</a></p>");
		pw.write("</form>");
		pw.flush();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String name = req.getParameter("username");
		String password = req.getParameter("password");
		String expectedPassword = users.get(name.toLowerCase());
		if (expectedPassword != null && expectedPassword.equals(password)) {
			req.getSession().setAttribute("user", name);
			resp.sendRedirect("/");
		} else {
			resp.sendError(HttpServletResponse.SC_FORBIDDEN);
		}
	}
}
