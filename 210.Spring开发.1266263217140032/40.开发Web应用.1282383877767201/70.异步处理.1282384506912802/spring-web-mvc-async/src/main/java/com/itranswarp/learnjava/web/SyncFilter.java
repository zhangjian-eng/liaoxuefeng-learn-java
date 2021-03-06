package com.itranswarp.learnjava.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SyncFilter implements Filter {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		logger.info("start SyncFilter...");
		chain.doFilter(request, response);
		logger.info("end SyncFilter.");
	}
}
