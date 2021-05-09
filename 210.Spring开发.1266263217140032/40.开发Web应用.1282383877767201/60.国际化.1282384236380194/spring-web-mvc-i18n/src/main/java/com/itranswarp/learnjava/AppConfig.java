package com.itranswarp.learnjava;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

import org.apache.catalina.Context;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.extension.AbstractExtension;
import com.mitchellbosecke.pebble.extension.Extension;
import com.mitchellbosecke.pebble.extension.Function;
import com.mitchellbosecke.pebble.loader.ServletLoader;
import com.mitchellbosecke.pebble.spring.servlet.PebbleViewResolver;
import com.mitchellbosecke.pebble.template.EvaluationContext;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@ComponentScan
@EnableWebMvc
@EnableTransactionManagement
@PropertySource("classpath:/jdbc.properties")
public class AppConfig {

	public static void main(String[] args) throws Exception {
		Tomcat tomcat = new Tomcat();
		tomcat.setPort(Integer.getInteger("port", 8080));
		tomcat.getConnector();
		Context ctx = tomcat.addWebapp("", new File("src/main/webapp").getAbsolutePath());
		WebResourceRoot resources = new StandardRoot(ctx);
		resources.addPreResources(
				new DirResourceSet(resources, "/WEB-INF/classes", new File("target/classes").getAbsolutePath(), "/"));
		ctx.setResources(resources);
		tomcat.start();
		tomcat.getServer().await();
	}

	// -- Mvc configuration ---------------------------------------------------

	@Bean
	WebMvcConfigurer createWebMvcConfigurer(@Autowired HandlerInterceptor[] interceptors) {
		return new WebMvcConfigurer() {
			@Override
			public void addInterceptors(InterceptorRegistry registry) {
				for (var interceptor : interceptors) {
					registry.addInterceptor(interceptor);
				}
			}

			@Override
			public void addResourceHandlers(ResourceHandlerRegistry registry) {
				registry.addResourceHandler("/static/**").addResourceLocations("/static/");
			}
		};
	}

	@Bean
	LocaleResolver createLocaleResolver() {
		var clr = new CookieLocaleResolver();
		clr.setDefaultLocale(Locale.ENGLISH);
		clr.setDefaultTimeZone(TimeZone.getDefault());
		return clr;
	}

	@Bean("i18n")
	MessageSource createMessageSource() {
		var messageSource = new ResourceBundleMessageSource();
		messageSource.setDefaultEncoding("UTF-8");
		messageSource.setBasename("messages");
		return messageSource;
	}

	// -- pebble view configuration -------------------------------------------

	@Bean
	ViewResolver createViewResolver(@Autowired ServletContext servletContext,
			@Autowired @Qualifier("i18n") MessageSource messageSource) {
		PebbleEngine engine = new PebbleEngine.Builder().autoEscaping(true)
				// cache:
				.cacheActive(false)
				// loader:
				.loader(new ServletLoader(servletContext))
				// extension:
				.extension(createExtension(messageSource))
				// build:
				.build();
		PebbleViewResolver viewResolver = new PebbleViewResolver();
		viewResolver.setPrefix("/WEB-INF/templates/");
		viewResolver.setSuffix("");
		viewResolver.setPebbleEngine(engine);
		return viewResolver;
	}

	private Extension createExtension(MessageSource messageSource) {
		return new AbstractExtension() {
			@Override
			public Map<String, Function> getFunctions() {
				return Map.of("_", new Function() {
					@Override
					public List<String> getArgumentNames() {
						return null;
					}

					@Override
					public Object execute(Map<String, Object> args, PebbleTemplate self, EvaluationContext context,
							int lineNumber) {
						String key = (String) args.get("0");
						List<Object> arguments = this.extractArguments(args);
						Locale locale = (Locale) context.getVariable("__locale__");
						return messageSource.getMessage(key, arguments.toArray(), "???" + key + "???", locale);
					}

					private List<Object> extractArguments(Map<String, Object> args) {
						int i = 1;
						List<Object> arguments = new ArrayList<>();
						while (args.containsKey(String.valueOf(i))) {
							Object param = args.get(String.valueOf(i));
							arguments.add(param);
							i++;
						}
						return arguments;
					}
				});
			}
		};
	}

	// -- jdbc configuration --------------------------------------------------

	@Bean
	DataSource createDataSource(@Value("${jdbc.url}") String jdbcUrl, @Value("${jdbc.username}") String jdbcUsername,
			@Value("${jdbc.password}") String jdbcPassword) {
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl(jdbcUrl);
		config.setUsername(jdbcUsername);
		config.setPassword(jdbcPassword);
		config.addDataSourceProperty("autoCommit", "false");
		config.addDataSourceProperty("connectionTimeout", "5");
		config.addDataSourceProperty("idleTimeout", "60");
		return new HikariDataSource(config);
	}

	@Bean
	JdbcTemplate createJdbcTemplate(@Autowired DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}

	@Bean
	PlatformTransactionManager createTxManager(@Autowired DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}
}
