package com.itranswarp.learnjava.web;

import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.LocaleResolver;

@Controller
public class LocaleController {

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	LocaleResolver localeResolver;

	@GetMapping("/locale/{lo}")
	public String setLocale(@PathVariable("lo") String lo, HttpServletRequest request, HttpServletResponse response) {
		Locale locale = null;
		int pos = lo.indexOf('_');
		if (pos > 0) {
			String lang = lo.substring(0, pos);
			String country = lo.substring(pos + 1);
			locale = new Locale(lang, country);
		} else {
			locale = new Locale(lo);
		}
		localeResolver.setLocale(request, response, locale);
		logger.info("locale is set to {}.", locale);
		String referer = request.getHeader("Referer");
		return "redirect:" + (referer == null ? "/" : referer);
	}
}
