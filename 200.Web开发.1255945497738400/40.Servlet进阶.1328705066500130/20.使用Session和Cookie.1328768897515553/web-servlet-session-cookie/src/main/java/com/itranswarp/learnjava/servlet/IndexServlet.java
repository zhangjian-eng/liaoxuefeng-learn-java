package com.itranswarp.learnjava.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/")
public class IndexServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String user = (String) req.getSession().getAttribute("user");
		String lang = parseLanguageFromCookie(req);
		resp.setContentType("text/html");
		resp.setCharacterEncoding("UTF-8");
		PrintWriter pw = resp.getWriter();
		if (lang.equals("zh")) {
			pw.write("<h1>你好, " + (user != null ? user : "Guest") + "</h1>");
			if (user == null) {
				pw.write("<p><a href=\"/signin\">登录</a></p>");
			} else {
				pw.write("<p><a href=\"/signout\">登出</a></p>");
			}
		} else {
			pw.write("<h1>Welcome, " + (user != null ? user : "Guest") + "</h1>");
			if (user == null) {
				pw.write("<p><a href=\"/signin\">Sign In</a></p>");
			} else {
				pw.write("<p><a href=\"/signout\">Sign Out</a></p>");
			}
		}
		pw.write("<p><a href=\"/pref?lang=en\">English</a> | <a href=\"/pref?lang=zh\">中文</a>");
		pw.flush();
	}

	private String parseLanguageFromCookie(HttpServletRequest req) {
		Cookie[] cookies = req.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("lang")) {
					return cookie.getValue();
				}
			}
		}
		return "en";
	}
}
