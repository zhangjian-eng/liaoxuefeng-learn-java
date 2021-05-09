package com.itranswarp.learnjava.web;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;

@Component
public class MvcInterceptor implements HandlerInterceptor {

	@Autowired
	LocaleResolver localeResolver;

	@Autowired
	@Qualifier("i18n")
	MessageSource messageSource;

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		if (modelAndView != null) {
			Locale locale = localeResolver.resolveLocale(request);
			modelAndView.addObject("__messageSource__", messageSource);
			modelAndView.addObject("__locale__", locale);
		}
	}
}
