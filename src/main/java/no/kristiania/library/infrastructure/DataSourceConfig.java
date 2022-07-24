package no.kristiania.library.infrastructure;

import org.actioncontroller.config.ConfigListener;
import org.actioncontroller.config.ConfigMap;
import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;

import javax.sql.DataSource;

public class DataSourceConfig implements ConfigListener.Transformer<DataSource> {
    @Override
    public DataSource apply(ConfigMap config) {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL(config.getOrDefault("url", "jdbc:h2:mem:library;DB_CLOSE_DELAY=-1"));
        dataSource.setUser(config.getOrDefault("username", null));
        dataSource.setPassword(config.getOrDefault("password", null));
        Flyway.configure().dataSource(dataSource).load().migrate();
        return dataSource;
    }
}
