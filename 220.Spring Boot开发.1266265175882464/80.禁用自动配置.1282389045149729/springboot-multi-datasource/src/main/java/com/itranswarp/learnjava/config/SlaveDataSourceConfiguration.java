package com.itranswarp.learnjava.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

public class SlaveDataSourceConfiguration {

	@Bean("slaveDataSourceProperties")
	@ConfigurationProperties("spring.datasource-slave")
	DataSourceProperties dataSourceProperties() {
		return new DataSourceProperties();
	}

	@Bean(RoutingDataSourceContext.SLAVE_DATASOURCE)
	DataSource dataSource(@Autowired @Qualifier("slaveDataSourceProperties") DataSourceProperties props) {
		return props.initializeDataSourceBuilder().build();
	}
}
