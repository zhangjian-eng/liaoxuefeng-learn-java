package com.itranswarp.learnjava.servlet;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/static/*")
public class FileServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ServletContext ctx = req.getServletContext();
		// remove context-path:
		String urlPath = req.getRequestURI().substring(ctx.getContextPath().length());
		String filepath = ctx.getRealPath(urlPath);
		if (filepath == null) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		Path path = Paths.get(filepath);
		if (!path.toFile().isFile()) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		String mime = Files.probeContentType(path);
		if (mime == null) {
			mime = "application/octet-stream";
		}
		resp.setContentType(mime);
		OutputStream output = resp.getOutputStream();
		try (InputStream input = new BufferedInputStream(new FileInputStream(filepath))) {
			input.transferTo(output);
		}
		output.flush();
	}
}
