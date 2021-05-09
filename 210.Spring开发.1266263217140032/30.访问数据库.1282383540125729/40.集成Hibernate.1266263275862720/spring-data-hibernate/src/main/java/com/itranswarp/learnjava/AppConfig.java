package com.itranswarp.learnjava;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.itranswarp.learnjava.entity.AbstractEntity;
import com.itranswarp.learnjava.entity.User;
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
			User alice = userService.register("alice@example.com", "helloalice", "Bob");
			System.out.println("Registered ok: " + alice);
		}
		// 查询所有用户:
		for (User u : userService.getUsers(1)) {
			System.out.println(u);
		}
		User bob = userService.login("bob@example.com", "bob123");
		System.out.println(bob);
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
	LocalSessionFactoryBean createSessionFactory(@Autowired DataSource dataSource) {
		var props = new Properties();
		props.setProperty("hibernate.hbm2ddl.auto", "update"); // 生产环境不要使用
		props.setProperty("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
		props.setProperty("hibernate.show_sql", "true");
		var sessionFactoryBean = new LocalSessionFactoryBean();
		sessionFactoryBean.setDataSource(dataSource);
		// 扫描指定的package获取所有entity class:
		sessionFactoryBean.setPackagesToScan(AbstractEntity.class.getPackageName());
		sessionFactoryBean.setHibernateProperties(props);
		return sessionFactoryBean;
	}

	@Bean
	HibernateTemplate createHibernateTemplate(@Autowired SessionFactory sessionFactory) {
		return new HibernateTemplate(sessionFactory);
	}

	@Bean
	PlatformTransactionManager createTxManager(@Autowired SessionFactory sessionFactory) {
		return new HibernateTransactionManager(sessionFactory);
	}
}
