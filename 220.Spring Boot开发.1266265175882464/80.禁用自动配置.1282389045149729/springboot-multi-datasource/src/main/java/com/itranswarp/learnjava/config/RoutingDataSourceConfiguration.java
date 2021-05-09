package com.itranswarp.learnjava.config;

import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class RoutingDataSourceConfiguration {

	@Primary
	@Bean
	DataSource dataSource(@Autowired @Qualifier(RoutingDataSourceContext.MASTER_DATASOURCE) DataSource masterDataSource,
			@Autowired @Qualifier(RoutingDataSourceContext.SLAVE_DATASOURCE) DataSource slaveDataSource) {
		var ds = new RoutingDataSource();
		ds.setTargetDataSources(Map.of(RoutingDataSourceContext.MASTER_DATASOURCE, masterDataSource,
				RoutingDataSourceContext.SLAVE_DATASOURCE, slaveDataSource));
		ds.setDefaultTargetDataSource(masterDataSource);
		return ds;
	}

	@Bean
	JdbcTemplate jdbcTemplate(@Autowired DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}

	@Bean
	DataSourceTransactionManager dataSourceTransactionManager(@Autowired DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}
}

class RoutingDataSource extends AbstractRoutingDataSource {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	protected Object determineCurrentLookupKey() {
		return RoutingDataSourceContext.getDataSourceRoutingKey();
	}

	@Override
	protected DataSource determineTargetDataSource() {
		DataSource ds = super.determineTargetDataSource();
		logger.info("determin target datasource: {}", ds);
		return ds;
	}
}
