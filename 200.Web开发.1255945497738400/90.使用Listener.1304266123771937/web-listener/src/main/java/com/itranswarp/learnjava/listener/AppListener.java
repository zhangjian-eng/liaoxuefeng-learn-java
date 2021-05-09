package com.itranswarp.learnjava.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class AppListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		System.out.println("WebApp initialized. ServletContext = " + sce.getServletContext());
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		System.out.println("WebApp destroyed.");
	}

}
