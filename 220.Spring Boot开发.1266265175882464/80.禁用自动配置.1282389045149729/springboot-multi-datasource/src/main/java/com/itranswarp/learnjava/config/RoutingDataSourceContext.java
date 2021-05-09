package com.itranswarp.learnjava.config;

public class RoutingDataSourceContext implements AutoCloseable {

	public static final String MASTER_DATASOURCE = "masterDataSource";
	public static final String SLAVE_DATASOURCE = "slaveDataSource";

	// holds data source key in thread local:
	static final ThreadLocal<String> threadLocalDataSourceKey = new ThreadLocal<>();

	public static String getDataSourceRoutingKey() {
		String key = threadLocalDataSourceKey.get();
		return key == null ? MASTER_DATASOURCE : key;
	}

	public RoutingDataSourceContext(String key) {
		threadLocalDataSourceKey.set(key);
	}

	public void close() {
		threadLocalDataSourceKey.remove();
	}
}
