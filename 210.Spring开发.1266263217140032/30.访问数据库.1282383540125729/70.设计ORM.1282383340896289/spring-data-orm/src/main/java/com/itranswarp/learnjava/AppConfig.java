package com.itranswarp.learnjava;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.itranswarp.learnjava.entity.AbstractEntity;
import com.itranswarp.learnjava.entity.User;
import com.itranswarp.learnjava.orm.DbTemplate;
import com.itranswarp.learnjava.service.UserService;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@ComponentScan
@EnableTransactionManagement
@PropertySource("jdbc.properties")
public class AppConfig {

	public static void main(String[] args) {
		ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		UserService userService = context.getBean(UserService.class);
		if (userService.fetchUserByEmail("bob@example.com") == null) {
			User bob = userService.register("bob@example.com", "bob123", "Bob");
			System.out.println("Registered ok: " + bob);
		}
		if (userService.fetchUserByEmail("alice@example.com") == null) {
			User alice = userService.register("alice@example.com", "helloalice", "Alice");
			System.out.println("Registered ok: " + alice);
		}
		if (userService.fetchUserByEmail("tom@example.com") == null) {
			User tom = userService.register("tom@example.com", "tomcat", "Alice");
			System.out.println("Registered ok: " + tom);
		}
		// 查询所有用户:
		for (User u : userService.getUsers(1)) {
			System.out.println(u);
		}
		System.out.println("login...");
		User tom = userService.login("tom@example.com", "tomcat");
		System.out.println(tom);
		System.out.println(userService.getNameByEmail("alice@example.com"));
		((ConfigurableApplicationContext) context).close();
	}

	@Bean
	DataSource createDataSource(
			// JDBC URL:
			@Value("${jdbc.url}") String jdbcUrl,
			// JDBC username:
			@Value("${jdbc.username}") String jdbcUsername,
			// JDBC password:
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
	DbTemplate createDbTemplate(@Autowired JdbcTemplate jdbcTemplate) {
		return new DbTemplate(jdbcTemplate, AbstractEntity.class.getPackageName());
	}

	@Bean
	PlatformTransactionManager createTxManager(@Autowired DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}
}
