package org.chess.quasimodo.database;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.embedded.ConnectionProperties;
import org.springframework.jdbc.datasource.embedded.DataSourceFactory;
import org.springframework.stereotype.Service;
import org.sqlite.JDBC;

@Service ("dataSourceFactory")
public class SqliteDataSourceFactory implements DataSourceFactory {
	private final SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
	
	public SqliteDataSourceFactory() {
		dataSource.setDriverClass(JDBC.class);
	}
	
	@Override
	public ConnectionProperties getConnectionProperties() {
		return null;
	}
	

	public void setUrl(String url) {
		this.dataSource.setUrl(url);
	}

	@Override
	public DataSource getDataSource() {
		return this.dataSource;
	}

}
