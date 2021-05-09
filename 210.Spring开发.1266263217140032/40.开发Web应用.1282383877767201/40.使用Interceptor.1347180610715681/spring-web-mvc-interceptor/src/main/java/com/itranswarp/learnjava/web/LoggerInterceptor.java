package com.itranswarp.learnjava.web;
 
import java.io.PrintWriter;
import java.time.LocalDateTime;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Order(1)
@Component
public class LoggerInterceptor implements HandlerInterceptor {

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		logger.info("preHandle {}...", request.getRequestURI());
		if (request.getParameter("debug") != null) {
			PrintWriter pw = response.getWriter();
			pw.write("<p>DEBUG MODE</p>");
			pw.flush();
			return false;
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		logger.info("postHandle {}.", request.getRequestURI());
		if (modelAndView != null) {
			modelAndView.addObject("__time__", LocalDateTime.now());
		}
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		logger.info("afterCompletion {}: exception = {}", request.getRequestURI(), ex);
	}
}
