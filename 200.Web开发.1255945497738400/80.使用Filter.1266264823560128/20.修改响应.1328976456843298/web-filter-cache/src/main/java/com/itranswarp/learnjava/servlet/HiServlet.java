package com.itranswarp.learnjava.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/slow/hi")
public class HiServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html");
		// simulate slow operation:
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
		PrintWriter pw = resp.getWriter();
		pw.write("<h1>Hi, Bob!</h1>");
		pw.flush();
	}
}
