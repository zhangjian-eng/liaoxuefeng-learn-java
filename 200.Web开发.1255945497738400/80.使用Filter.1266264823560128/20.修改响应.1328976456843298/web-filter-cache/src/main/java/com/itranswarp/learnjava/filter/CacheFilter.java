package com.itranswarp.learnjava.filter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.WriteListener;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

@WebFilter("/slow/*")
public class CacheFilter implements Filter {

	private Map<String, byte[]> cache = new ConcurrentHashMap<>();

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		String url = req.getRequestURI();
		byte[] data = this.cache.get(url);
		resp.setHeader("X-Cache-Hit", data == null ? "No" : "Yes");
		if (data == null) {
			CachedHttpServletResponse wrapper = new CachedHttpServletResponse(resp);
			chain.doFilter(request, wrapper);
			data = wrapper.getContent();
			cache.put(url, data);
		}
		ServletOutputStream output = resp.getOutputStream();
		output.write(data);
		output.flush();
	}
}

class CachedHttpServletResponse extends HttpServletResponseWrapper {

	private boolean open = false;
	private ByteArrayOutputStream output = new ByteArrayOutputStream();

	public CachedHttpServletResponse(HttpServletResponse response) {
		super(response);
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		if (open) {
			throw new IllegalStateException("Cannot re-open writer!");
		}
		open = true;
		return new PrintWriter(output, false, StandardCharsets.UTF_8);
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		if (open) {
			throw new IllegalStateException("Cannot re-open output stream!");
		}
		open = true;
		return new ServletOutputStream() {
			@Override
			public boolean isReady() {
				return true;
			}

			@Override
			public void setWriteListener(WriteListener listener) {
			}

			@Override
			public void write(int b) throws IOException {
				output.write(b);
			}
		};
	}

	public byte[] getContent() {
		return output.toByteArray();
	}
}
